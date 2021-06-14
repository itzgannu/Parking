package divyaganesh.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import divyaganesh.parking.databinding.ActivityCreateAccountBinding;
import divyaganesh.parking.helpers.RecursiveMethods;
import divyaganesh.parking.model.Account;
import divyaganesh.parking.viewmodels.UsersViewModel;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener {
    private ActivityCreateAccountBinding binding;

    private Account account;
    private UsersViewModel user;

    RecursiveMethods method = new RecursiveMethods();
    private final String TAG = this.getClass().getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        View view = this.binding.getRoot();
        setContentView(view);

        this.user = UsersViewModel.getInstance(this.getApplication());
        this.binding.createAccBtn.setOnClickListener(this);

        this.account = new Account();
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.createAccBtn: {
                    /**
                     * Check Internet
                     */
                    if (method.checkInternet(getApplicationContext())) {
                        /**
                         * Logic to check if email already exist in firebase
                         */
                        String email = this.binding.createAccEmailField.getText().toString();
                        /**
                         * If using user & account collections, use the below line of code
                         * boolean check = this.user.checkUser(email);
                         */
                        boolean check = this.user.checkIfEmailAlreadyExistInProfileCollection(email);
                        if (check) {
                            this.binding.createAccEmailField.setError("Email already exists");
                            break;
                        }
                        /**
                         * Logic to check if car no already exist in firebase
                         */
                        String carNum = this.binding.createAccLicenceField.getText().toString();
                        boolean carCheck = this.user.checkIfCarNoAlreadyExistInProfileCollection(carNum);
                        if(carCheck){
                            this.binding.createAccLicenceField.setError("Number already exists");
                            break;
                        }
                        /**
                         * Field validations check
                         */
                        if (validateFields()) {
                            method.logCatD(TAG, "onClick: Save button clicked");
                            this.saveProfileToDB();
                            this.clearTextEntries();
                            method.toastMessageLong(getApplicationContext(), "Created profile onClick successfully!");
                            method.goToSignInScreen(getApplicationContext());
                            this.finish();
                        }
                    }
                    else {
                        method.toastMessageLong(getApplicationContext(), "NO INTERNET CONNECTION");
                    }
                    break;
                }
            }
        }
    }

    private void clearTextEntries() {
        this.binding.createAccNameField.getText().clear();
        this.binding.createAccEmailField.getText().clear();
        this.binding.createAccPasswordField.getText().clear();
        this.binding.createAccContactField.getText().clear();
        this.binding.createAccLicenceField.getText().clear();
    }

    private Boolean validateFields() {
        boolean isValid = true;
        if (this.binding.createAccNameField.getText().toString().isEmpty()) {
            this.binding.createAccNameField.setError("Field empty - Enter name");
            isValid = false;
        }
        if (this.binding.createAccEmailField.getText().toString().isEmpty()) {
            this.binding.createAccEmailField.setError("Field empty - Enter email");
            isValid = false;
        }
        if (this.binding.createAccPasswordField.getText().toString().isEmpty()) {
            this.binding.createAccPasswordField.setError("Field empty - Enter password");
            isValid = false;
        }
        if (this.binding.createAccContactField.getText().toString().isEmpty()) {
            this.binding.createAccContactField.setError("Field empty - Enter contact number");
            isValid = false;
        }
        if (this.binding.createAccLicenceField.getText().toString().isEmpty()) {
            this.binding.createAccLicenceField.setError("Field empty - Enter License Plate number");
            isValid = false;
        }
        return isValid;
    }

    private void saveProfileToDB() {
        this.account.setName(this.binding.createAccNameField.getText().toString());
        this.account.setEmail(this.binding.createAccEmailField.getText().toString());
        this.account.setPassword(this.binding.createAccPasswordField.getText().toString());
        this.account.setContactNo(this.binding.createAccContactField.getText().toString());
        this.account.setCarNo(this.binding.createAccLicenceField.getText().toString());
        this.account.setId(this.account.getEmail());
        this.user.isProfileAdded(this.account);
    }


    /**
     * DATABASE Version 1.0 Functions
     */

    private void saveToDB() {
        this.account.setName(this.binding.createAccNameField.getText().toString());
        this.account.setEmail(this.binding.createAccEmailField.getText().toString());
        this.account.setPassword(this.binding.createAccPasswordField.getText().toString());
        this.account.setContactNo(this.binding.createAccContactField.getText().toString());
        this.account.setCarNo(this.binding.createAccLicenceField.getText().toString());

        method.logCatD(TAG, "saveToDB: " + this.account);
        this.user.addUser(this.account);
    }

    private void createLogin() {
        this.account.setEmail(this.binding.createAccEmailField.getText().toString());
        this.account.setPassword(this.binding.createAccPasswordField.getText().toString());
        this.user.createLogin(this.account);
    }

}
