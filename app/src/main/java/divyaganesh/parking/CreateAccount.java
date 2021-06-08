package divyaganesh.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import divyaganesh.parking.FirebaseData.FirestoreDB;
import divyaganesh.parking.databinding.ActivityCreateAccountBinding;
import divyaganesh.parking.model.Account;
import divyaganesh.parking.viewmodels.UsersViewModel;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener {
    private ActivityCreateAccountBinding binding;

    private Account account;
    private UsersViewModel user;
    private FirestoreDB db;
    private final String TAG = this.getClass().getCanonicalName();
    int val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_account);

        this.binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        View view = this.binding.getRoot();
        setContentView(view);

        this.user = UsersViewModel.getInstance(this.getApplication());
        this.binding.createAccBtn.setOnClickListener(this);

        this.account = new Account();

    }


    @Override
    public void onClick(View v) {
        if(v != null){
            switch (v.getId()){
                case R.id.createAccBtn:{
                    if(validateFields()) {

                        Log.d(TAG, "onClick: Save button clicked");
//                        val = ifEmailExists();
//                        if(val ==1) {
//                            Toast.makeText(this, "User already exists. Please use another email", Toast.LENGTH_LONG).show();
//                        }
//                        else {
                            this.saveToDB();
                            this.createLogin();
                            this.clearTextEntries();
                            Toast.makeText(this, "User created successfully. Please login", Toast.LENGTH_LONG).show();
                            Intent insertIntent = new Intent(this, MainActivity.class);
                            startActivity(insertIntent);
//                        }
                    }
                    break;
                }
            }
        }

    }

    private Boolean validateFields(){
        Boolean isValid = true;
        if(this.binding.createAccNameField.getText().toString().isEmpty()){
            this.binding.createAccNameField.setError("Field empty - Enter name");
            isValid = false;
        }
        if(this.binding.createAccEmailField.getText().toString().isEmpty()){
            this.binding.createAccEmailField.setError("Field empty - Enter email");
            isValid = false;
        }
        if(this.binding.createAccPasswordField.getText().toString().isEmpty()){
            this.binding.createAccPasswordField.setError("Field empty - Enter password");
            isValid = false;
        }
        if(this.binding.createAccContactField.getText().toString().isEmpty()){
            this.binding.createAccContactField.setError("Field empty - Enter contact number");
            isValid = false;
        }
        if(this.binding.createAccLicenceField.getText().toString().isEmpty()){
            this.binding.createAccLicenceField.setError("Field empty - Enter License Plate number");
            isValid = false;
        }
        return isValid;
    }

    private void saveToDB(){
        this.account.setName(this.binding.createAccNameField.getText().toString());
        this.account.setEmail(this.binding.createAccEmailField.getText().toString());
        this.account.setPassword(this.binding.createAccPasswordField.getText().toString());
        this.account.setContactNo(this.binding.createAccContactField.getText().toString());
        this.account.setCarNo(this.binding.createAccLicenceField.getText().toString());

        Log.d(TAG, "saveToDB: " + this.account);
        this.user.addUser(this.account);
    }

    private void createLogin(){
        this.account.setEmail(this.binding.createAccEmailField.getText().toString());
        this.account.setPassword(this.binding.createAccPasswordField.getText().toString());

        this.user.createLogin(this.account);
    }

    private void clearTextEntries(){
        this.binding.createAccNameField.getText().clear();
        this.binding.createAccEmailField.getText().clear();
        this.binding.createAccPasswordField.getText().clear();
        this.binding.createAccContactField.getText().clear();
        this.binding.createAccLicenceField.getText().clear();
    }

/******** this function checks if the email/user exists already in the Account Details DB **********/

//    private int ifEmailExists() {
//        String email = this.binding.createAccEmailField.getText().toString();
//        int val =0;
//
//        String em = this.user.checkEmail(email);
//        Log.d(TAG, "ifEmailExists: email " +em);
//        if(em == null){
//            val = 0;
//        }
//        else if(em != null && em.equalsIgnoreCase(email)){
//            val = 1;
//        }
//        return val;
//    }
}
