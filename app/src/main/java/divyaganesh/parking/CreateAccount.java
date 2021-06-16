package divyaganesh.parking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import divyaganesh.parking.databinding.ActivityCreateAccountBinding;
import divyaganesh.parking.databinding.ImageSelectorBinding;
import divyaganesh.parking.helpers.RecursiveMethods;
import divyaganesh.parking.model.Account;
import divyaganesh.parking.viewmodels.UsersViewModel;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener {
    private ActivityCreateAccountBinding binding;

    private Account account;
    private UsersViewModel user;

    RecursiveMethods method = new RecursiveMethods();
    private final String TAG = this.getClass().getCanonicalName();

    //to select image
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    ImageButton imageButton1, imageButton2, imageButton3, imageButton4, imageButton5, imageButton6;
    Button save, cancel;
    int image = 0;
    int selectedImage = 0;
    int dbSetImage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        View view = this.binding.getRoot();
        setContentView(view);

        image = 0; selectedImage = 0; dbSetImage = 0;

        this.user = UsersViewModel.getInstance(this.getApplication());
        this.binding.createAccBtn.setOnClickListener(this);
        this.binding.createAccProfilePictImg.setOnClickListener(this);

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
                         * Field validations check
                         */
                        if (validateFields()) {
                            /**
                             * Logic to check if car no already exist in firebase
                             */
                            String carNum = this.binding.createAccLicenceField.getText().toString();
                            boolean carCheck = this.user.checkIfCarNoAlreadyExistInProfileCollection(carNum);
                            if(carCheck){
                                this.binding.createAccLicenceField.setError("Number already exists");
                                break;
                            }
                            method.logCatD(TAG, "onClick: Save button clicked");
                            this.saveProfileToDB();
                            this.clearTextEntries();
                            method.toastMessageLong(getApplicationContext(), "Created profile onClick successfully!");
                            method.goToSignInScreen(this);
                            finish();
                        }
                    }
                    else {
                        method.toastMessageLong(getApplicationContext(), "NO INTERNET CONNECTION");
                    }
                    break;
                }
                case R.id.createAccProfilePictImg:{
                    createDialogPopUpView();
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
        if(!checkLicenceNo(this.binding.createAccLicenceField.getText().toString()) || this.binding.createAccLicenceField.getText().toString().isEmpty()){
            this.binding.createAccLicenceField.setError("Licence - should be min 2, max 8 alphanum characters");
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

    private boolean checkLicenceNo(String licence){
        String licenceRegex = "^\\w{2,8}$";
        return licence != null && licence.matches(licenceRegex);
    }

    /**
     * DIALOG POPUP
     */
    public void createDialogPopUpView(){
        image = 0; selectedImage = 0; dbSetImage = 0;
        dialogBuilder = new AlertDialog.Builder(this);
        final View popUpView = getLayoutInflater().inflate(R.layout.image_selector, null);
//        ImageSelectorBinding selectorBinding = ImageSelectorBinding.inflate(getLayoutInflater());
        imageButton1 = (ImageButton) popUpView.findViewById(R.id.imageButton1);
        imageButton2 = (ImageButton) popUpView.findViewById(R.id.imageButton2);
        imageButton3 = (ImageButton) popUpView.findViewById(R.id.imageButton3);
        imageButton4 = (ImageButton) popUpView.findViewById(R.id.imageButton4);
        imageButton5 = (ImageButton) popUpView.findViewById(R.id.imageButton5);
        imageButton6 = (ImageButton) popUpView.findViewById(R.id.imageButton6);

        save = (Button) popUpView.findViewById(R.id.selectImage);
        cancel = (Button) popUpView.findViewById(R.id.cancelImage);

        dialogBuilder.setView(popUpView);
        dialog = dialogBuilder.create();
        dialog.show();
        
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image = 0;
                dbSetImage = image;
                dialog.dismiss();
            }
        });

        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image = 1;
            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image = 2;
            }
        });
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image = 3;
            }
        });
        imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image = 4;
            }
        });
        imageButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image = 5;
            }
        });
        imageButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image = 6;
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(image == 0 ){
                    method.toastMessageShort(getApplicationContext(), "Click on any Image to select");
                }else{
                    selectedImage = image;
                    dbSetImage = image;
                    method.toastMessageShort(getApplicationContext(), "Selected image - "+selectedImage);
                    dialog.dismiss();
                    setImage(image);
                }
            }
        });
    }

    private void setImage(int i){
        if(i == 0 || i == 1){
            this.binding.createAccProfilePictImg.setImageResource(R.drawable.profile1);
        }else if(i == 2){
            this.binding.createAccProfilePictImg.setImageResource(R.drawable.profile2);
        }else if(i == 3){
            this.binding.createAccProfilePictImg.setImageResource(R.drawable.profile3);
        }else if(i == 4){
            this.binding.createAccProfilePictImg.setImageResource(R.drawable.profile4);
        }else if(i == 5){
            this.binding.createAccProfilePictImg.setImageResource(R.drawable.profile5);
        }else if(i == 6){
            this.binding.createAccProfilePictImg.setImageResource(R.drawable.profile6);
        }
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
