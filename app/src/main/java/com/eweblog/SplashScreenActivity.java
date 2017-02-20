package com.eweblog;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
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
import com.eweblog.common.AlarmReceiver;
import com.eweblog.common.ConnectionDetector;
import com.eweblog.common.MapAppConstant;
import com.eweblog.common.Prefshelper;
import com.eweblog.common.VolleySingleton;
import com.eweblog.model.CaseListModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {
    ConnectionDetector cd;
    Prefshelper prefshelper;
    private static int SPLASH_TIME_OUT = 2000;
    String userID, userSecHash, userName, userEmail, userContact, userEmailVerified, userMobileVerified,
            userStatus, imgUrl,userLastName,userStateOfPractice,userCityofPractice,userSpecialization;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    Calendar cal = Calendar.getInstance();
    Date sysDate = cal.getTime();
    List<CaseListModel> caseList ;
    int groupId;
    JSONObject jsonObject;
    int corporatePlansId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        prefshelper = new Prefshelper(SplashScreenActivity.this);

        caseList = prefshelper.getList();
        if(caseList!=null) {
            if (caseList.size() > 0) {
                for (int i = 0; i < caseList.size(); i++)
                {
                    if (caseList.get(i).getDate().equalsIgnoreCase(dateFormat.format(sysDate)))
                    {

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.MINUTE, 59);
                        calendar.set(Calendar.HOUR,5);
                        calendar.set(Calendar.AM_PM, Calendar.PM);
                        Intent myIntent = new Intent(this, AlarmReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);
                        AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager2.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                AlarmManager.INTERVAL_DAY, pendingIntent);

                    }
                }
            }

        }
        cd = new ConnectionDetector(getApplicationContext());

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (cd.isConnectingToInternet()) {

                        if (((Utils.getUserPreferences(SplashScreenActivity.this,Prefshelper.USER_ID)==null || Utils.getUserPreferences(SplashScreenActivity.this,Prefshelper.USER_ID).equalsIgnoreCase(""))) ||
                                ((Utils.getUserPreferences(SplashScreenActivity.this,Prefshelper.USER_SECURITY_HASH)==null || Utils.getUserPreferences(SplashScreenActivity.this,Prefshelper.USER_SECURITY_HASH).equalsIgnoreCase(""))))
                        {
                            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }
                        else if (!(Utils.getUserPreferences(SplashScreenActivity.this,Prefshelper.USER_ID).equalsIgnoreCase("")) &&
                                !(Utils.getUserPreferences(SplashScreenActivity.this,Prefshelper.USER_SECURITY_HASH).equalsIgnoreCase(""))
                        && !(Utils.getUserPreferencesBoolean(SplashScreenActivity.this,Prefshelper.USER_MOBILE_VERIFICATION_STATUS)))
                        {
                            Intent intent = new Intent(SplashScreenActivity.this, OTPScreenActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else if (!(Utils.getUserPreferences(SplashScreenActivity.this,Prefshelper.USER_ID).equalsIgnoreCase("")) || !(Utils.getUserPreferences(SplashScreenActivity.this,Prefshelper.USER_SECURITY_HASH).equalsIgnoreCase("")) &&
                                (Utils.getUserPreferencesBoolean(SplashScreenActivity.this,Prefshelper.USER_MOBILE_VERIFICATION_STATUS)))
                        {
                            sessionLogin();
                        }
                    } else {

                        if (!(Utils.getUserPreferences(SplashScreenActivity.this,Prefshelper.USER_ID).equalsIgnoreCase("")) && !(Utils.getUserPreferences(SplashScreenActivity.this,Prefshelper.USER_SECURITY_HASH).equalsIgnoreCase("")) &&
                                (Utils.getUserPreferencesBoolean(SplashScreenActivity.this,Prefshelper.USER_MOBILE_VERIFICATION_STATUS)))
                        {
                            if(groupId==4)
                                Utils.storeUserPreferencesBoolean(SplashScreenActivity.this,Prefshelper.FREE_OR_PAID,true);
                            else
                                Utils.storeUserPreferencesBoolean(SplashScreenActivity.this,Prefshelper.FREE_OR_PAID,false);
                             Intent intent=new Intent(SplashScreenActivity.this,MainAcitivity.class);
                            startActivity(intent);
                            finish();
                          /*  if(Utils.getUserPreferencesBoolean(SplashScreenActivity.this,Prefshelper.CORPORATE_OR_NOT))
                            {
                                Intent intent = new Intent(SplashScreenActivity.this, CorporateUserMainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {

                                Intent intent = new Intent(SplashScreenActivity.this, MainAcitivity.class);
                                startActivity(intent);
                                finish();
                            }*/
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
    @Override
    public void onRestart()
    {
        super.onRestart();

    }
    @Override
    public void onStop()
    {
        super.onStop();
    }
    @Override
    public void onResume() {
        super.onResume();

    }
    public void sessionLogin() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(SplashScreenActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

            Log.e("SessionLogin URL",MapAppConstant.API + "session_login");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "session_login", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("SessionLogin Response" ,response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Utils.showToast(SplashScreenActivity.this, serverMessage);

                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {
                                if ("1".equals(serverCode)) {
                                    jsonObject=object.getJSONObject("data");
                                     userID=jsonObject.getString("user_id");
                                     userSecHash=jsonObject.getString("user_security_hash");
                                     userName=jsonObject.getString("user_name");
                                     userEmail=jsonObject.getString("user_email");
                                     userContact=jsonObject.getString("user_contact");
                                     userEmailVerified=jsonObject.getString("user_email_verification_status");
                                     userMobileVerified=jsonObject.getString("user_mobile_verification_status");
                                     userStatus=jsonObject.getString("user_status");
                                     userStateOfPractice=jsonObject.getString(Prefshelper.USER_STATE_OF_PRACTISE);
                                      userCityofPractice=jsonObject.getString(Prefshelper.USER_CITY_OF_PRACTISE);
                                      userLastName=jsonObject.getString(Prefshelper.USER_LAST_NAME);
                                      groupId=jsonObject.getInt("group_id");
                                      imgUrl = jsonObject.getString("user_profile_image");
                                    if(groupId==4 || groupId==5) //Check if paid or business user
                                    corporatePlansId=jsonObject.getInt(Prefshelper.CORPORATE_PLANS_ID);
                                    userSpecialization=jsonObject.getString(Prefshelper.USER_SPECIALIZATION);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Utils.saveTypeOfUser(SplashScreenActivity.this,groupId);
                            Utils.checkSmsAlert(SplashScreenActivity.this,corporatePlansId);

                            Utils.storeUserPreferences(SplashScreenActivity.this,Prefshelper.USER_ID,userID);
                            Utils.storeUserPreferences(SplashScreenActivity.this,Prefshelper.USER_SECURITY_HASH,userSecHash);
                            Utils.storeUserPreferences(SplashScreenActivity.this,Prefshelper.USER_EMAIL,userEmail);
                            Utils.storeUserPreferences(SplashScreenActivity.this,Prefshelper.USER_NAME,userName);
                            Utils.storeUserPreferences(SplashScreenActivity.this,Prefshelper.USER_CONTACT,userContact);
                            Utils.storeUserPreferences(SplashScreenActivity.this,Prefshelper.USER_STATUS,userStatus);
                            Utils.storeUserPreferences(SplashScreenActivity.this,Prefshelper.USER_PROFILE_IMAGE_URL,imgUrl);
                            Utils.storeUserPreferences(SplashScreenActivity.this,Prefshelper.USER_LAST_NAME,userLastName);
                            Utils.storeUserPreferences(SplashScreenActivity.this,Prefshelper.USER_STATE_OF_PRACTISE,userStateOfPractice);
                            Utils.storeUserPreferences(SplashScreenActivity.this,Prefshelper.USER_CITY_OF_PRACTISE,userCityofPractice);
                            Utils.storeUserPreferences(SplashScreenActivity.this,Prefshelper.USER_SPECIALIZATION,userSpecialization);
                            if(userEmailVerified.equalsIgnoreCase("1"))
                                Utils.storeUserPreferencesBoolean(SplashScreenActivity.this,Prefshelper.USER_EMAIL_VERIFICATION_STATUS,true);
                            else
                                Utils.storeUserPreferencesBoolean(SplashScreenActivity.this,Prefshelper.USER_EMAIL_VERIFICATION_STATUS,false);
                            if(userMobileVerified.equalsIgnoreCase("1"))
                                Utils.storeUserPreferencesBoolean(SplashScreenActivity.this,Prefshelper.USER_MOBILE_VERIFICATION_STATUS,true);
                            else
                                Utils.storeUserPreferencesBoolean(SplashScreenActivity.this,Prefshelper.USER_MOBILE_VERIFICATION_STATUS,false);
                            Intent intent = new Intent(SplashScreenActivity.this, MainAcitivity.class);
                            startActivity(intent);
                            finish();
                        }

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
                        Toast.makeText(SplashScreenActivity.this, "No Internet Connection",
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

                    params.put("user_id", Utils.getUserPreferences(SplashScreenActivity.this,Prefshelper.USER_ID));
                    params.put("user_security_hash", Utils.getUserPreferences(SplashScreenActivity.this,Prefshelper.USER_SECURITY_HASH));
                    Log.e("SessionLogin Request" ,params.toString());
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
