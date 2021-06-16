package divyaganesh.parking.helpers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.Locale;

public class LocationHelper {

    public boolean locationPermissionGranted = false;
    private LocationRequest locationRequest;
    public final int request_code_location = 101;
    private FusedLocationProviderClient fusedLocationProviderClient = null;
    MutableLiveData<Location> locationFetch = new MutableLiveData<>();
    RecursiveMethods fun = new RecursiveMethods();

    private static final LocationHelper ourInstance = new LocationHelper();
    public static  LocationHelper getInstance(){
        return  ourInstance;
    }

    private LocationHelper(){
        this.locationRequest = new LocationRequest();
        this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //indicates the accuracy
        this.locationRequest.setInterval(300000);
    }

    public void checkPermissions(Context context){
        this.locationPermissionGranted = (ContextCompat.checkSelfPermission(context.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        fun.logCatD("LocationHelper","LocationPermissionGranted"+this.locationPermissionGranted);

        if(!this.locationPermissionGranted){
            requestLocationPermission(context);
        }
    }

    public void requestLocationPermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, this.request_code_location);
    }

    public FusedLocationProviderClient getFusedLocationProviderClient(Context context){
        if(this.fusedLocationProviderClient == null){
            this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        }
        return this.fusedLocationProviderClient;
    }

    @SuppressLint("MissingPermission")
    public MutableLiveData<Location> getLocation(Context context){
        if(this.locationPermissionGranted){
            try{
                this.getFusedLocationProviderClient(context).getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if(location != null){
                                    locationFetch.setValue(location);
                                    fun.logCatD("LocationHelper","Last Location received : Lat :" +locationFetch.getValue().getLatitude()+"Long : " +locationFetch.getValue().getLongitude());
                                }else{
                                    fun.logCatD("LocationHelper","Else");
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                fun.logCatE("LocationHelper","Could not get the last location");
                            }
                        });
            }catch(Exception e){
                fun.logCatE("LocationHelper",e.getLocalizedMessage());
                return null;
            }
            return this.locationFetch;

        }else{
            fun.logCatE("LocationHelper","App does not have access permission for location");
            return null;
        }
    }

    public String getCurrentAddress(Context context, Location loc){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addressList;

        try{
            addressList = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 2);

            String address = addressList.get(0).getAddressLine(0);
            fun.logCatD("LocationHelper","Address: "+address);
            return address;
        }catch(Exception e){
            fun.logCatE("LocationHelper","Address not fetched");
        }
        return null;
    }
}
