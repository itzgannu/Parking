package divyaganesh.parking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import divyaganesh.parking.databinding.ActivityUpdateProfileBinding;
import divyaganesh.parking.helpers.RecursiveMethods;
import divyaganesh.parking.model.Account;
import divyaganesh.parking.viewmodels.ParkingViewModel;
import divyaganesh.parking.viewmodels.UsersViewModel;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class UpdateProfileActivity extends AppCompatActivity {

    ActivityUpdateProfileBinding binding;

    private Account matchedAccount;
    private UsersViewModel usersViewModel;
    private ParkingViewModel parkingViewModel;

    String currentUser;

    //to select image
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    RecursiveMethods method = new RecursiveMethods();
    private final String TAG = this.getClass().getCanonicalName();

    ImageButton imageButton1, imageButton2, imageButton3, imageButton4, imageButton5, imageButton6;
    Button save, cancel;
    int image = 0;
    int selectedImage = 0;
    int dbSetImage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.currentUser = method.getCurrentUser(this);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.teal_700));
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));

        this.usersViewModel = UsersViewModel.getInstance(this.getApplication());
        this.parkingViewModel = ParkingViewModel.getInstance(this.getApplication());

        this.searchAccountAndSetValues();

        this.binding.updateProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialogPopUpView();
            }
        });

        this.binding.updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValuesAndSave(matchedAccount);
            }
        });
    }

    private void getValuesAndSave(Account account){
        String contact, name, password, email, car;
        contact = this.binding.updateContactField.getText().toString();
        name = this.binding.updateNameField.getText().toString();
        password = this.binding.updatePwdField.getText().toString();
        if(validFields()){
            matchedAccount.setContactNo(contact);
            matchedAccount.setName(name);
            matchedAccount.setPassword(password);
            matchedAccount.setImage(dbSetImage);
            if(method.checkInternet(getApplicationContext())){
                //update in db on click
                this.usersViewModel.updateProfile(matchedAccount);
                //close activity
                finish();
            }else{
                method.toastMessageLong(getApplicationContext(),"NO INTERNET CONNECTION");
            }
        } else{
            method.toastMessageShort(this, "First enter values in field & then try updating!");
        }
    }

    private boolean validFields(){
        boolean isValid = true;
        String contact, name, password ;
        contact = this.binding.updateContactField.getText().toString();
        name = this.binding.updateNameField.getText().toString();
        password = this.binding.updatePwdField.getText().toString();
        if(name.isEmpty()){
            this.binding.updateNameField.setError("Field is empty");
            isValid = false;
        }
        if(contact.isEmpty()){
            this.binding.updateContactField.setError("Field is empty");
            isValid = false;
        }
        if(password.isEmpty()){
            this.binding.updatePwdField.setError("Field is empty");
            isValid = false;
        }
        return isValid;
    }

    public void searchAccountAndSetValues(){
        String email = this.currentUser;
        Account account = this.usersViewModel.findThisProfile(email);
        this.matchedAccount = account;
        binding.updateContactField.setText(account.getContactNo());
        binding.updateNameField.setText(account.getName());
        binding.updatePwdField.setText(account.getPassword());
        setImage(account.getImage());
        String email_carNo = "You won't be able to modify email & licence no!"+
                "\n"+ "Your email is - "+account.getEmail()+
                "\n"+ "Your car number is - "+account.getCarNo();
        binding.updateEmailCarnoLabel.setText(email_carNo);
        method.logCatD(TAG, "Loaded data on to fields successfully!");
    }

    /**
     * DIALOG POPUP
     */
    public void createDialogPopUpView(){
        image = 0; selectedImage = 0; dbSetImage = 0;
        dialogBuilder = new AlertDialog.Builder(this);
        final View popUpView = getLayoutInflater().inflate(R.layout.image_selector, null);
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
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image = 0;
                dialog.dismiss();
            }
        });
    }

    private void setImage(int i){
        if(i == 0 || i == 1){
            this.binding.updateProfileImage.setImageResource(R.drawable.profile1);
        }else if(i == 2){
            this.binding.updateProfileImage.setImageResource(R.drawable.profile2);
        }else if(i == 3){
            this.binding.updateProfileImage.setImageResource(R.drawable.profile3);
        }else if(i == 4){
            this.binding.updateProfileImage.setImageResource(R.drawable.profile4);
        }else if(i == 5){
            this.binding.updateProfileImage.setImageResource(R.drawable.profile5);
        }else if(i == 6){
            this.binding.updateProfileImage.setImageResource(R.drawable.profile6);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update_delete: {
                //as suggested by prof jigisha
                String email = matchedAccount.getEmail();
                this.parkingViewModel.deleteParkingLinkedToEmail(email);
                method.toastMessageLong(this,"Clicked on update profile");
                this.usersViewModel.deleteProfile(matchedAccount);
                method.signOut(this);
                break;
            }
            case R.id.update_signOut: {
                method.signOut(this);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}