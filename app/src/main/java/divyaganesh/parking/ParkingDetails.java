package divyaganesh.parking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import divyaganesh.parking.databinding.ActivityParkingDetailsBinding;
import divyaganesh.parking.helpers.RecursiveMethods;
import divyaganesh.parking.model.Parking;
import divyaganesh.parking.viewmodels.ParkingViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;

public class ParkingDetails extends AppCompatActivity implements OnMapReadyCallback, Serializable {

    Parking detailParkingObj;
    RecursiveMethods fun = new RecursiveMethods();
    private final String TAG = this.getClass().getCanonicalName();

    String buildingText, addressText, carText, hoursText, dateText, hostText;

    //map - MapViewBundleKey
    private MapView mMapView;

    //Importing viewModel class
    ParkingViewModel parkingViewModel;

    ActivityParkingDetailsBinding binding;

    /**
     * current Parking Details gets from previous screen Intent
     * @link - https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityParkingDetailsBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        fun.checkIfSignUserAvailable(this);

        detailParkingObj = (Parking) getIntent().getSerializableExtra("Parking");
        //added assert as it was showing in suggestion
        assert detailParkingObj != null;
        fun.logCatD(TAG,detailParkingObj.toString());
        setDetails();

        mMapView = findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        this.parkingViewModel = ParkingViewModel.getInstance(this.getApplication());
    }

    //added @NonNull as it was suggest by IDE
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Double lat, lon;
        String carNo;
        lat = detailParkingObj.getLat();
        lon = detailParkingObj.getLong();
        carNo = detailParkingObj.getCarNo().toUpperCase();
        LatLng location = new LatLng(lat, lon);
        MarkerOptions markerOptions = new MarkerOptions().position(location).title(carNo+" is Parked here!");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(location));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 5));
        googleMap.addMarker(markerOptions);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void setDetails(){
        String title = detailParkingObj.getCarNo().toUpperCase()+" Details";
        buildingText = "Parked in the building - "+detailParkingObj.getBuildingNo().toUpperCase();
        addressText = "Parked at the location - "+detailParkingObj.getAddress();
        carText = "Parked car number is - "+detailParkingObj.getCarNo().toUpperCase();
        hoursText = "Parked for hours of - "+detailParkingObj.getHours();
        dateText = "Parked on the date - "+detailParkingObj.getDate();
        hostText = "Parked Suit No is - "+detailParkingObj.getHostNo().toUpperCase();
        this.binding.detailTitle.setText(title);
        this.binding.address.setText(addressText);
        this.binding.building.setText(buildingText);
        this.binding.car.setText(carText);
        this.binding.hours.setText(hoursText);
        this.binding.date.setText(dateText);
        this.binding.suit.setText(hostText);
    }

    private void deleteDetails(){
        this.parkingViewModel.deleteParkingDetails(detailParkingObj);
        fun.toastMessageLong(this,"Deleted record from Firebase");
        Intent goToParkingScreen = new Intent(this,ParkingList.class);
        goToParkingScreen.putExtra("Parking",detailParkingObj);
        goToParkingScreen.putExtra("forDelete",true);
        startActivity(goToParkingScreen);
        /*
        finish() helps us to make sure when the user click on back button
        he wont be navigating back to the screen which is in in-active status
        which will display the detailedParkingObj details of the deleted screen and new obj isn't passed
        If you want to test, comment the below line & check by going backwards from ParkingList after deleting record
         */
        finish();
    }

    private void goToEditParking(Parking obj){
        Intent goToEditParkingScreen = new Intent(getApplicationContext(), AddParking.class);
        goToEditParkingScreen.putExtra("EditParking", obj);
        goToEditParkingScreen.putExtra("forEdit", true);
        startActivity(goToEditParkingScreen);
        finish();
    }

    /**
     * This is to add menu items to the activity
     *
     * @param menu - this will help to return / fetch menu
     * @return - will be true if created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update_details: {
                fun.toastMessageLong(this,"Clicked on update profile");
                //update profile - bug fix, added the functionality
                String currentUser = fun.getCurrentUser(this);
                Intent updateProfileIntent = new Intent(this, UpdateProfileActivity.class);
                updateProfileIntent.putExtra("CurrentUser", currentUser);
                startActivity(updateProfileIntent);
                break;
            }
            case R.id.signOut_details: {
                fun.signOut(this);
                break;
            }
            case R.id.edit_parking:{
                //call edit parking
                goToEditParking(detailParkingObj);
                break;
            }
            case R.id.delete_parking:{
                fun.toastMessageLong(this,"Clicked on Delete");
                deleteDetails();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
