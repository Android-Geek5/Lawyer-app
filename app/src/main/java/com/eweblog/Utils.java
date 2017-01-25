package com.eweblog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

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
}
