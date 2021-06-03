package divyaganesh.parking.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import divyaganesh.parking.FirebaseData.FirestoreDB;
import divyaganesh.parking.model.Login;

public class UsersViewModel extends AndroidViewModel {

    private final FirestoreDB dbRepo = new FirestoreDB();
    private static UsersViewModel instance;
    public MutableLiveData<List<Login>> login;

    public static UsersViewModel getInstance(Application application){
        if(instance == null){
            instance = new UsersViewModel(application);
        }
        return instance;
    }

    public UsersViewModel(Application application){
        super(application);
        this.dbRepo.getExistingUsers();
//        this.login = this.dbRepo.login;
    }
}
