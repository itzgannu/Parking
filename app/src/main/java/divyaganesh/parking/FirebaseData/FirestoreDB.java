package divyaganesh.parking.FirebaseData;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import divyaganesh.parking.model.Login;
import divyaganesh.parking.model.Parking;

public class FirestoreDB {

    private final FirebaseFirestore db;
    private final String COLLECTION_PARKING = "Parking Details";
    private final String COLLECTION_LOGIN = "Login Details";
    private final String COLLECTION_ACCOUNT = "Account Details";
    public MutableLiveData<List<Login>> login = new MutableLiveData<List<Login>>();

    /*
    Log Cat
    */
    private final String TAG = this.getClass().getCanonicalName();
    private void logCat(String message){
        Log.d(TAG,message);
    }
    private void logCatE(String message){
        Log.e(TAG,message);
    }


    public FirestoreDB(){
        db = FirebaseFirestore.getInstance();
    }

    public void getExistingUsers(){
        try{
            db.collection(COLLECTION_LOGIN)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(error != null){
                                logCatE("Failed with some error");
                                return;
                            }
                            List<Login> loginList = new ArrayList<>();

                            if(value.isEmpty()){
                                logCatE("No documents found in the collection");
                            }else{
                                for(DocumentChange docChange : value.getDocumentChanges()){
                                    Login loginDetails = docChange.getDocument().toObject(Login.class);
                                    loginDetails.setEmail(docChange.getDocument().getString("Email"));
                                    loginDetails.setPassword(docChange.getDocument().getString("Password"));

                                    Log.d(TAG, "onEvent: Email : " +loginDetails.getEmail().toString());
                                    Log.d(TAG, "onEvent: Password : " + loginDetails.getPassword().toString());

                                }
                            }
                        }
                    });

        }catch(Exception e){
            logCatE("Unable to fetch data");
        }

    }
}
