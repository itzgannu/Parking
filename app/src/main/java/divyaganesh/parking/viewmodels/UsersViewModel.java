package divyaganesh.parking.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import divyaganesh.parking.FirebaseData.FirestoreDB;
import divyaganesh.parking.model.Account;
import divyaganesh.parking.model.Login;

public class UsersViewModel extends AndroidViewModel {
    /**
     * Database initializing
     */
    private final FirestoreDB dbRepo = new FirestoreDB();
    private static UsersViewModel instance;
    public static UsersViewModel getInstance(Application application) {
        if (instance == null) {
            instance = new UsersViewModel(application);
        }
        return instance;
    }
    public UsersViewModel(Application application) {
        super(application);
        this.dbRepo.getAllProfiles();
        this.dbRepo.getAllParkingDetails();
    }

    /**
     * Database version 2.0
     * Create profile variables
     */
    /**
     * Store Profile MutableLiveData into List of Profile model class
     */
    public MutableLiveData<List<Account>> viewModelProfileLiveData;
    public List<Account> viewModelProfileList;

    /**
     * New database collection functions
     */
    /**
     * CREATE
     * @param createThisProfile - which profile to be added is set by the passing obj
     * @return - true if added successfully, else false
     */
    public void isProfileAdded(Account createThisProfile){
        this.dbRepo.createProfile(createThisProfile);
    }
    /**
     * READ
     */
    public void getAllProfiles(){
        this.dbRepo.getAllProfiles();
        this.viewModelProfileLiveData = this.dbRepo.profileDBLiveData;
        this.viewModelProfileList = this.viewModelProfileLiveData.getValue();
    }
    /**
     * VALIDATION
     * @param email - email will be checked across database available email to tally if user exist or not
     * @return - true if exists, else false
     */
    public boolean checkIfEmailAlreadyExistInProfileCollection(String email){
        boolean check = false;
        this.dbRepo.getAllProfiles();
        this.viewModelProfileLiveData = this.dbRepo.profileDBLiveData;
        this.viewModelProfileList = this.viewModelProfileLiveData.getValue();
        for(Account account : this.viewModelProfileList){
            if(account.getEmail().equalsIgnoreCase(email)){
                check = true;
                return check;
            }
        }
        return check;
    }
    public boolean checkIfCarNoAlreadyExistInProfileCollection(String carNo){
        boolean check = false;
        this.dbRepo.getAllProfiles();
        this.viewModelProfileLiveData = this.dbRepo.profileDBLiveData;
        this.viewModelProfileList = this.viewModelProfileLiveData.getValue();
        for(Account account : this.viewModelProfileList){
            if(account.getCarNo().equalsIgnoreCase(carNo)){
                check = true;
                return check;
            }
        }
        return check;
    }
    public Account findThisProfile(String email){
        Account check = new Account();
        this.dbRepo.getAllProfiles();
        this.viewModelProfileLiveData = this.dbRepo.profileDBLiveData;
        this.viewModelProfileList = this.viewModelProfileLiveData.getValue();
        for(Account account : this.viewModelProfileList){
            if(account.getEmail().equalsIgnoreCase(email)){
                check = account;
                return check;
            }
        }
        return null;
    }
    /**
     * UPDATE
     */
    public void updateProfile(Account updateThisProfile){
        this.dbRepo.updateProfile(updateThisProfile);
    }
    /**
     * DELETE
     */
    public void deleteProfile(Account deleteThisProfile){
        this.dbRepo.deleteProfile(deleteThisProfile);
    }



    /**
     * Database version 1.0
     */
    /**
     * Variables for Login & Account
     */
    public MutableLiveData<List<Login>> login;
    public MutableLiveData<Account> matchedAccount;
    public List<Login> loginArrayList;

    /**
     * Get all the existing users from repository
     * Set the local MutableLiveData with view Model class
     * Set the local List array by converting MutableLiveData
     */
    public void getExistingUsers() {
        this.dbRepo.getExistingUsers();
        this.login = this.dbRepo.loginLiveData;
        this.loginArrayList = this.login.getValue();
    }
    /**
     * CREATE
     */
    public void addUser(Account ac) {
        this.dbRepo.addUser(ac);
    }
    public void createLogin(Account ac) {
        this.dbRepo.createLogin(ac);
    }
    /**
     * UPDATE
     */
    public void updateUser(Account ac){
        this.dbRepo.updateUser(ac);
    }
    public void updateLogin(Login login){
        this.dbRepo.updateLogin(login);
    }
    /**
     * Function to check if user already exist in firebase database or not
     */
    public boolean checkUser(String name) {
        boolean check = false;
        this.dbRepo.getExistingUsers();
        this.login = this.dbRepo.loginLiveData;
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
    public void searchUser(String email){
        this.dbRepo.searchUser(email);
        this.matchedAccount = this.dbRepo.accFromDB;
    }

}
