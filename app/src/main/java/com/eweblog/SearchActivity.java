package com.eweblog;

import android.app.ProgressDialog;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.eweblog.adapter.ListAdapter;
import com.eweblog.adapter.ViewPagerAdapter;
import com.eweblog.common.MapAppConstant;
import com.eweblog.common.Prefshelper;
import com.eweblog.common.SlidingTabLayout;
import com.eweblog.common.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    ViewPager pager;
    ListAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Weekly","All"};
    int Numboftabs =2;
    LinearLayout llBack;
    TextView txtType, txtCaseTitle, txtJudge;
    Prefshelper prefshelper;
    LinearLayout llFilters;
    Spinner spinner;
    EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefshelper=new Prefshelper(this);
        spinner=(Spinner)findViewById(R.id.spinner);
        edtSearch=(EditText) findViewById(R.id.toolbar_title);
        llFilters=(LinearLayout)findViewById(R.id.ll_filter);
        adapter =  new ListAdapter(getSupportFragmentManager(),Titles,Numboftabs);

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
        txtCaseTitle=(TextView)findViewById(R.id.text_title);
        txtType=(TextView)findViewById(R.id.text_type);
        txtJudge=(TextView)findViewById(R.id.text_judge);
        if(prefshelper.getSearch().equalsIgnoreCase("case_status"))
        {
            llFilters.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
            edtSearch.setVisibility(View.GONE);
        }
        else
        {
            llFilters.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
            edtSearch.setVisibility(View.VISIBLE);
        }
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        txtCaseTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    txtCaseTitle.setTextColor(getResources().getColor(R.color.tabsScrollColor,null));
                    txtType.setTextColor(getResources().getColor(R.color.colorText,null));
                    txtJudge.setTextColor(getResources().getColor(R.color.colorText,null));
                }
                else
                {
                    txtCaseTitle.setTextColor(getResources().getColor(R.color.tabsScrollColor));
                    txtType.setTextColor(getResources().getColor(R.color.colorText));
                    txtJudge.setTextColor(getResources().getColor(R.color.colorText));
                }
            }
        });
        txtJudge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    txtJudge.setTextColor(getResources().getColor(R.color.tabsScrollColor,null));
                    txtType.setTextColor(getResources().getColor(R.color.colorText,null));
                    txtCaseTitle.setTextColor(getResources().getColor(R.color.colorText,null));
                }
                else
                {
                    txtJudge.setTextColor(getResources().getColor(R.color.tabsScrollColor));
                    txtType.setTextColor(getResources().getColor(R.color.colorText));
                    txtCaseTitle.setTextColor(getResources().getColor(R.color.colorText));
                }
            }
        });
        txtType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    txtType.setTextColor(getResources().getColor(R.color.tabsScrollColor,null));
                    txtJudge.setTextColor(getResources().getColor(R.color.colorText,null));
                    txtCaseTitle.setTextColor(getResources().getColor(R.color.colorText,null));
                }
                else
                {
                    txtType.setTextColor(getResources().getColor(R.color.tabsScrollColor));
                    txtJudge.setTextColor(getResources().getColor(R.color.colorText));
                    txtCaseTitle.setTextColor(getResources().getColor(R.color.colorText));
                }
            }
        });

    }
    public void getCaseStatuses() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(SearchActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

            Log.e("", "get_case_statuses " + MapAppConstant.API + "get_case_statuses");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "get_case_statuses", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response);

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Toast.makeText(SearchActivity.this, serverMessage,Toast.LENGTH_LONG).show();
                        Log.e("error", response);
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try
                            {
                                prefshelper.getPreferences().edit().clear().apply();
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }

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
                        Toast.makeText(SearchActivity.this, "No Internet Connection",
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
