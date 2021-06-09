package divyaganesh.parking.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

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
}
