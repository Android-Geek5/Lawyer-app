package com.eweblog;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.eweblog.common.UserInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by erginus on 1/24/2017.
 */

public class Utils {
     // Common method to show toast in the App
    public static void showToast(Context context, String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
    public static void hideKeyboard(Context context,View view)
    {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public static void showKeyboard(Context context,View view)
    {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        }
    }
    public static void hideSoftKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
    // Clear all the data in shared preferences
    public static void clearData(Context context)
    {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit();
    }
    // All the preferences saved are either String or boolean.Store String preferences here
    public static void storeUserPreferences(Context context,String key,String value)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.commit();
    }
    // All the preferences saved are either String or boolean.Store boolean preferences here
    public static void storeUserPreferencesBoolean(Context context,String key,boolean value)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(key,value);
        editor.commit();
    }
    // All the preferences saved are either String or boolean.Get String preferences here
    public static String getUserPreferences(Context context,String key)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String string = prefs.getString(key, null);
        return string;
    }
    // All the preferences saved are either String or boolean.Get boolean preferences here
    public static boolean getUserPreferencesBoolean(Context context,String key)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean bool = prefs.getBoolean(key,false);
        return bool;
    }

    public static boolean isValidPhone(String pass) {
        return pass != null && pass.length() == 10;
    }

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isValidPass(String pass) {
        return pass != null && pass.length() >= 6;
    }
 /// This method is used to get sms alert status.If corporate plan is 2,4 or 6,then sms alert is active
    public static void checkSmsAlert(Context context,int corporatePlansId)
    {
        if(corporatePlansId==2 || corporatePlansId==4 || corporatePlansId==6)
            Utils.storeUserPreferencesBoolean(context, UserInfo.SMS_ALERT,true);
        else
            Utils.storeUserPreferencesBoolean(context, UserInfo.SMS_ALERT,false);
    }
   // There are four different type of users on the basis of group id and parent id,They are set here
    public static void saveTypeOfUser(Context context,int groupId,int parentId)
    {
        // First,make all type of users false
        Utils.storeUserPreferencesBoolean(context, UserInfo.CORPORATE_OR_NOT,false);
        Utils.storeUserPreferencesBoolean(context, UserInfo.COMMON_PAID,false);
        Utils.storeUserPreferencesBoolean(context, UserInfo.FREE_OR_PAID,false);
        Utils.storeUserPreferencesBoolean(context, UserInfo.CHILD_USER_OR_NOT,false);
        if(groupId==5)  // This is for coporate user
        {
            Utils.storeUserPreferencesBoolean(context, UserInfo.CORPORATE_OR_NOT,true);
            Utils.storeUserPreferencesBoolean(context, UserInfo.COMMON_PAID,true);
        }
        else if(groupId==4)   // This is for Individual paid user
        {
            Utils.storeUserPreferencesBoolean(context, UserInfo.FREE_OR_PAID, true);
            Utils.storeUserPreferencesBoolean(context, UserInfo.COMMON_PAID,true);
            if(parentId!=0)   // This is for Individual paid child user
            {
                Utils.storeUserPreferencesBoolean(context, UserInfo.CHILD_USER_OR_NOT,true);
            }
        }
        else   // This is for free user
        {
            Utils.storeUserPreferencesBoolean(context, UserInfo.FREE_OR_PAID,false);
        }

    }
       // Get app version of the app
       public static String GetAppVersion(Context context){
           try {
               PackageInfo _info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
               return _info.versionName;
           } catch (PackageManager.NameNotFoundException e) {
               e.printStackTrace();
               return "";
           }
       }


}
