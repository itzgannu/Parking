package divyaganesh.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import divyaganesh.parking.FirebaseData.FirestoreDB;
import divyaganesh.parking.databinding.ActivityMainBinding;
import divyaganesh.parking.model.Login;
import divyaganesh.parking.viewmodels.UsersViewModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityMainBinding binding;
    private UsersViewModel usersViewModel;
    private final String TAG = this.getClass().getCanonicalName();
    List<Login> loginList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.usersViewModel = UsersViewModel.getInstance(this.getApplication());
        this.binding.signInCreateAccBtn.setOnClickListener(this);
        this.binding.signInBtn.setOnClickListener(this);
    }

    public void onClick(View view){
        if(view != null){
            /*
             * Local variable to perform few checks
             */
            int userExist = 0;
            int noOfUsers = 0;
            boolean success = false;
            String currentUserId = "";
            switch (view.getId()){
                case R.id.signInBtn: {
                    /*
                     * Check if email field is empty
                     */
                    if(this.binding.signInEmailField.getText().toString().isEmpty()){
                        toastMessage("Enter User Name");
                        break;
                    }
                    /*
                     * Call view model functions to retrieve all existing users
                     * Store List of array of view model class into local loginList
                     */
                    this.usersViewModel.getExistingUsers();
                    this.loginList = this.usersViewModel.loginArrayList;
                    noOfUsers = this.loginList.size();
                    Log.d(TAG, "onClick: Login List populated to check with - "+this.loginList.toString());
                    for(Login login:this.loginList){
                       /*
                       Check if user exist in the database or not
                        */
                        if(login.getEmail().contentEquals(this.binding.signInEmailField.getText().toString())){
                            /*
                             * Check if password field is empty
                             */
                            if(this.binding.signInPwdField.getText().toString().isEmpty()){
                                toastMessage("Enter Password");
                                break;
                            }
                           /*
                           If the user exist, then password matched with the user or not?
                            */
                            if(login.getPassword().contentEquals(this.binding.signInPwdField.getText().toString())){
                               /*
                               If combination of username & password is correct, navigate to next screen
                                */
                                success = true;
                                currentUserId = login.getId();
                                Log.d(TAG, "onClick: currentUserId - "+ currentUserId);
                            } else{
                               /*
                               Show toast message to user that they entered wrong password
                                */
                                toastMessage("Incorrect Password");
                                return;
                            }
                        } else{
                            userExist = userExist+1;
                        }
                    }
                    if(success){
                        toastMessage("Login Success");
                        Intent parkingIntent = new Intent(getApplicationContext(),ParkingList.class);
                        startActivity(parkingIntent);
                        break;
                    }
                    /*
                     * If username doesn't exist in the firebase, inform user to create an account first
                     */
                    if(userExist == noOfUsers){
                        toastMessage("User doesn't exist, Kindly create account!");
                        break;
                    }
                    break;
                }
                case R.id.signInCreateAccBtn:{
                    Log.d(TAG, "onClick: create clicked to redirect");
                    Intent insertIntent = new Intent(this, CreateAccount.class);
                    startActivity(insertIntent);
                    break;
                }
            }
        }
    }

    private void toastMessage(String message){
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
