package com.eweblog;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.eweblog.adapter.LoginViewPagerAdapter;
import com.eweblog.common.AlarmReceiver;
import com.eweblog.common.UserInfo;
import com.eweblog.common.ConnectionDetector;
import com.eweblog.common.MapAppConstant;
import com.eweblog.common.SlidingTabLayout;
import com.eweblog.common.VolleySingleton;
import com.eweblog.model.CaseList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    ViewPager pager;
    LoginViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Individual User","Business User"};
    int Numboftabs =2;
    ConnectionDetector cd;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    List<CaseList> caseList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
              // WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        adapter = new LoginViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        cd = new ConnectionDetector(getApplicationContext());
        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return getResources().getColor(R.color.tabsScrollColor, null);
                } else {
                    return getResources().getColor(R.color.tabsScrollColor);
                }
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
    }

    /* Get all cases after login to set alarms**/
    public void getAllCases() {

        final List<CaseList> caseLists =new ArrayList<>();
        try {
            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String GET_ALL_CASES_URL= MapAppConstant.API+MapAppConstant.GET_USER_ALL_CASES;
            Log.e("GET ALL CASES URL", GET_ALL_CASES_URL);
            StringRequest sr = new StringRequest(Request.Method.POST,GET_ALL_CASES_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("GET ALL CASES RESPONSE", response);
                    caseLists.clear();
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        if (serverCode.equalsIgnoreCase("0"))
                        {
                            Intent intent = new Intent(LoginActivity.this, MainAcitivity.class);
                            startActivity(intent);
                            finish();
                        }
                        if (serverCode.equalsIgnoreCase("1"))
                        {
                            try {
                                if ("1".equals(serverCode))
                                {

                                    JSONArray jsonArray=object.getJSONArray("data");
                                    if (jsonArray.length() > 0)
                                    {
                                        for (int i = 0; i < jsonArray.length(); i++)
                                        {

                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String date=jsonObject.getString("date");
                                            JSONArray jsonArray1=jsonObject.getJSONArray("details");
                                            if(jsonArray1.length()>0)
                                            {
                                                for (int j = 0; j < jsonArray1.length(); j++)
                                                {
                                                    JSONObject jsonObject1=jsonArray1.getJSONObject(j);
                                                    CaseList caseListObject =new CaseList();
                                                    caseListObject.setCase_id(jsonObject1.getInt(UserInfo.CASE_ID));
                                                    caseListObject.setCase_date(date);
                                                    caseListObject.setCase_title(jsonObject1.getString(UserInfo.CASE_TITLE));
                                                    caseLists.add(caseListObject);
                                                }
                                            }
                                            Gson gson1 = new Gson();
                                            String json1 = gson1.toJson(caseLists);
                                            Utils.storeUserPreferences(LoginActivity.this, UserInfo.ALL_CASE_LIST,json1);
                                        }
                                    }

                                }
                                Intent intent = new Intent(LoginActivity.this, MainAcitivity.class);
                                startActivity(intent);
                                finish();
                                createAlarm();

                            } catch (Exception e) {
                                e.printStackTrace();
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
                        Toast.makeText(LoginActivity.this, "No Internet Connection",
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
                    params.put(UserInfo.USER_ID, Utils.getUserPreferences(LoginActivity.this, UserInfo.USER_ID));
                    params.put(UserInfo.USER_SECURITY_HASH, Utils.getUserPreferences(LoginActivity.this, UserInfo.USER_SECURITY_HASH));

                    return params;
                }
            };
            sr.setShouldCache(true);

            sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(LoginActivity.this.getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createAlarm()
    {
        Gson gson = new Gson();
        String json = Utils.getUserPreferences(this, UserInfo.ALL_CASE_LIST);
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
                    if (caseList.get(i).getCase_date().equalsIgnoreCase(dateFormat.format(cal.getTime())))
                    {
                        Calendar calendar = Calendar.getInstance();
                        Calendar now = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.MINUTE, 30);
                        calendar.set(Calendar.HOUR,5);
                        calendar.set(Calendar.AM_PM, Calendar.PM);
                        if(now.before(calendar)) {
                            Intent myIntent = new Intent(LoginActivity.this.getApplicationContext(), AlarmReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(LoginActivity.this.getApplicationContext(), 4
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
}

