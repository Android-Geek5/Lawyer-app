package com.eweblog;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by erginus on 1/24/2017.
 */

public class Utils {

    public static void showToast(Context context, String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
