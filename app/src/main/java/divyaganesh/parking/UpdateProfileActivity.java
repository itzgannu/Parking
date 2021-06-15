package divyaganesh.parking;

import androidx.annotation.NonNull;
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

public class UpdateProfileActivity extends AppCompatActivity {

    ActivityUpdateProfileBinding binding;

    private Account matchedAccount;
    private UsersViewModel usersViewModel;
    private ParkingViewModel parkingViewModel;

    String currentUser;

    RecursiveMethods method = new RecursiveMethods();
    private final String TAG = this.getClass().getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.currentUser = method.getCurrentUser(this);

        this.usersViewModel = UsersViewModel.getInstance(this.getApplication());
        this.parkingViewModel = ParkingViewModel.getInstance(this.getApplication());

        this.searchAccountAndSetValues();

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
        matchedAccount.setContactNo(contact);
        matchedAccount.setName(name);
        matchedAccount.setPassword(password);
        if(method.checkInternet(getApplicationContext())){
            //update in db on click
            this.usersViewModel.updateProfile(matchedAccount);
            //close activity
            finish();
        }else{
            method.toastMessageLong(getApplicationContext(),"NO INTERNET CONNECTION");
        }
    }

    public void searchAccountAndSetValues(){
        String email = this.currentUser;
        Account account = this.usersViewModel.findThisProfile(email);
        this.matchedAccount = account;
        binding.updateContactField.setText(account.getContactNo());
        binding.updateNameField.setText(account.getName());
        binding.updatePwdField.setText(account.getPassword());
        String email_carNo = "You won't be able to modify email & licence no!"+
                "\n"+ "Your email is - "+account.getEmail()+
                "\n"+ "Your car number is - "+account.getCarNo();
        binding.updateEmailCarnoLabel.setText(email_carNo);
        method.logCatD(TAG, "Loaded data on to fields successfully!");
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
                method.toastMessageLong(this,"Clicked on update profile");
                this.usersViewModel.deleteProfile(matchedAccount);
                String email = matchedAccount.getEmail();
                this.parkingViewModel.deleteParkingLinkedToEmail(email);
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