package com.eweblog;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

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

    public void clearData(Context context)
    {

    }

    public static void storeUserPreferences(Context context,String key,String value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences("USER_PREFERENCES", MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void storeUserPreferencesBoolean(Context context,String key,boolean value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences("USER_PREFERENCES", MODE_PRIVATE).edit();
        editor.putBoolean(key,value);
        editor.commit();
    }

    public static String getUserPreferences(Context context,String key)
    {
        SharedPreferences prefs = context.getSharedPreferences("USER_PREFERENCES", MODE_PRIVATE);
        String string = prefs.getString(key, null);
        return string;
    }

    public static boolean getUserPreferencesBoolean(Context context,String key)
    {
        SharedPreferences prefs = context.getSharedPreferences("USER_PREFERENCES", MODE_PRIVATE);
        Boolean bool = prefs.getBoolean(key,false);
        return bool;
    }

}
