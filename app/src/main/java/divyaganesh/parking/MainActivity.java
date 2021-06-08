package divyaganesh.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import divyaganesh.parking.FirebaseData.FirestoreDB;
import divyaganesh.parking.model.Login;
import divyaganesh.parking.viewmodels.UsersViewModel;

public class MainActivity extends AppCompatActivity {
    private UsersViewModel usersViewModel;
    private FirestoreDB firestoreDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.usersViewModel = UsersViewModel.getInstance(this.getApplication());
    }

    public void OnClick(View view){
        if(view != null){
            switch (view.getId()){
                case R.id.signIn :
//                    this.firestoreDB.getExistingUsers();
            }
        }
    }
}