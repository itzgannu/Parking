package divyaganesh.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.Observer;
import divyaganesh.parking.databinding.ActivityMainBinding;
import divyaganesh.parking.helpers.RecursiveMethods;
import divyaganesh.parking.model.Account;
import divyaganesh.parking.viewmodels.UsersViewModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityMainBinding binding;

    private UsersViewModel usersViewModel;
    List<Account> profileList = new ArrayList<>();

    RecursiveMethods fun = new RecursiveMethods();
    private final String TAG = this.getClass().getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        userLoggedIn();

        getWindow().setNavigationBarColor(getResources().getColor(R.color.teal_700));
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));

        this.usersViewModel = UsersViewModel.getInstance(this.getApplication());
        profileList.clear();
        this.usersViewModel.getAllProfiles();
        this.usersViewModel.viewModelProfileLiveData.observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(List<Account> accounts) {
                profileList = accounts;
            }
        });
        this.binding.signInCreateAccBtn.setOnClickListener(this);
        this.binding.signInBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        profileList.clear();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        userLoggedIn();

        this.usersViewModel = UsersViewModel.getInstance(this.getApplication());
        this.usersViewModel.getAllProfiles();
        this.usersViewModel.viewModelProfileLiveData.observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(List<Account> accounts) {
                        profileList = accounts;
            }
        });

        this.binding.signInCreateAccBtn.setOnClickListener(this);
        this.binding.signInBtn.setOnClickListener(this);
    }

    //to check if user already logged in or not
    protected void userLoggedIn(){
        if(fun.getCurrentUser(this).contentEquals("")){
            //do nothing
            fun.logCatD(TAG, "No user logged in");
        }else{
            //go to parking screen
            fun.logCatD(TAG, "User logged in");
            Intent parkingIntent = new Intent(getApplicationContext(),ParkingList.class);
            startActivity(parkingIntent);
            finish();
        }
    }

    public void onClick(View view){
        if(view != null){
            int userExist = 0;
            int noOfUsers = 0;
            boolean success = false;
            String currentUserId = "";
            String currentUserEmail = "";
            switch (view.getId()){
                case R.id.signInBtn: {
                    if(this.binding.signInEmailField.getText().toString().isEmpty()){
                        this.binding.signInEmailField.setError("Enter Username");
                        break;
                    }
                    this.usersViewModel.getAllProfiles();
                    this.profileList = this.usersViewModel.viewModelProfileList;
                    noOfUsers = this.profileList.size();

                    for(Account account:this.profileList){
                        fun.logCatD(TAG,"onClick: Login List populated to check with - "+account.toString());
                        String email, pass;
                        email = account.getEmail();
                        pass = account.getPassword();
                        String enteredEmail, enteredPass;
                        enteredEmail = this.binding.signInEmailField.getText().toString();
                        enteredPass = this.binding.signInPwdField.getText().toString();
                        fun.logCatD(TAG, "Email is -- "+email);
                        fun.logCatD(TAG, "Password is -- "+pass);
                        fun.logCatD(TAG, "Entered Email is -- "+enteredEmail);
                        fun.logCatD(TAG, "Entered Password is -- "+enteredPass);
                        if(email.contentEquals(enteredEmail)){
                            if(enteredPass.isEmpty()){
                                this.binding.signInPwdField.setError("Enter Password");
                                break;
                            }
                            if(pass.contentEquals(enteredPass)){
                                success = true;
                                currentUserId = account.getId();
                                fun.logCatD(TAG, "onClick: currentUserId - "+ currentUserId);
                                break;
                            } else{
                                this.binding.signInPwdField.setError("Incorrect Password");
                                return;
                            }
                        } else{
                            userExist = userExist+1;
                        }
                    }
                    if(success){
                        fun.toastMessageLong(this,"Login Success");
                        currentUserEmail = this.binding.signInEmailField.getText().toString();
                        Intent parkingIntent = new Intent(getApplicationContext(),ParkingList.class);
                        fun.setCurrentUser(this,currentUserEmail);
                        startActivity(parkingIntent);
                         /*
                        finish() helps us to make sure when the user click on back button
                        he wont be navigating back to the screen which is in in-active status
                        If you want to test, comment the below line & check by going backwards from ParkingList after loggin in
                         */
                        finish();
                        break;
                    }
                    /*
                     * If username doesn't exist in the firebase, inform user to create an account first
                     */
                    if(userExist == noOfUsers){
                        this.binding.signInEmailField.setError("User doesn't exist. Kindly create account");
                        break;
                    }
                    break;
                }
                case R.id.signInCreateAccBtn:{
                    fun.logCatD(TAG, "onClick: Clicked on 'Create Account' & now redirecting");
                    Intent insertIntent = new Intent(this, CreateAccount.class);
                    startActivity(insertIntent);
                    break;
                }
            }
        }
    }
}
