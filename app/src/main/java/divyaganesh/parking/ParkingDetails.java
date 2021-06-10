package divyaganesh.parking;

import androidx.appcompat.app.AppCompatActivity;
import divyaganesh.parking.helpers.RecursiveMethods;
import divyaganesh.parking.model.Parking;

import android.os.Bundle;

import java.io.Serializable;

public class ParkingDetails extends AppCompatActivity implements Serializable {

    Parking detailParkingObj;
    RecursiveMethods fun = new RecursiveMethods();

    /**
     * current Parking Details gets from previous screen Intent
     * @link - https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_details);

        detailParkingObj = (Parking) getIntent().getSerializableExtra("Parking");
        fun.logCatD("-------------",detailParkingObj.toString());
    }
}