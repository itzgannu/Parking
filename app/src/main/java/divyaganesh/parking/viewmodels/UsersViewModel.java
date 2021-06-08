package divyaganesh.parking.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import divyaganesh.parking.FirebaseData.FirestoreDB;
import divyaganesh.parking.model.Account;
import divyaganesh.parking.model.Login;

public class UsersViewModel extends AndroidViewModel {

    private final FirestoreDB dbRepo = new FirestoreDB();
    private static UsersViewModel instance;
    public MutableLiveData<List<Login>> login;
    public MutableLiveData<Account> account;
    /**
     * Store Login MutableLiveData into List of Login model class
     */
    public List<Login> loginArrayList;

    public static UsersViewModel getInstance(Application application) {
        if (instance == null) {
            instance = new UsersViewModel(application);
        }
        return instance;
    }

    public UsersViewModel(Application application) {
        super(application);
        this.dbRepo.getExistingUsers();
    }

    public void getExistingUsers() {
        /**
         * Get all the existing users from repository
         * Set the local MutableLiveData with view Model class
         * Set the local List array by converting MutableLiveData
         */
        this.dbRepo.getExistingUsers();
        this.login = this.dbRepo.login;
        this.loginArrayList = this.login.getValue();
    }

    public void addUser(Account ac) {
        this.dbRepo.addUser(ac);
    }

    public void createLogin(Account ac) {
        this.dbRepo.createLogin(ac);
    }

    public boolean checkUser(String name) {
        /**
         * Function to check if user already exist in firebase database or not
         */
        boolean check = false;
        this.dbRepo.getExistingUsers();
        this.login = this.dbRepo.login;
        this.loginArrayList = this.login.getValue();
        assert this.loginArrayList != null;
        for (Login login : this.loginArrayList) {
            if (login.getEmail().equalsIgnoreCase(name)) {
                check = true;
                return check;
            }
        }
        return check;
    }
}
