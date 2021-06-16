package divyaganesh.parking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import divyaganesh.parking.databinding.ActivityAddParkingBinding;
import divyaganesh.parking.databinding.ActivityParkingListBinding;
import divyaganesh.parking.helpers.LocationHelper;
import divyaganesh.parking.helpers.RecursiveMethods;
import divyaganesh.parking.model.Parking;
import divyaganesh.parking.viewmodels.ParkingViewModel;

public class AddParking extends AppCompatActivity implements Serializable {

    ActivityAddParkingBinding binding;
    private final String TAG = this.getClass().getCanonicalName();
    private Parking parking;
    private ParkingViewModel parkingCallDB;
    private String email;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private LocationHelper locationHelper;
    private Location currentLocation;
    private String obtainedAddress;
    private Double obtainedLat, obtainedLong;

    public boolean forEdit = false;
    public Parking editParkingObj;

    private boolean clickedLocation = false;
    RecursiveMethods fun = new RecursiveMethods();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityAddParkingBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        Intent i = getIntent();
        this.forEdit = i.getBooleanExtra("forEdit", false);
        if (forEdit) {
            editParkingObj = (Parking) getIntent().getSerializableExtra("EditParking");
            getWindow().setNavigationBarColor(getResources().getColor(R.color.teal_700));
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
            setTitle("Update Parking");
            setValues(editParkingObj);
            //Disabling fetch date button for update/edit parking since date should not be allowed to change
            this.binding.addParkingCalendarBtn.setVisibility(View.GONE);
        }

        this.parking = new Parking();
        this.parkingCallDB = ParkingViewModel.getInstance(this.getApplication());


        this.email = fun.getCurrentUser(this);

        calendar = Calendar.getInstance();

        this.locationHelper = LocationHelper.getInstance();
        this.locationHelper.checkPermissions(this);

        this.binding.addParkingCurrentLocationField.setFocusable(false);
        this.binding.addParkingDateField.setFocusable(false);

        //fetches the system date & time
        this.binding.addParkingCalendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                String date = dateFormat.format(calendar.getTime());
                binding.addParkingDateField.setText(date);
                binding.addParkingDateField.setFocusable(false);
            }
        });

        this.binding.addParkingFetchCurrentLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fun.logCatD(TAG, "onClick: Fetch Current Location clicked");
                fetchLocation();
                clickedLocation = true;
//                binding.addParkingAddressField.setFocusable(false);
//                binding.addParkingFetchAddressBtn.setClickable(false);
                binding.addParkingAddressField.setText("");
            }
        });

        this.binding.addParkingFetchAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
//                    binding.addParkingFetchCurrentLocBtn.setClickable(false);
                    binding.addParkingCurrentLocationField.setText("");
                    fetchLatLong(binding.addParkingAddressField.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    fun.toastMessageLong(getApplicationContext(), "Exception " + e.getLocalizedMessage());
                }
            }
        });

    }

    private void setValues(Parking parking) {
        this.binding.addParkingBuildingField.setText(parking.getBuildingNo());
        this.binding.addParkingAddressField.setText(parking.getAddress());
        this.binding.addParkingDateField.setText(parking.getDate());
        this.binding.addParkingHoursField.setText(parking.getHours());
        this.binding.addParkingSuitField.setText(parking.getHostNo());
        this.binding.addParkingLicenceField.setText(parking.getCarNo());
        this.binding.addParkingCurrentLocationField.setText(parking.getAddress());
        //disable
        this.binding.addParkingLicenceField.setFocusable(false);
        this.binding.addParkingCalendarBtn.setClickable(false);
        this.binding.addParkingDateField.setFocusable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fun.checkIfSignUserAvailable(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_parking_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_parking: {
                if (checkFieldValidation()) {
                    fun.logCatD("AddParking", "onOptionsItemSelected: Save clicked");
                    saveToDB();
                    fun.toastMessageLong(this, "Parking Details Saved to DB");
                    clearFields();
                    //Removed finish() because of bug - after first add parking, two items are displayed in parking list
                }
                break;
            }
            case R.id.update_add: {
                //update profile - bug fix, added the functionality
                String currentUser = fun.getCurrentUser(this);
                Intent updateProfileIntent = new Intent(this, UpdateProfileActivity.class);
                updateProfileIntent.putExtra("CurrentUser", currentUser);
                startActivity(updateProfileIntent);
                break;
            }
            case R.id.signOut_add: {
                fun.signOut(this);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    Function that fetches the current location
     */
    public void fetchLocation() {
        if (locationHelper.locationPermissionGranted) {
            locationHelper.getLocation(this).observe(this, new Observer<Location>() {
                @Override
                public void onChanged(Location location) {
                    if (location != null) {
                        currentLocation = location;
                        obtainedAddress = locationHelper.getCurrentAddress(getApplicationContext(), currentLocation);
                        obtainedLat = currentLocation.getLatitude();
                        obtainedLong = currentLocation.getLongitude();
                        binding.addParkingCurrentLocationField.setText(obtainedAddress);
                        binding.addParkingCurrentLocationField.setFocusable(false);

                        fun.logCatD("AddParking", "LocationResult: " + currentLocation.toString());
                    } else {
                        fun.logCatE("AddParking", "Location Not available");
                    }
                }
            });
        }
    }

    /*
    Function that fetches the lat & long and the address based on the input from user
     */
    public void fetchLatLong(String address) throws IOException {
        Geocoder geocoder = new Geocoder(getApplicationContext());
        String addressToSearch = address;
        List<Address> fromLocationName = null;
        fromLocationName = geocoder.getFromLocationName(addressToSearch, 1);
        if (fromLocationName != null && fromLocationName.size() > 0) {
            Address a = fromLocationName.get(0);
            Log.d(TAG, "fetchLatLong: Address " +a);
            this.obtainedLat = a.getLatitude();
            this.obtainedLong = a.getLongitude();
            this.obtainedAddress= a.getAddressLine(0);
            Log.d(TAG, "fetchLatLong: Obtained " +obtainedAddress);
            fun.toastMessageLong(getApplicationContext(), String.valueOf(this.obtainedLat));
            fun.logCatD("Here Lat", String.valueOf(this.obtainedLat));
            fun.logCatD("Here Long", String.valueOf(this.obtainedLong));
            fun.toastMessageLong(getApplicationContext(),"Entered location is : " +obtainedAddress);
        }else{
            fun.logCatD("Here ", "No Value found");
            fun.toastMessageLong(getApplicationContext(),"No address found with this!");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == this.locationHelper.request_code_location){
            this.locationHelper.locationPermissionGranted = (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);

            if(this.locationHelper.locationPermissionGranted){
                //fetch the device location
                fun.logCatD(TAG, "onCreate: Location Permission Granted" + this.locationHelper.locationPermissionGranted);
            }
        }
    }

    /*
    @checkFieldValidation function : to check the field validations
     */
    public boolean checkFieldValidation(){
        String buildNO = this.binding.addParkingBuildingField.getText().toString();
        String hours = this.binding.addParkingHoursField.getText().toString();
        String licence = this.binding.addParkingLicenceField.getText().toString();
        String suitNo = this.binding.addParkingSuitField.getText().toString();
        Boolean isValid = true;

        if(!checkBuildingNo(buildNO) || buildNO.isEmpty()){
            this.binding.addParkingBuildingField.setError("Building No - should be exactly 5 alphanum characters");
            isValid = false;
        }
        if(!checkHours(hours) || hours.isEmpty()){
            this.binding.addParkingHoursField.setError("Hours - Format : Hours & Mins");
            isValid = false;
        }
        if(!checkLicenceNo(licence) || licence.isEmpty()){
            this.binding.addParkingLicenceField.setError("Licence - should be min 2, max 8 alphanum characters");
            isValid = false;
        }
        if(!checkSuitNo(suitNo) || suitNo.isEmpty()){
            this.binding.addParkingSuitField.setError("Suit No - should be min 2, max 5 alphanum characters");
            isValid = false;
        }
        if(this.binding.addParkingDateField.getText().toString().isEmpty()){
            fun.toastMessageShort(this, "Click the icon to fetch the Date");
            isValid = false;
        }
        if(this.binding.addParkingCurrentLocationField.getText().toString().isEmpty() && this.binding.addParkingAddressField.getText().toString().isEmpty()){
            fun.toastMessageShort(this, "Click the icon to fetch the address or enter the address");
            isValid = false;
        }
        //bug fix for update Parking
        if(this.obtainedAddress == null || this.obtainedLong == null || this.obtainedLat == null){
            fun.toastMessageLong(this, "Click on current location or location by address buttons to proceed");
            isValid = false;
        }
        return isValid;
    }

    /*
    @saveToDB function : to save the add parking details to firebase
     */

    public void saveToDB(){
        fun.logCatD(TAG, "saveToDB: Calling Save to DB");

        if(forEdit){
            Parking toUpdate = editParkingObj;
            fun.logCatD("Here to", editParkingObj.toString());
            toUpdate.setBuildingNo(this.binding.addParkingBuildingField.getText().toString());
            toUpdate.setHours(this.binding.addParkingHoursField.getText().toString());
            toUpdate.setHostNo(this.binding.addParkingSuitField.getText().toString());
            //location part is tricky
            //need to implement address part
//            if(clickedLocation){
//                toUpdate.setAddress(this.obtainedAddress);
//                toUpdate.setLat(this.currentLocation.getLatitude());
//                toUpdate.setLong(this.currentLocation.getLongitude());
//            }

            //another method to save the location to DB - @Divya
            toUpdate.setAddress(this.obtainedAddress);
            toUpdate.setLat(this.obtainedLat);
            toUpdate.setLong(this.obtainedLong);
            fun.logCatD("Here to", toUpdate.toString());
            //db call
            this.parkingCallDB.updateParkingDetails(toUpdate);
        }else{
            this.parking.setBuildingNo(this.binding.addParkingBuildingField.getText().toString());
            this.parking.setHours(this.binding.addParkingHoursField.getText().toString());
            this.parking.setCarNo(this.binding.addParkingLicenceField.getText().toString());
            this.parking.setHostNo(this.binding.addParkingSuitField.getText().toString());
            this.parking.setDate(this.binding.addParkingDateField.getText().toString());
            this.parking.setEmail(this.email);
            this.parking.setAddress(this.obtainedAddress);
            this.parking.setLat(this.obtainedLat);
            this.parking.setLong(this.obtainedLong);
            this.parkingCallDB.addParkingDetails(this.parking);
        }


    }

    private void clearFields(){
        this.binding.addParkingBuildingField.getText().clear();
        this.binding.addParkingHoursField.getText().clear();
        this.binding.addParkingLicenceField.getText().clear();
        this.binding.addParkingSuitField.getText().clear();
        this.binding.addParkingDateField.getText().clear();
        this.binding.addParkingCurrentLocationField.getText().clear();
        this.binding.addParkingAddressField.getText().clear();
    }

    /*
    @regex functions : contains all the field validation checks using the regular expressions
     */
    private boolean checkBuildingNo(String buildNo){
        String buildRegex = "^\\w{5}$";
        return buildNo != null && buildNo.matches(buildRegex);
    }

    private boolean checkHours(String hours){
        String hoursRegex = "^\\b((\\d+(\\.\\d+)?)\\s*(h|hr|hrs?|hours?))?(\\s*(\\d+)\\s*(m|min|mins?|minutes?))?\\b";
        String hoursRegexN = "^[0-9]+$";
        return hours != null && hours.matches(hoursRegex);
    }

    private boolean checkLicenceNo(String licence){
        String licenceRegex = "^\\w{2,8}$";
        return licence != null && licence.matches(licenceRegex);
    }

    private boolean checkSuitNo(String suitNo){
        String suitRegex = "^[a-zA-Z0-9_]{2,5}$";
        return  suitNo != null && suitNo.matches(suitRegex);
    }
}