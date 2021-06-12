package divyaganesh.parking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import divyaganesh.parking.databinding.ActivityAddParkingBinding;
import divyaganesh.parking.databinding.ActivityParkingListBinding;
import divyaganesh.parking.helpers.LocationHelper;
import divyaganesh.parking.helpers.RecursiveMethods;
import divyaganesh.parking.model.Parking;
import divyaganesh.parking.viewmodels.ParkingViewModel;

public class AddParking extends AppCompatActivity {

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

    RecursiveMethods fun = new RecursiveMethods();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_parking);

        this.binding = ActivityAddParkingBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.parking = new Parking();
        this.parkingCallDB = ParkingViewModel.getInstance(this.getApplication());

        Intent i = getIntent();
        this.email = i.getStringExtra("CurrentUser");

        calendar = Calendar.getInstance();

        this.locationHelper = LocationHelper.getInstance();
        this.locationHelper.checkPermissions(this);

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
                Log.d(TAG, "onClick: Fetch Current Location clicked");
                fetchLocation();
            }
        });
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
                if(checkFieldValidation()) {
                    Log.d(TAG, "onOptionsItemSelected: Save clicked");
                    saveToDB();
                    fun.toastMessageLong(this,"Parking Details Saved to DB");
                    clearFields();
                }
                break;
            }
            case R.id.update_add: {
                //update profile
                break;
            }
            case R.id.signOut_add: {
                fun.signOut(this);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void fetchLocation(){
        if(locationHelper.locationPermissionGranted){
            locationHelper.getLastLocation(this).observe(this, new Observer<Location>() {
                @Override
                public void onChanged(Location location) {
                    if(location != null){
                        currentLocation = location;
//                        binding.tvLocationInfo.setText(lastLocation.toString());
                        obtainedAddress = locationHelper.getAddress(getApplicationContext(),currentLocation);
                        binding.addParkingCurrentLocationField.setText(obtainedAddress);
                        binding.addParkingCurrentLocationField.setFocusable(false);
                        Log.d(TAG, "onCreate: Last Location received" + currentLocation.toString());
                    }else {
                        Log.d(TAG, "onChanged: Location Not available");
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == this.locationHelper.request_code_location){
            this.locationHelper.locationPermissionGranted = (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);

            if(this.locationHelper.locationPermissionGranted){
                //fetch the device location
                Log.d(TAG, "onCreate: Location Permission Granted" + this.locationHelper.locationPermissionGranted);
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
        return isValid;
    }

    /*
    @saveToDB function : to save the add parking details to firebase
     */

    public void saveToDB(){
        Log.d(TAG, "saveToDB: Calling Save to DB");
        this.parking.setBuildingNo(this.binding.addParkingBuildingField.getText().toString());
        this.parking.setHours(this.binding.addParkingHoursField.getText().toString());
        this.parking.setCarNo(this.binding.addParkingLicenceField.getText().toString());
        this.parking.setHostNo(this.binding.addParkingSuitField.getText().toString());
        this.parking.setDate(this.binding.addParkingDateField.getText().toString());
        this.parking.setEmail(this.email);
        this.parking.setAddress(this.obtainedAddress);
        this.parking.setLat(this.currentLocation.getLatitude());
        this.parking.setLong(this.currentLocation.getLongitude());

        this.parkingCallDB.addParkingDetails(this.parking);
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