package divyaganesh.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import divyaganesh.parking.FirebaseData.FirestoreDB;
import divyaganesh.parking.databinding.ActivityMainBinding;
import divyaganesh.parking.viewmodels.UsersViewModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityMainBinding binding;
    private UsersViewModel usersViewModel;
    private FirestoreDB firestoreDB;
    private final String TAG = this.getClass().getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.usersViewModel = UsersViewModel.getInstance(this.getApplication());
        this.binding.signInCreateAccBtn.setOnClickListener(this);
        this.binding.signInBtn.setOnClickListener(this);
    }

    public void onClick(View view){
        if(view != null){
            switch (view.getId()){
                case R.id.signInBtn: {
//                    this.firestoreDB.getExistingUsers();
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
}