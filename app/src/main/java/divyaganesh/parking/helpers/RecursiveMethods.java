package divyaganesh.parking.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import divyaganesh.parking.MainActivity;
import divyaganesh.parking.UpdateProfileActivity;

public class RecursiveMethods {
    //Shared Preferences
    SharedPreferences sharedPreferences;
    static final String preferenceName = "parking";
    static final String currentUser = "userEmail";

    public void setCurrentUser(Context context, String email){
        sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(currentUser, email);
        editor.commit();
    }
    public String getCurrentUser(Context context){
        sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(currentUser)){
            return sharedPreferences.getString(currentUser,"");
        }else{
            return "";
        }
    }
    public void logCatD(String className, String message){
        Log.d(className, message);
    }
    public void logCatE(String className, String message){
        Log.e(className, message);
    }
    public void toastMessageShort(Context context, String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }
    public void toastMessageLong(Context context, String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }
    public void signOut(Context context){
        Intent goToLoginScreen = new Intent(context, MainActivity.class);
        setCurrentUser(context,"");
        context.startActivity(goToLoginScreen);
        toastMessageLong(context,"Signed out!");
    }

    public void checkIfSignUserAvailable(Context context){
        if(getCurrentUser(context).contentEquals("") || getCurrentUser(context).isEmpty()){
            Intent goToLoginScreen = new Intent(context, MainActivity.class);
            context.startActivity(goToLoginScreen);
            toastMessageLong(context,"Signed out!");
        }
    }

    public void goToUpdateProfileScreen(Context context){
        Intent goToUpdateProfile = new Intent(context, UpdateProfileActivity.class);
        String currentUser = getCurrentUser(context);
        goToUpdateProfile.putExtra("currentUser", currentUser);
        context.startActivity(goToUpdateProfile);
    }

    public void goToSignInScreen(Context context){
        Intent goToSignInScreen = new Intent(context,MainActivity.class);
        setCurrentUser(context,"");
        context.startActivity(goToSignInScreen);
    }

    public boolean checkInternet(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
