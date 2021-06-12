package divyaganesh.parking;

import androidx.appcompat.app.AppCompatActivity;
import divyaganesh.parking.databinding.ActivityUpdateProfileBinding;
import divyaganesh.parking.helpers.RecursiveMethods;
import divyaganesh.parking.model.Account;

import android.os.Bundle;

public class UpdateProfileActivity extends AppCompatActivity {

    ActivityUpdateProfileBinding binding;

    Account currentAccount, newAccount;

    String currentUser;

    RecursiveMethods method = new RecursiveMethods();
    private final String TAG = this.getClass().getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        //get current account details with shared preference email id

        //setValues(currentAccount);
    }

    private void setValues(Account account){
        this.binding.updateContactField.setText(account.getContactNo());
        this.binding.updateNameField.setText(account.getName());
        this.binding.updatePwdField.setText(account.getPassword());
        String email_carNo = "You won't be able to modify email & licence no!"+
                "\n"+ "Your email is - "+account.getEmail()+
                "\n"+ "Your car number is - "+account.getCarNo();
        this.binding.updateEmailCarnoLabel.setText(email_carNo);
        method.logCatD(TAG, "Loaded data on to fields successfully!");
    }

    private void getValues(Account account){
        String contact, name, password, email, car;
        contact = this.binding.updateContactField.getText().toString();
        name = this.binding.updateNameField.getText().toString();
        password = this.binding.updatePwdField.getText().toString();
        email = this.currentAccount.getEmail();
        car = this.currentAccount.getCarNo();
        //update in db on click
        if(password.contentEquals(this.currentAccount.getPassword())){
            //don't update password - no call to login details db
        }else{
            //else call to both db's
        }
        //after saving to db, finish()
    }

}