package divyaganesh.parking.FirebaseData;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import divyaganesh.parking.model.Account;
import divyaganesh.parking.model.Login;

public class FirestoreDB {

    private final FirebaseFirestore db;
    private final String COLLECTION_PARKING = "Parking Details";
    private final String COLLECTION_LOGIN = "Login Details";
    private final String COLLECTION_ACCOUNT = "Account Details";
    public MutableLiveData<List<Login>> login = new MutableLiveData<List<Login>>();
    public MutableLiveData<Account> accountLive = new MutableLiveData<>();
    int count;
    String emailDB;

    List<Login> loginArrayList = new ArrayList<>();

    /*
    Log Cat
    */
    private final String TAG = this.getClass().getCanonicalName();

    private void logCat(String message) {
        Log.d(TAG, message);
    }

    private void logCatE(String message) {
        Log.e(TAG, message);
    }


    public FirestoreDB() {
        db = FirebaseFirestore.getInstance();
    }

    public void getExistingUsers() {
        try {
            db.collection(COLLECTION_LOGIN)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                logCatE("Failed with some error");
                                return;
                            }

                            if (value.isEmpty()) {
                                logCatE("No documents found in the collection");
                            } else {
                                for (DocumentChange docChange : value.getDocumentChanges()) {
                                    Login loginDetails = docChange.getDocument().toObject(Login.class);
                                    loginDetails.setId(docChange.getDocument().getId());
                                    Log.d(TAG, "onEvent: LoginDetails - "+loginDetails);
                                    loginArrayList.add(loginDetails);
                                }
                            }
                        }
                    });
            login.postValue(loginArrayList);

        } catch (Exception e) {
            logCatE("Unable to fetch data");
        }

    }

    public void addUser(Account ac) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("Name", ac.getName());
            data.put("Email", ac.getEmail());
            data.put("Password", ac.getPassword());
            data.put("ContactNo", ac.getContactNo());
            data.put("CarNo", ac.getCarNo());

            db.collection(COLLECTION_ACCOUNT).add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            logCat("Data added successfully");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            logCatE(e.getLocalizedMessage());
                        }
                    });

        } catch (Exception e) {
            logCatE(e.getLocalizedMessage());
        }
    }

    public void createLogin(Account ac) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("Email", ac.getEmail());
            data.put("Password", ac.getPassword());

            db.collection(COLLECTION_LOGIN).add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            logCat("Login Created successfully");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            logCatE(e.getLocalizedMessage());
                        }
                    });
        } catch (Exception e) {
            logCatE(e.getLocalizedMessage());
        }
    }


    /********* check if email already exists while creating account *********/

//    public String checkEmail(String emailToCheck) {
//        try{
//            db.collection(COLLECTION_ACCOUNT).whereEqualTo("Email",emailToCheck).get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if(task.isSuccessful()){
//                                if(task.getResult().getDocuments().size() !=0){
//                                    Account acc = task.getResult().getDocuments().get(0).toObject(Account.class);
//                                    acc.setEmail(task.getResult().getDocuments().get(0).getString("Email"));
//
//                                    String emailFromDB = acc.getEmail().toString();
//                                    Log.d(TAG, "onComplete: emailfromdb: " +emailFromDB);
//
//                                    if(emailFromDB != null){
//                                        emailDB = emailFromDB;
//                                    }
//                                }else{
//                                    logCatE("User does not exist");
//                                    emailDB = null;
//                                }
//                            }
//                        }
//                    });
//        }catch (Exception e){
//            logCatE(e.getLocalizedMessage());
//        }
//        return emailDB;
//    }
}