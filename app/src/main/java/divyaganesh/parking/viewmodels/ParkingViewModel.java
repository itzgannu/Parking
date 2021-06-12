package divyaganesh.parking.viewmodels;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import divyaganesh.parking.FirebaseData.FirestoreDB;
import divyaganesh.parking.helpers.RecursiveMethods;
import divyaganesh.parking.model.Parking;

public class ParkingViewModel extends AndroidViewModel {
    //recursive object
    RecursiveMethods fun = new RecursiveMethods();
    //database Variables
    private final FirestoreDB db = new FirestoreDB();
    //instance Variable
    private static ParkingViewModel instance;
    //mutable Live Data
    public MutableLiveData<List<Parking>> viewModelLiveData;

    public static ParkingViewModel getInstance(Application application) {
        if(instance == null){
            instance = new ParkingViewModel(application);
        }
        return instance;
    }

    //Constructor which is created automatically because of extending AndroidViewModel to the current class
    public ParkingViewModel(@NonNull Application application) {
        super(application);
        this.viewModelLiveData = this.db.parkingLiveData;
    }

    public List<Parking> getALlParkingDetails(){
        return this.db.getAllParkingDetails();
    }

    public void addParkingDetails(Parking p){
        this.db.addParkingDetail(p);
    }

    public void deleteParkingDetails(Parking p){
        this.db.deleteParkingDetail(p);
    }
}
