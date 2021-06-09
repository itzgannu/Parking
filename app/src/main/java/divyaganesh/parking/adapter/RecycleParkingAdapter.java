package divyaganesh.parking.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import divyaganesh.parking.ParkingDetails;
import divyaganesh.parking.R;
import divyaganesh.parking.model.Parking;

/*
class will extend to RecycleView.Adapter & we pass the ViewHolder in it
 */

public class RecycleParkingAdapter extends RecyclerView.Adapter<RecycleParkingAdapter.MyViewHolder>{
    /*
    Log Cat
     */
    private final String TAG  = this.getClass().getCanonicalName();
    private void logCat(String message){
        Log.e(TAG,message);
    }
    /*
    Creating list of Parking class variables
     */
    List<Parking> recycleParkingList = new ArrayList<>();
    Context context;

    public RecycleParkingAdapter(Context context, List<Parking> parkingList) {
        this.context = context;
        this.recycleParkingList = parkingList;
    }

    @NonNull
    @Override
    public RecycleParkingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*
        Create a view with the layout which we created for the card view & return the view
        */
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_parking,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleParkingAdapter.MyViewHolder holder, int position) {
        /*
          Here we will bind the data to each card view
          so we need to create instances for the parked CarNo & date
          let us not use other items inside Parking class
          We will later on set this in MyViewHolder
          */
        String parkedCarNo = recycleParkingList.get(position).getCarNo();
        String parkedDate = recycleParkingList.get(position).getDate();
        holder.assignValues(parkedCarNo,parkedDate);
        holder.detailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToDetailScreen = new Intent(context, ParkingDetails.class);
                goToDetailScreen.putExtra("Parking",recycleParkingList.get(position));
                context.startActivity(goToDetailScreen);
            }
        });
    }

    @Override
    public int getItemCount() {
        /*
        This will return number of items or card view we need to display on screen
        */
        logCat("Size-"+recycleParkingList.size());
        return recycleParkingList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        /*
        MyViewHolder is the one which we iterate for each item present in the list
         */

        /*
        First we need to get the id's & assign
         */
        private TextView carNumberTextView, parkedDateTextView;

        ConstraintLayout detailsLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            /*
            Now we will assign above one's with the id's which we given in layout for this itemView
             */
            carNumberTextView = itemView.findViewById(R.id.listTitle);
            parkedDateTextView = itemView.findViewById(R.id.listDate);
            detailsLayout = itemView.findViewById(R.id.detailsLayout);
        }

        public void assignValues(String parkedCarNo, String parkedDate) {
            /*
            we are passing values from Main view
            parkedCarNo & parkedDate are set of each individual item (or) at item level (or) for each item, we can say
             */
            carNumberTextView.setText(parkedCarNo);
            String parkedD = "Parked on -"+parkedDate;
            parkedDateTextView.setText(parkedD);
            logCat("Set Values");
        }
    }
}
