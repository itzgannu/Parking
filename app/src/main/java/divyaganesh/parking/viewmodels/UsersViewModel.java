package divyaganesh.parking.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import divyaganesh.parking.FirebaseData.FirestoreDB;
import divyaganesh.parking.model.Account;
import divyaganesh.parking.model.Login;

public class UsersViewModel extends AndroidViewModel {

    private final FirestoreDB dbRepo = new FirestoreDB();
    private static UsersViewModel instance;
    public MutableLiveData<List<Login>> login;
    public MutableLiveData<Account> account;

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

    public void getExistingUsers(){
        this.dbRepo.getExistingUsers();
    }

    public void addUser(Account ac){
        this.dbRepo.addUser(ac);
    }

    public void createLogin(Account ac){
        this.dbRepo.createLogin(ac);
    }

    /***** this func is the viewmodel for checkifemail exists while create account ******/
//    public String checkEmail(String email){
//        String emailFetched = this.dbRepo.checkEmail(email);
//       this.account = this.dbRepo.accountLive;
//        return emailFetched;
//    }
}
