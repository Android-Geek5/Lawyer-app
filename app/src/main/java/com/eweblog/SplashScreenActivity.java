package com.eweblog;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
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
import com.eweblog.common.UserInfo;
import com.eweblog.common.ConnectionDetector;
import com.eweblog.common.MapAppConstant;
import com.eweblog.common.VolleySingleton;
import com.eweblog.model.CaseList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {
    ConnectionDetector cd;
    private static int SPLASH_TIME_OUT = 2000;
    String userID, userSecHash, userName, userEmail, userContact, userEmailVerified, userMobileVerified,
            userStatus, imgUrl,userLastName,userStateOfPractice,userCityofPractice,userSpecialization
            ,userStateOfPractice2,userCityofPractice2;;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    String versionName,versionResponse,linkDownload;
    List<CaseList> caseList=new ArrayList<>();
    int groupId;
    JSONObject jsonObject;
    int corporatePlansId=0;
    int parentId=0;
    int expiry=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        versionName=Utils.GetAppVersion(this);
        Log.e("Version name",versionName);
        // Cancel alarms
        alarmCancel();
        cd = new ConnectionDetector(getApplicationContext());

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    /** If internet is there**/
                    if (cd.isConnectingToInternet()) {
                       /* If no user id and securtiy hash saved,then show login activity **/
                        if (((Utils.getUserPreferences(SplashScreenActivity.this, UserInfo.USER_ID)==null || Utils.getUserPreferences(SplashScreenActivity.this, UserInfo.USER_ID).equalsIgnoreCase(""))) ||
                                ((Utils.getUserPreferences(SplashScreenActivity.this, UserInfo.USER_SECURITY_HASH)==null || Utils.getUserPreferences(SplashScreenActivity.this, UserInfo.USER_SECURITY_HASH).equalsIgnoreCase(""))))
                        {
                            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }
                        /* If mobile not verfieid,show otp screen **/
                        else if (!(Utils.getUserPreferences(SplashScreenActivity.this, UserInfo.USER_ID).equalsIgnoreCase("")) &&
                                !(Utils.getUserPreferences(SplashScreenActivity.this, UserInfo.USER_SECURITY_HASH).equalsIgnoreCase(""))
                        && !(Utils.getUserPreferencesBoolean(SplashScreenActivity.this, UserInfo.USER_MOBILE_VERIFICATION_STATUS)))
                        {
                            Intent intent = new Intent(SplashScreenActivity.this, OTPScreenActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        /** If mobile confirmed,then do session login and create alarms **/
                        else if (!(Utils.getUserPreferences(SplashScreenActivity.this, UserInfo.USER_ID).equalsIgnoreCase("")) || !(Utils.getUserPreferences(SplashScreenActivity.this, UserInfo.USER_SECURITY_HASH).equalsIgnoreCase("")) &&
                                (Utils.getUserPreferencesBoolean(SplashScreenActivity.this, UserInfo.USER_MOBILE_VERIFICATION_STATUS)))
                        {
                            createAlarm();
                            sessionLogin();
                        }
                    }
                    else {
                        /* If alraedy login and mobile verfied and no internet,then show main screen **/
                        if (!(Utils.getUserPreferences(SplashScreenActivity.this, UserInfo.USER_ID).equalsIgnoreCase("")) && !(Utils.getUserPreferences(SplashScreenActivity.this, UserInfo.USER_SECURITY_HASH).equalsIgnoreCase("")) &&
                                (Utils.getUserPreferencesBoolean(SplashScreenActivity.this, UserInfo.USER_MOBILE_VERIFICATION_STATUS)))
                        {
                            /* NO USE if(groupId==4)
                                Utils.storeUserPreferencesBoolean(SplashScreenActivity.this, UserInfo.FREE_OR_PAID,true);
                            else
                                Utils.storeUserPreferencesBoolean(SplashScreenActivity.this, UserInfo.FREE_OR_PAID,false);**/
                            createAlarm();
                            Intent intent=new Intent(SplashScreenActivity.this,MainAcitivity.class);
                            startActivity(intent);
                            finish();
                        }
                        // No internet and credentials saved
                        else
                        {
                          dialog();
                        }
                    }
                }

            }, SPLASH_TIME_OUT);
        }
    /** Dialog for internet check **/
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
    /** Dialog for version change **/
    public void dialogUpdate(final String url) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.update_layout);

        Button yes = (Button) dialog.findViewById(R.id.bt_yes);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
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
            String SESSION_LOGIN_URL=MapAppConstant.API +MapAppConstant.SESSION_LOGIN;
            Log.e("SessionLogin URL",SESSION_LOGIN_URL);
            StringRequest sr = new StringRequest(Request.Method.POST, SESSION_LOGIN_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("SessionLogin Response" ,response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");


                        if (serverCode.equalsIgnoreCase("0")) {
                            Utils.showToast(SplashScreenActivity.this, serverMessage);
                        }
                        if (serverCode.equalsIgnoreCase("-1")) {
                            Utils.showToast(SplashScreenActivity.this, serverMessage);
                            Utils.clearData(SplashScreenActivity.this);
                            Intent intent=new Intent(SplashScreenActivity.this,LoginActivity.class);
                            startActivity(intent);
                            SplashScreenActivity.this.finish();
                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {
                                if ("1".equals(serverCode)) {
                                    jsonObject=object.getJSONObject("data");
                                     userID=jsonObject.getString(UserInfo.USER_ID);
                                     userSecHash=jsonObject.getString(UserInfo.USER_SECURITY_HASH);
                                     userName=jsonObject.getString(UserInfo.USER_NAME);
                                     userEmail=jsonObject.getString(UserInfo.USER_EMAIL);
                                     userContact=jsonObject.getString(UserInfo.USER_CONTACT);
                                     userEmailVerified=jsonObject.getString(UserInfo.USER_EMAIL_VERIFICATION_STATUS);
                                     userMobileVerified=jsonObject.getString(UserInfo.USER_MOBILE_VERIFICATION_STATUS);
                                     userStatus=jsonObject.getString(UserInfo.USER_STATUS);
                                     userStateOfPractice=jsonObject.getString(UserInfo.USER_STATE_OF_PRACTISE);
                                     userCityofPractice=jsonObject.getString(UserInfo.USER_CITY_OF_PRACTISE);
                                     userStateOfPractice2=jsonObject.getString(UserInfo.USER_STATE_OF_PRACTISE1);
                                     userCityofPractice2=jsonObject.getString(UserInfo.USER_CITY_OF_PRACTISE1);
                                     userLastName=jsonObject.getString(UserInfo.USER_LAST_NAME);
                                     groupId=jsonObject.getInt(UserInfo.GROUP_ID);
                                     parentId=jsonObject.getInt(UserInfo.USER_PARENT_ID);
                                    /** Check if profile image is there,fetch url for the same **/
                                     if(jsonObject.getString(UserInfo.USER_PROFILE_IMAGE).equalsIgnoreCase(""))
                                         imgUrl="";
                                    else
                                         imgUrl = jsonObject.getString(UserInfo.USER_PROFILE_IMAGE_URL);

                                    //Fetch corporate plan id if paid or business user & not a child user
                                    if((groupId==4 || groupId==5)&& parentId==0)
                                    corporatePlansId=jsonObject.getInt(UserInfo.CORPORATE_PLANS_ID);
                                }
                                /** Get version info **/
                                JSONObject configurations=jsonObject.getJSONObject(UserInfo.CONFIGURATIONS);
                                versionResponse=configurations.getString(UserInfo.VERSION);
                                linkDownload=configurations.getString(UserInfo.URL_DOWNLOAD);
                                /** Get expiry **/
                                expiry=jsonObject.getInt(UserInfo.USER_IS_EXPIRED);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            // save type of user
                            Utils.saveTypeOfUser(SplashScreenActivity.this,groupId,parentId);
                            // check sms package or not
                            Utils.checkSmsAlert(SplashScreenActivity.this,corporatePlansId);

                            Utils.storeUserPreferences(SplashScreenActivity.this, UserInfo.USER_ID,userID);
                            Utils.storeUserPreferences(SplashScreenActivity.this, UserInfo.USER_SECURITY_HASH,userSecHash);
                            Utils.storeUserPreferences(SplashScreenActivity.this, UserInfo.USER_EMAIL,userEmail);
                            Utils.storeUserPreferences(SplashScreenActivity.this, UserInfo.USER_NAME,userName);
                            Utils.storeUserPreferences(SplashScreenActivity.this, UserInfo.USER_CONTACT,userContact);
                            Utils.storeUserPreferences(SplashScreenActivity.this, UserInfo.USER_STATUS,userStatus);
                            Utils.storeUserPreferences(SplashScreenActivity.this, UserInfo.USER_PROFILE_IMAGE_URL,imgUrl);
                            Utils.storeUserPreferences(SplashScreenActivity.this, UserInfo.USER_LAST_NAME,userLastName);
                            Utils.storeUserPreferences(SplashScreenActivity.this, UserInfo.USER_STATE_OF_PRACTISE,userStateOfPractice);
                            Utils.storeUserPreferences(SplashScreenActivity.this, UserInfo.USER_CITY_OF_PRACTISE,userCityofPractice);
                            Utils.storeUserPreferences(SplashScreenActivity.this, UserInfo.USER_SPECIALIZATION,userSpecialization);
                            Utils.storeUserPreferences(SplashScreenActivity.this, UserInfo.USER_STATE_OF_PRACTISE1,userStateOfPractice2);
                            Utils.storeUserPreferences(SplashScreenActivity.this, UserInfo.USER_CITY_OF_PRACTISE1,userCityofPractice2);

                            if(userEmailVerified.equalsIgnoreCase("1"))
                                Utils.storeUserPreferencesBoolean(SplashScreenActivity.this, UserInfo.USER_EMAIL_VERIFICATION_STATUS,true);
                            else
                                Utils.storeUserPreferencesBoolean(SplashScreenActivity.this, UserInfo.USER_EMAIL_VERIFICATION_STATUS,false);

                            if(userMobileVerified.equalsIgnoreCase("1"))
                                Utils.storeUserPreferencesBoolean(SplashScreenActivity.this, UserInfo.USER_MOBILE_VERIFICATION_STATUS,true);
                            else
                                Utils.storeUserPreferencesBoolean(SplashScreenActivity.this, UserInfo.USER_MOBILE_VERIFICATION_STATUS,false);

                            if(versionName.equalsIgnoreCase(versionResponse))
                            {
                               // If same version,check expiry
                                if(expiry==0) {
                                    // Not expired
                                    Utils.showToast(SplashScreenActivity.this, serverMessage);
                                    Intent intent = new Intent(SplashScreenActivity.this, MainAcitivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else if(expiry==1)   //about to expire
                                {
                                    dialogExpiry(1,serverMessage);
                                }
                                else if(expiry==2)  //expired
                                {
                                    dialogExpiry(2,serverMessage);
                                }
                            }
                            else{
                               // Different version,show update message
                                dialogUpdate(linkDownload);
                            }

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
                        Utils.showToast(SplashScreenActivity.this, "No Internet Connection");
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

                    params.put(UserInfo.USER_ID, Utils.getUserPreferences(SplashScreenActivity.this, UserInfo.USER_ID));
                    params.put(UserInfo.USER_SECURITY_HASH, Utils.getUserPreferences(SplashScreenActivity.this, UserInfo.USER_SECURITY_HASH));
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
    public void alarmCancel()
    {
        Intent intent1 = new Intent(this.getApplicationContext(), AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getActivity(this.getApplicationContext(),4, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(sender);
        sender.cancel();
    }
    public void createAlarm()
    {
        Gson gson = new Gson();
        String json = Utils.getUserPreferences(SplashScreenActivity.this, UserInfo.ALL_CASE_LIST);
        Type type = new TypeToken<List<CaseList>>(){}.getType();
        List<CaseList> list=gson.fromJson(json, type);
        if(list!=null) {
            caseList.addAll(list);
        }

        if(caseList!=null) {
            if (caseList.size() > 0) {
                for (int i = 0; i < caseList.size(); i++)
                {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE,1);
                  //  Log.e("date",caseList.get(i).getCase_date()+"    "+dateFormat.format(cal.getTime()));
                    if (caseList.get(i).getCase_date().equalsIgnoreCase(dateFormat.format(cal.getTime())))
                    {
                        Calendar calendar = Calendar.getInstance();
                        Calendar now = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.MINUTE, 30);
                        calendar.set(Calendar.HOUR,5);
                        calendar.set(Calendar.AM_PM, Calendar.PM);
                        if(now.before(calendar)) {
                            Intent myIntent = new Intent(SplashScreenActivity.this.getApplicationContext(), AlarmReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(SplashScreenActivity.this.getApplicationContext(), 4
                                    , myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);
                            alarmManager2.cancel(pendingIntent);
                            alarmManager2.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                    pendingIntent);
                        }
                        break;
                    }

                }
            }

        }
    }
    /** Expiry packages check **/
    public void dialogExpiry(int expiryInteger,final String serverMessage) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.update_layout);
        TextView textView=(TextView) dialog.findViewById(R.id.text1);
        Button yes = (Button) dialog.findViewById(R.id.bt_yes);
        if(expiryInteger==1) {
            textView.setText("Your package is about to expire.");
            yes.setText("OK");
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Utils.showToast(SplashScreenActivity.this, serverMessage);
                    Intent intent = new Intent(SplashScreenActivity.this, MainAcitivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
            if(expiryInteger==2)
            {
                textView.setText("You subscription is expired. Please renew your package first to use unlimited services");
                yes.setText("BUY NOW");
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(MapAppConstant.REGISTER_AS_PAID));
                        startActivity(i);
                    }
                });
        }

        dialog.show();
    }
}
