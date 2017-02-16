package com.eweblog;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.eweblog.common.Prefshelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by erginus on 1/24/2017.
 */

public class Utils {

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

    public static void clearData(Context context)
    {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit();
    }

    public static void storeUserPreferences(Context context,String key,String value)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void storeUserPreferencesBoolean(Context context,String key,boolean value)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(key,value);
        editor.commit();
    }

    public static String getUserPreferences(Context context,String key)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String string = prefs.getString(key, null);
        return string;
    }

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

    public static void checkSmsAlert(Context context,int corporatePlansId)
    {
        if(corporatePlansId==2 || corporatePlansId==4 || corporatePlansId==6)
            Utils.storeUserPreferencesBoolean(context, Prefshelper.SMS_ALERT,true);
        else
            Utils.storeUserPreferencesBoolean(context, Prefshelper.SMS_ALERT,false);
    }

    public static void saveTypeOfUser(Context context,int groupId)
    {
        if(groupId==5)
        {Utils.storeUserPreferencesBoolean(context,Prefshelper.CORPORATE_OR_NOT,true);
            Utils.storeUserPreferencesBoolean(context,Prefshelper.COMMON_PAID,true);}
        if(groupId==4)
        { Utils.storeUserPreferencesBoolean(context, Prefshelper.FREE_OR_PAID, true);
            Utils.storeUserPreferencesBoolean(context,Prefshelper.COMMON_PAID,true);}
        else Utils.storeUserPreferencesBoolean(context, Prefshelper.FREE_OR_PAID,false);

    }

       /* COMMENTED FOR NOW
                                        JSONArray servicesArray=jsonObject.getJSONArray("services_array");
                                        if(servicesArray.length()>0)
                                        {
                                            for(int i=0;i<servicesArray.length();i++)
                                            {
                                                JSONObject jsonObject1=servicesArray.getJSONObject(i);
                                                String name=jsonObject1.getString("service_name");
                                                if(name.equalsIgnoreCase("Sms Alert"))
                                                {
                                                    int status=jsonObject1.getInt("service_status");
                                                    if(status==1)
                                                        Utils.storeUserPreferencesBoolean(SplashScreenActivity.this,Prefshelper.SMS_ALERT,true);
                                                    else Utils.storeUserPreferencesBoolean(SplashScreenActivity.this,Prefshelper.SMS_ALERT,false);
                                                }
                                                if(name.equalsIgnoreCase("Fee Management"))
                                                {
                                                    int status=jsonObject1.getInt("service_status");
                                                    if(status==1)
                                                        Utils.storeUserPreferencesBoolean(SplashScreenActivity.this,Prefshelper.FEE_MANAGEMENT,true);
                                                    else Utils.storeUserPreferencesBoolean(SplashScreenActivity.this,Prefshelper.FEE_MANAGEMENT,false);
                                                }
                                            }
                                        }*/
}
