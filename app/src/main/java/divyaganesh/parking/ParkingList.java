package divyaganesh.parking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import divyaganesh.parking.adapter.RecycleParkingAdapter;
import divyaganesh.parking.databinding.ActivityParkingListBinding;
import divyaganesh.parking.helpers.RecursiveMethods;
import divyaganesh.parking.model.Parking;
import divyaganesh.parking.viewmodels.ParkingViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ParkingList extends AppCompatActivity {
    /*
    Creating variable for recycler view related things
     */
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecycleParkingAdapter recycleParkingAdapter;
    String currentUser = "";

    //Importing viewModel class
    ParkingViewModel parkingViewModel;
    List<Parking> parkingList = new ArrayList<>();

    //Recursive Method function call
    RecursiveMethods fun = new RecursiveMethods();
    private final String TAG = this.getClass().getCanonicalName();

    ActivityParkingListBinding binding;

    //To show progress bar on start of activity
    @Override
    protected void onStart() {
        super.onStart();
        this.binding.progressbar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityParkingListBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        this.binding.progressbar.setVisibility(View.VISIBLE);

        if (fun.getCurrentUser(this).contentEquals("")) {
            fun.toastMessageLong(this, "Something went wrong. Kindly login again!");
            Intent goToLoginScreen = new Intent(this, MainActivity.class);
            startActivity(goToLoginScreen);
        } else {
            currentUser = fun.getCurrentUser(this);
        }

        this.parkingViewModel = ParkingViewModel.getInstance(this.getApplication());
        this.parkingViewModel.viewModelLiveData.observe(this, new Observer<List<Parking>>() {
            @Override
            public void onChanged(List<Parking> parking) {
                List<Parking> emailParking = parking;
                if (parking != null) {
                    for (Parking park : parking) {
                        fun.logCatD(TAG, "Parking Details fetched in activity screen - " + park.toString());
                    }
                }
                for (int i = 0; i < emailParking.size(); i++) {
                    //filter as per the currentUser
                    if (emailParking.get(i).getEmail().contentEquals(currentUser)) {

                    } else {
                        emailParking.remove(i);
                        i--;
                    }
                    binding.progressbar.setVisibility(View.GONE);
                }
                fun.logCatD(TAG, "Parking Details w.r.t to email are - " + emailParking.size());
                //Load data into recycler View
                initializeRecyclerView(emailParking);
            }
        });
        //comment below line to check the progress bar visibility (forever)
        parkingList = this.parkingViewModel.getALlParkingDetails();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.parkingViewModel = ParkingViewModel.getInstance(this.getApplication());
        this.parkingViewModel.viewModelLiveData.observe(this, new Observer<List<Parking>>() {
            @Override
            public void onChanged(List<Parking> parking) {
                List<Parking> emailParking = parking;
                if (parking != null) {
                    for (Parking park : parking) {
                        fun.logCatD(TAG, "Parking Details fetched in activity screen - " + park.toString());
                    }
                }
                for (int i = 0; i < emailParking.size(); i++) {
                    //filter as per the currentUser
                    if (emailParking.get(i).getEmail().contentEquals(currentUser)) {

                    } else {
                        emailParking.remove(i);
                        i--;
                    }
                    binding.progressbar.setVisibility(View.GONE);
                }
                fun.logCatD(TAG, "Parking Details w.r.t to email are - " + emailParking.size());
                //Load data into recycler View
                initializeRecyclerView(emailParking);
            }
        });
        //comment below line to check the progress bar visibility (forever)
        parkingList = this.parkingViewModel.getALlParkingDetails();
    }

    /*
    Initialize recycler view
    */
    private void initializeRecyclerView(List<Parking> park) {
        recyclerView = this.binding.recyclerViews;
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recycleParkingAdapter = new RecycleParkingAdapter(this, park);
        recyclerView.setAdapter(recycleParkingAdapter);
    }

    /**
     * This is to add menu items to the activity
     *
     * @param menu - this will help to return / fetch menu
     * @return - will be true if created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.parking_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addParking: {
                Log.d(TAG, "onOptionsItemSelected: Add Parking clicked");
                Intent addParkingIntent = new Intent(this, AddParking.class);
                addParkingIntent.putExtra("CurrentUser", currentUser);
                startActivity(addParkingIntent);
                break;
            }
            case R.id.update_parking: {
                Log.d(TAG, "onOptionsItemSelected: Update Profile clicked");
                break;
            }
            case R.id.signOut_parking: {
                Log.d(TAG, "onOptionsItemSelected: Sign out clicked");
                //need to implement sign out Intent
                fun.setCurrentUser(this,"");
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}