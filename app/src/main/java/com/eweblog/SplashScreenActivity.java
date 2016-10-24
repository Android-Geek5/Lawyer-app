package com.eweblog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.eweblog.common.ConnectionDetector;
import com.eweblog.common.MapAppConstant;
import com.eweblog.common.Prefshelper;
import com.eweblog.common.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {
    ConnectionDetector cd;
    Prefshelper prefshelper;
    private static int SPLASH_TIME_OUT = 2000;
    String userID, userSecHash, userName, userEmail, userContact, userEmailVerified, userMobileVerified, userStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        prefshelper = new Prefshelper(SplashScreenActivity.this);
        cd = new ConnectionDetector(getApplicationContext());

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (cd.isConnectingToInternet()) {

                        if ((prefshelper.getUserIdFromPreference().equals("")) || (prefshelper.getUserSecHashFromPreference().equals(""))) {

                            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }
                        else if ((!(prefshelper.getUserIdFromPreference().equalsIgnoreCase("")) && !(prefshelper.getUserSecHashFromPreference().equalsIgnoreCase("")) &&
                                prefshelper.getMobileVerification().equalsIgnoreCase("0"))) {

                            Intent intent = new Intent(SplashScreenActivity.this, OTPScreenActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            sessionLogin();
                        }
                    } else {

                        if (!(prefshelper.getUserIdFromPreference().equalsIgnoreCase("")) || !(prefshelper.getUserSecHashFromPreference().equalsIgnoreCase("")))
                        {
                            Intent intent = new Intent(SplashScreenActivity.this, SelectDateActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                          dialog();
                        }
                    }
                }

            }, SPLASH_TIME_OUT);
        }
    public void dialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_layout);

        Button yes = (Button) dialog.findViewById(R.id.bt_yes);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void sessionLogin() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(SplashScreenActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "SIGNUP " + MapAppConstant.API + "session_login");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "session_login", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Toast.makeText(SplashScreenActivity.this, serverMessage,Toast.LENGTH_LONG).show();

                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {
                                if ("1".equals(serverCode)) {
                                    JSONObject jsonObject=object.getJSONObject("data");
                                     userID=jsonObject.getString("user_id");
                                     userSecHash=jsonObject.getString("user_security_hash");
                                     userName=jsonObject.getString("user_name");
                                     userEmail=jsonObject.getString("user_email");
                                     userContact=jsonObject.getString("user_contact");
                                     userEmailVerified=jsonObject.getString("user_email_verification_status");
                                     userMobileVerified=jsonObject.getString("user_mobile_verification_status");
                                     userStatus=jsonObject.getString("user_status");


                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                        prefshelper.storeUserIdToPreference(userID);
                        prefshelper.storeSecHashToPreference(userSecHash);
                        prefshelper.storeEmailToPreference(userEmail);
                        prefshelper.storeUserNameToPreference(userName);
                        prefshelper.storeUserContactToPreference(userContact);
                        prefshelper.storeUserStatusToPreference(userStatus);
                        prefshelper.storeEmailVerification(userEmailVerified);
                        prefshelper.storeMobileVerification(userMobileVerified);

                        Intent intent = new Intent(SplashScreenActivity.this, SelectDateActivity.class);
                        startActivity(intent);
                        finish();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(SplashScreenActivity.this, "Timeout Error",
                                Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof ServerError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof NetworkError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof ParseError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    }
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("user_id", prefshelper.getUserIdFromPreference());
                    params.put("user_security_hash", prefshelper.getUserSecHashFromPreference());
                    return params;
                }
            };
            sr.setShouldCache(true);

            sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
