package divyaganesh.parking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import divyaganesh.parking.databinding.ActivityUpdateProfileBinding;
import divyaganesh.parking.helpers.RecursiveMethods;
import divyaganesh.parking.model.Account;
import divyaganesh.parking.model.Login;
import divyaganesh.parking.viewmodels.UsersViewModel;

import android.os.Bundle;
import android.view.View;

public class UpdateProfileActivity extends AppCompatActivity {

    ActivityUpdateProfileBinding binding;

    private Account matchedAccount;
    private Login changeLogin;
    private UsersViewModel usersViewModel;

    String currentUser;

    RecursiveMethods method = new RecursiveMethods();
    private final String TAG = this.getClass().getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        //get current account details with shared preference email id
        this.currentUser = method.getCurrentUser(this);

        //setValues(currentAccount);
//        this.matchedAccount = new Account();
        this.usersViewModel = UsersViewModel.getInstance(this.getApplication());

        this.searchAccount();

        this.binding.updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValues(matchedAccount);
            }
        });
    }

//    private void setValues(Account account){
//
//    }

    private void getValues(Account account){
        String contact, name, password, email, car;
        contact = this.binding.updateContactField.getText().toString();
        name = this.binding.updateNameField.getText().toString();
        password = this.binding.updatePwdField.getText().toString();
        email = this.matchedAccount.getEmail();
        car = this.matchedAccount.getCarNo();
        //update in db on click
        if(password.contentEquals(this.matchedAccount.getPassword())){
            //don't update password - no call to login details db
            this.matchedAccount.setName(this.binding.updateNameField.getText().toString());
            this.matchedAccount.setContactNo(this.binding.updateContactField.getText().toString());

            this.usersViewModel.updateUser(matchedAccount);
        }else{
            //else call to both db's
            this.matchedAccount.setName(this.binding.updateNameField.getText().toString());
            this.matchedAccount.setContactNo(this.binding.updateContactField.getText().toString());
            this.matchedAccount.setPassword(this.binding.updatePwdField.getText().toString());
//            this.changeLogin.setPassword(this.binding.updatePwdField.getText().toString());

            this.usersViewModel.updateUser(matchedAccount);
//            this.usersViewModel.updateLogin(changeLogin);
        }
        //after saving to db, finish()
    }

    public void searchAccount(){
        String email = this.currentUser;
        this.usersViewModel.searchUser(email);
        this.usersViewModel.matchedAccount.observe(this, new Observer<Account>() {
            @Override
            public void onChanged(Account account) {
                if(account != null){
                    binding.updateContactField.setText(account.getContactNo());
                    binding.updateNameField.setText(account.getName());
                    binding.updatePwdField.setText(account.getPassword());
                    String email_carNo = "You won't be able to modify email & licence no!"+
                            "\n"+ "Your email is - "+account.getEmail()+
                            "\n"+ "Your car number is - "+account.getCarNo();
                    binding.updateEmailCarnoLabel.setText(email_carNo);
                    method.logCatD(TAG, "Loaded data on to fields successfully!");
                    matchedAccount = account;

                }
            }
        });
    }

}