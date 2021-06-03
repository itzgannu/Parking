package divyaganesh.parking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import divyaganesh.parking.adapter.RecycleParkingAdapter;
import divyaganesh.parking.model.Parking;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ParkingList extends AppCompatActivity {
    /*
    Creating variable for recycler view related things
     */
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<Parking> parkingList;
    RecycleParkingAdapter recycleParkingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_list);

        /*
        Calling setListItems() to Set data of each first
         */
        setListItems();
        /*
        Calling recycler view initializer to show up in the list
         */
        initializeRecyclerView();
    }

    /*
    Initialize recycler view
     */
    private void initializeRecyclerView(){
        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recycleParkingAdapter = new RecycleParkingAdapter(parkingList);
        recyclerView.setAdapter(recycleParkingAdapter);
        recycleParkingAdapter.notifyDataSetChanged();
    }

    /*
    Set the data which needs to be displayed on our List
     */
    private void setListItems(){
        /*
        Will update this once we had established data base connection & delete below 3 lines
         */
        parkingList = new ArrayList<>();
        parkingList.add(new Parking("","AP39HT1223","28 Jan 2021","","","","",""));
        parkingList.add(new Parking("","AP39HT1223","23 Dec 1993","","","","",""));
    }
}