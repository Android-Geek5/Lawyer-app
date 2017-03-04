package com.eweblog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
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
import com.eweblog.adapter.UserViewPagerAdapter;
import com.eweblog.common.MapAppConstant;
import com.eweblog.common.UserInfo;
import com.eweblog.common.SlidingTabLayout;
import com.eweblog.common.VolleySingleton;
import com.eweblog.fragment.ActiveUsersFragement;
import com.eweblog.fragment.InactiveUsersFragement;
import com.eweblog.model.ChildUsersList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewUsersActivity extends AppCompatActivity {
    ViewPager pager;
    UserViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Active","Inactive"};
    int Numboftabs =2;
    LinearLayout llBack;
    TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtTitle = (TextView) findViewById(R.id.toolbar_title);
        txtTitle.setText("View Users");
        adapter =  new UserViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    return getResources().getColor(R.color.tabsScrollColor,null);
                }
                else
                {
                    return getResources().getColor(R.color.tabsScrollColor);
                }
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        llBack=(LinearLayout)findViewById(R.id.imageView_back);

        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
                Intent in = new Intent(ViewUsersActivity.this,  MainAcitivity.class);
                startActivity(in);
            }
        });
        getChildUsers();
    }
  /* Fetch the list of active and inactive users in main activity and then pass the data to fragments**/
    public void getChildUsers()
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(ViewUsersActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String VIEW_CHILD_USERS= MapAppConstant.API+MapAppConstant.VIEW_CHILD;
            Log.e("VIEW CHILD URL", VIEW_CHILD_USERS);
            StringRequest sr = new StringRequest(Request.Method.POST, VIEW_CHILD_USERS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("VIEW CHILD RESPONSE",response);

                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Utils.showToast(ViewUsersActivity.this, serverMessage);

                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            JSONObject data=object.getJSONObject("data");
                            JSONArray inactiveUsers=data.getJSONArray("inactive_users");
                            List<ChildUsersList> childUsersLists=new ArrayList<>();
                            // fetch inactive users first
                            if(inactiveUsers.length()>0)
                            {
                                for(int i = 0; i<inactiveUsers.length(); i++)
                            {
                                JSONObject jsonObject=inactiveUsers.getJSONObject(i);
                                ChildUsersList childUsersListObject=new ChildUsersList();
                                childUsersListObject.setId(jsonObject.getInt(UserInfo.USER_ID));
                                childUsersListObject.setUserDetail(jsonObject.getString(UserInfo.USER_DETAILS));
                                childUsersListObject.setCasesCount(jsonObject.getInt(UserInfo.CASES_COUNT));
                                childUsersLists.add(childUsersListObject);
                            }
                                //Now send data to inactive fragment
                                inflateInactiveUsers(childUsersLists,false);
                            }
                            else
                            {
                                //In case of no inactive users,show null layout
                                inflateInactiveUsers(childUsersLists,true);
                            }
                            JSONArray activeUsers=data.getJSONArray("active_users");
                            List<ChildUsersList> childUsersLists2=new ArrayList<>();
                            // fetch active users first
                            if(activeUsers.length()>0)
                            {
                                for(int i = 0; i<activeUsers.length(); i++)
                                {
                                    JSONObject jsonObject=activeUsers.getJSONObject(i);
                                    ChildUsersList childUsersListObject=new ChildUsersList();
                                    childUsersListObject.setId(jsonObject.getInt(UserInfo.USER_ID));
                                    childUsersListObject.setUserDetail(jsonObject.getString(UserInfo.USER_DETAILS));
                                    childUsersListObject.setCasesCount(jsonObject.getInt(UserInfo.CASES_COUNT));
                                    childUsersLists2.add(childUsersListObject);
                                }
                                //Now send data to active fragment
                                inflateActiveUsers(childUsersLists2,false);
                            }
                            else
                            {
                                //In case of no active users,show null layout
                                inflateActiveUsers(childUsersLists2,true);
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
                       Utils.showToast(ViewUsersActivity.this, "No Internet Connection");
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
                    params.put(UserInfo.USER_ID, Utils.getUserPreferences(ViewUsersActivity.this, UserInfo.USER_ID));
                    params.put(UserInfo.USER_SECURITY_HASH, Utils.getUserPreferences(ViewUsersActivity.this, UserInfo.USER_SECURITY_HASH));

                    Log.e("VIEW CHILD REQUEST",params.toString());
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

    public void inflateInactiveUsers(List<ChildUsersList> childUsersList,boolean noUser)
    {
        Fragment reference = null;
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

        for (int i = 0; i < fragmentList.size(); i++) {
            reference = fragmentList.get(i);
            if (reference != null && reference instanceof InactiveUsersFragement) {
                if(noUser)
                    ((InactiveUsersFragement) reference).noUsers();   // No users
                else
                ((InactiveUsersFragement) reference).inflateList(childUsersList); //Pass list of users
            }
        }
    }

    public void inflateActiveUsers(List<ChildUsersList> childUsersList,boolean noUser)
    {
        Fragment reference = null;
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

        for (int i = 0; i < fragmentList.size(); i++) {
            reference = fragmentList.get(i);
            if (reference != null && reference instanceof ActiveUsersFragement) {
                if(noUser)
                    ((ActiveUsersFragement) reference).noUsers(); // No users
                else
                ((ActiveUsersFragement) reference).inflateList(childUsersList); //Pass list of users
            }
        }
    }
}
