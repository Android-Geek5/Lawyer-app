package com.eweblog;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.eweblog.adapter.SearchCaseListAdapter;
import com.eweblog.common.MapAppConstant;
import com.eweblog.common.Prefshelper;
import com.eweblog.common.SlidingTabLayout;
import com.eweblog.common.VolleySingleton;
import com.eweblog.model.SearchCaseList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    SlidingTabLayout tabs;
    LinearLayout llBack,llSearch,llUnderline;
    TextView txtType, txtCaseTitle, txtJudge,txtWeekly,txtAll;
    Prefshelper prefshelper;
    LinearLayout llFilters;
    Spinner spinner;
    EditText edtSearch;
    View v1,v2,v3,v4,v5;
    int search_period=0;
    int search_in=0;
    RecyclerView recyclerView;
    SearchCaseListAdapter searchCaseListAdapter;
    List<SearchCaseList> searchCaseList=new ArrayList<>();
    HashMap<String,String> hash = new HashMap<String,String>();
    List<String> commonList=new ArrayList<>();
    ArrayAdapter<String> caseStatusAdapter;
    int caseStatusId=0;
    int searchType=0; // 2 for search and 1 for advanced search

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_search);
        inflateToolbar();
        inflateLayout();
        advancedOrSimpleSearch();
        onClickSubTabs();
        search_in=1;
        search_period=1;
    }

    public void inflateToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        prefshelper=new Prefshelper(this);
        spinner=(Spinner)toolbar.findViewById(R.id.spinner);
        edtSearch=(EditText)toolbar.findViewById(R.id.toolbar_title);
        llBack=(LinearLayout)toolbar.findViewById(R.id.imageView_back);
        llSearch=(LinearLayout)toolbar.findViewById(R.id.imageView_back2);
        llSearch.setVisibility(View.VISIBLE);
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchType==2) {
                    if (edtSearch.getText().toString() != null || !edtSearch.getText().toString().isEmpty())
                        search(edtSearch.getText().toString());
                }
                else if(searchType==1){advanceSearch(String.valueOf(caseStatusId));}
            }
        });
    }

    public void inflateLayout()
    {
        llFilters=(LinearLayout)findViewById(R.id.ll_filter);
        llUnderline=(LinearLayout) findViewById(R.id.ll_underline);
        v1=(View) findViewById(R.id.underline1);
        v2=(View) findViewById(R.id.underline2);
        v3=(View) findViewById(R.id.underline3);
        v4=(View) findViewById(R.id.underline4);
        v5=(View) findViewById(R.id.underline5);
        txtCaseTitle=(TextView)findViewById(R.id.text_title);
        txtType=(TextView)findViewById(R.id.text_type);
        txtJudge=(TextView)findViewById(R.id.text_judge);
        txtWeekly=(TextView) findViewById(R.id.text_weekly);
        txtAll=(TextView) findViewById(R.id.text_all);
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchCaseListAdapter=new SearchCaseListAdapter(SearchActivity.this,searchCaseList);
        recyclerView.setAdapter(searchCaseListAdapter);
    }

    public void onClickMainTabs()
    {
        txtType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_in=1;
                v1.setVisibility(View.VISIBLE);
                v2.setVisibility(View.INVISIBLE);
                v3.setVisibility(View.INVISIBLE);
            }
        });
        txtCaseTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_in=2;
                v2.setVisibility(View.VISIBLE);
                v1.setVisibility(View.INVISIBLE);
                v3.setVisibility(View.INVISIBLE);
            }
        });
        txtJudge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_in=3;
                v3.setVisibility(View.VISIBLE);
                v1.setVisibility(View.INVISIBLE);
                v2.setVisibility(View.INVISIBLE);
            }
        });

    }
    public void onClickSubTabs()
    {
        txtWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_period=1;
                v4.setVisibility(View.VISIBLE);
                v5.setVisibility(View.INVISIBLE);
            }
        });
        txtAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_period=2;
                v5.setVisibility(View.VISIBLE);
                v4.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void advancedOrSimpleSearch()
    {

        if(Utils.getUserPreferences(SearchActivity.this,Prefshelper.SEARCH).equalsIgnoreCase("advanced_search"))
        {
            llFilters.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
            edtSearch.setVisibility(View.GONE);
            llUnderline.setVisibility(View.GONE);
            getCaseStatuses();
            searchType=1;
        }
        else
        {
            llFilters.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
            edtSearch.setVisibility(View.VISIBLE);
            onClickMainTabs();
            searchType=2;
        }
    }

    public void search(final String searchKeyword)
    {
        Utils.hideSoftKeyboard(SearchActivity.this);
        try {
            final ProgressDialog pDialog = new ProgressDialog(SearchActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String SEARCH_URL=MapAppConstant.API+MapAppConstant.SEARCH;
            Log.e("SEARCH URL", SEARCH_URL);
            StringRequest sr = new StringRequest(Request.Method.POST, SEARCH_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("SEARCH RESPONSE",response);

                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Toast.makeText(SearchActivity.this, serverMessage,Toast.LENGTH_LONG).show();

                        searchCaseList.clear();
                        searchCaseListAdapter.notifyDataSetChanged();

                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            JSONArray jsonArray=object.getJSONArray("data");
                            if(jsonArray.length()>0)
                            {
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    SearchCaseList searchCaseListObject=new SearchCaseList();
                                    searchCaseListObject.setCase_id(jsonObject.getInt(Prefshelper.CASE_ID));
                                    searchCaseListObject.setCase_date(jsonObject.getString(Prefshelper.CASE_DATE));
                                    searchCaseListObject.setCase_title(jsonObject.getString(Prefshelper.CASE_TITLE));
                                    searchCaseList.add(searchCaseListObject);
                                }
                                searchCaseListAdapter.notifyDataSetChanged();
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
                    params.put(Prefshelper.USER_ID, Utils.getUserPreferences(SearchActivity.this,Prefshelper.USER_ID));
                    params.put(Prefshelper.USER_SECURITY_HASH, Utils.getUserPreferences(SearchActivity.this,Prefshelper.USER_SECURITY_HASH));
                    params.put(Prefshelper.CASE_SEARCH_IN, String.valueOf(search_in));
                    params.put(Prefshelper.CASE_SEARCH_KEYWORD, searchKeyword);
                    params.put(Prefshelper.CASE_SEARCH_PERIOD,String.valueOf(search_period));
                    Log.e("SEARCH REQUEST",params.toString());
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
    public void advanceSearch(final String searchKeyword)
    {
        Utils.hideSoftKeyboard(SearchActivity.this);
        try {
            final ProgressDialog pDialog = new ProgressDialog(SearchActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String ADVANCE_SEARCH_URL=MapAppConstant.API+MapAppConstant.ADVANCED_SEARCH;
            Log.e("SEARCH URL", ADVANCE_SEARCH_URL);
            StringRequest sr = new StringRequest(Request.Method.POST, ADVANCE_SEARCH_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("ADVANCE SEARCH RESPONSE",response);

                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Toast.makeText(SearchActivity.this, serverMessage,Toast.LENGTH_LONG).show();

                        searchCaseList.clear();
                        searchCaseListAdapter.notifyDataSetChanged();

                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            JSONArray jsonArray=object.getJSONArray("data");
                            if(jsonArray.length()>0)
                            {
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    SearchCaseList searchCaseListObject=new SearchCaseList();
                                    searchCaseListObject.setCase_id(jsonObject.getInt(Prefshelper.CASE_ID));
                                    searchCaseListObject.setCase_date(jsonObject.getString(Prefshelper.CASE_DATE));
                                    searchCaseListObject.setCase_title(jsonObject.getString(Prefshelper.CASE_TITLE));
                                    searchCaseList.add(searchCaseListObject);
                                }
                                searchCaseListAdapter.notifyDataSetChanged();
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
                    params.put(Prefshelper.USER_ID, Utils.getUserPreferences(SearchActivity.this,Prefshelper.USER_ID));
                    params.put(Prefshelper.USER_SECURITY_HASH, Utils.getUserPreferences(SearchActivity.this,Prefshelper.USER_SECURITY_HASH));
                    params.put(Prefshelper.CASE_STATUS_ID,searchKeyword);
                    params.put(Prefshelper.CASE_SEARCH_PERIOD,String.valueOf(search_period));
                    Log.e("SEARCH REQUEST",params.toString());
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
    public void getCaseStatuses() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(SearchActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String URL_CASE_STATUS=MapAppConstant.API + MapAppConstant.GET_CASE_STATUS;
            Log.e("CASE STATUS URL" ,URL_CASE_STATUS);
            StringRequest sr = new StringRequest(Request.Method.POST, URL_CASE_STATUS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("CASE STATUS RESPONSE",response);

                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Toast.makeText(SearchActivity.this, serverMessage,Toast.LENGTH_LONG).show();

                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {
                                if ("1".equals(serverCode)) {
                                    JSONArray jsonArray=object.getJSONArray("data");
                                    // CommonList commonListObject1=new CommonList(0,"Case Status");
                                    // commonList.add(commonListObject1);
                                    commonList.add("Case Status");
                                    hash.put("0","Case Status");
                                    if(jsonArray.length()>0)
                                    {
                                        for(int i=0; i<jsonArray.length();i++)
                                        {
                                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                                            String id=jsonObject.getString("case_status_id");
                                            String name=jsonObject.getString("case_status_name");
                                            hash.put(id,name);
                                            commonList.add(name);
                                            //CommonList commonListObject=new CommonList(id,name);
                                            // commonList.add(commonListObject);
                                        }
                                    }


                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (spinner.getAdapter() == null) {
                                caseStatusAdapter = new ArrayAdapter<String>(SearchActivity.this, R.layout.layout_spinner_white, commonList)
                                {
                                    @Override
                                    public boolean isEnabled(int position) {

                                        return position != 0;
                                    }

                                    @Override
                                    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                                        View view = super.getDropDownView(position, convertView, parent);
                                        TextView tv = (TextView) view;
                                            tv.setTextColor(Color.WHITE);
                                        return view;
                                    }
                                };
                                caseStatusAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
                                spinner.setAdapter(caseStatusAdapter);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        for (Map.Entry<String, String> entry : hash.entrySet()) {
                                            if (entry.getValue().equals(commonList.get(i))) {
                                                caseStatusId=Integer.valueOf(entry.getKey());
                                                Log.e("KEY",""+caseStatusId);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                                spinner.setSelection(1);
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

                    params.put("user_id", Utils.getUserPreferences(SearchActivity.this,Prefshelper.USER_ID));
                    params.put("user_security_hash", Utils.getUserPreferences(SearchActivity.this,Prefshelper.USER_SECURITY_HASH));

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
