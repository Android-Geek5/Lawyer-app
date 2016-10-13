package com.erginus.lawyerapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;

import android.service.textservice.SpellCheckerService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
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
import com.erginus.lawyerapp.common.MapAppConstant;
import com.erginus.lawyerapp.common.Prefshelper;
import com.erginus.lawyerapp.common.VolleySingleton;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AddCaseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    LinearLayout linearLayout;
    Spinner sprDay, sprMonth, sprYear, sprNDay, sprNMonth, sprNYear, sprCaseType, sprSDay, sprSMonth, sprSYear, sprTime, sprNtime;
    //private SpellCheckerSession mScs;
    ArrayAdapter<String> stringArrayAdapter;
    Button btnAdd;
    EditText edtCourtName, edtCaseNumber, edtCaseTitle, edtCaseType, edtStatus, edtRetainName, edtRetainMobile,
            edtOppositeName,edtOppositeNumber, edtComments;
    Prefshelper prefshelper;
    String strNumber, strTitle, strType, strCourt, strStatus, strPreviousDt, strNextDt, strOCName,strOCContact, strRName, strRContact,
    strComment, strStartDate, strDay, strMonth, strYear, strNDay, strNMonth, strNYear, strSDay, strSMonth,strSYear, strTime, strNTime,
            prevTime, nextTime;
    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    DateFormat dateFormatter2 = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_case);
        prefshelper=new Prefshelper(AddCaseActivity.this);
        sprDay = (Spinner) findViewById(R.id.spinner_day);
        sprMonth = (Spinner) findViewById(R.id.spinner_month);
        sprYear = (Spinner) findViewById(R.id.spinner_year);
        sprNDay = (Spinner) findViewById(R.id.spinner_nday);
        sprNMonth = (Spinner) findViewById(R.id.spinner_nmonth);
        sprNYear = (Spinner) findViewById(R.id.spinner_nyear);
        sprSDay = (Spinner) findViewById(R.id.spinner_sday);
        sprSMonth = (Spinner) findViewById(R.id.spinner_smonth);
        sprSYear = (Spinner) findViewById(R.id.spinner_syear);
        sprTime = (Spinner) findViewById(R.id.spinner_time);
        sprNtime = (Spinner) findViewById(R.id.spinner_ntime);
        sprCaseType = (Spinner) findViewById(R.id.spinner_type);
        btnAdd=(Button)findViewById(R.id.add);
        linearLayout = (LinearLayout) findViewById(R.id.ll_navi);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(AddCaseActivity.this, SelectDateActivity.class);
                startActivity(intent1);
                finish();
                SelectDateActivity.txtTitle.setText("Home");
            }
        });
        edtCourtName=(EditText) findViewById(R.id.textView_name);
        edtCaseNumber=(EditText) findViewById(R.id.textView_number);
        edtCaseTitle=(EditText) findViewById(R.id.textView_title);
        edtCaseType=(EditText) findViewById(R.id.textView_type);
        edtStatus=(EditText) findViewById(R.id.textView_position);
        edtRetainName=(EditText) findViewById(R.id.textView_nm);
        edtRetainMobile=(EditText) findViewById(R.id.textView_mobile);
        edtOppositeName=(EditText) findViewById(R.id.textView_cname);
        edtOppositeNumber=(EditText) findViewById(R.id.textView_cmobile);
        edtComments=(EditText) findViewById(R.id.textView_comments);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //  mScs.getSuggestions(new TextInfo(edtCourtName.getText().toString()), 3 );
           /*     PackageManager pm = getPackageManager();
                Intent spell = new Intent(SpellCheckerService.SERVICE_INTERFACE);
                ResolveInfo info = pm.resolveService(spell, 0);
                if (info == null) {
                    edtCourtName.setText("no spell checker found");
                } else {
                    edtCourtName.setText("found spell checker " + info.serviceInfo.name + " in package " + info.serviceInfo.packageName);
                }*/
                View focusView = null;
                boolean cancelLogin = false;

                strNumber=edtCaseNumber.getText().toString();
                strTitle=edtCaseTitle.getText().toString();
                strCourt=edtCourtName.getText().toString();
                strOCName=edtOppositeName.getText().toString();
                strOCContact=edtOppositeNumber.getText().toString();
                strRName=edtRetainName.getText().toString();
                strRContact=edtRetainMobile.getText().toString();
                strComment=edtComments.getText().toString();
                strStatus=edtStatus.getText().toString();

                if(edtCaseType.getVisibility()==View.VISIBLE)
                {
                    strType=edtCaseType.getText().toString();
                }

              /*  if(strStartDate.equalsIgnoreCase(""))
                {
                    Toast.makeText(AddCaseActivity.this,"Please Select Start Date", Toast.LENGTH_SHORT).show();
                }
                if(strPreviousDt.equalsIgnoreCase(""))
                {
                    Toast.makeText(AddCaseActivity.this,"Please Select Previous Date", Toast.LENGTH_SHORT).show();
                }


*/              if(prevTime!=null)
                {
                    prevTime= strTime.substring(0, strTime.length()-2);
                }
                if(nextTime!=null)
                {
                    nextTime=strNTime.substring(0, strNTime.length()-2);
                }

                String startDt=strSYear+"-"+strSMonth+"-"+strSDay;
                try {
                    Date dt1=dateFormatter2.parse(startDt);
                    strStartDate=dateFormatter2.format(dt1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.e("year", strSYear);
                String prevDate=strYear+"-"+strMonth+"-"+strDay+" "+prevTime;
                try {
                    Date date=dateFormatter.parse(prevDate);
                    strPreviousDt=dateFormatter.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String nextDate=strNYear+"-"+strNMonth+"-"+strNDay+" "+nextTime;
                try {
                    Date dt=dateFormatter.parse(nextDate);
                    strNextDt=dateFormatter.format(dt);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(strTitle)) {
                    edtCaseTitle.setError("Field must not be empty.");
                    focusView = edtCaseTitle;
                    cancelLogin = true;
                }

                if (TextUtils.isEmpty(strCourt)) {
                    edtCourtName.setError("Field must not be empty.");
                    focusView = edtCourtName;
                    cancelLogin = true;
                }
                if (TextUtils.isEmpty(strOCName)) {
                    edtOppositeName.setError("Field must not be empty.");
                    focusView = edtOppositeName;
                    cancelLogin = true;
                }
                if (TextUtils.isEmpty(strStatus)) {
                    edtStatus.setError("Field must not be empty.");
                    focusView = edtStatus;
                    cancelLogin = true;
                }

                if (TextUtils.isEmpty(strRName)) {
                    edtRetainName.setError("Field must not be empty.");
                    focusView = edtRetainName;
                    cancelLogin = true;
                }
                if (TextUtils.isEmpty(strRContact)) {
                    edtRetainMobile.setError("Field must not be empty.");
                    focusView = edtRetainMobile;
                    cancelLogin = true;
                }


                if (cancelLogin) {
                    // error in login
                    focusView.requestFocus();
                } else {
                   
                    addCase();
                }


            }
        });

        if (sprCaseType.getAdapter() == null) {
            stringArrayAdapter = new ArrayAdapter<String>(AddCaseActivity.this, R.layout.layout_spinner, getResources().getStringArray(R.array.case_type)) {

                @Override
                public boolean isEnabled(int position) {

                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }

                    return view;
                }
            };
            stringArrayAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
            sprCaseType.setAdapter(stringArrayAdapter);
        }
        if (sprTime.getAdapter() == null) {
            stringArrayAdapter = new ArrayAdapter<String>(AddCaseActivity.this, R.layout.layout_spinner, getResources().getStringArray(R.array.time)) {

                @Override
                public boolean isEnabled(int position) {

                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }

                    return view;
                }
            };
            stringArrayAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
            sprTime.setAdapter(stringArrayAdapter);
        }
        if (sprNtime.getAdapter() == null) {
            stringArrayAdapter = new ArrayAdapter<String>(AddCaseActivity.this, R.layout.layout_spinner, getResources().getStringArray(R.array.time)) {

                @Override
                public boolean isEnabled(int position) {

                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }

                    return view;
                }
            };
            stringArrayAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
            sprNtime.setAdapter(stringArrayAdapter);
        }
        if (sprSDay.getAdapter() == null) {
           stringArrayAdapter = new ArrayAdapter<String>(AddCaseActivity.this, R.layout.layout_spinner, getResources().getStringArray(R.array.day)) {

                @Override
                public boolean isEnabled(int position) {

                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            stringArrayAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
            sprSDay.setAdapter(stringArrayAdapter);
        }
        if (sprSMonth.getAdapter() == null) {
           stringArrayAdapter = new ArrayAdapter<String>(AddCaseActivity.this, R.layout.layout_spinner, getResources().getStringArray(R.array.month)) {

                @Override
                public boolean isEnabled(int position) {

                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            stringArrayAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
            sprSMonth.setAdapter(stringArrayAdapter);
        }
        if (sprSYear.getAdapter() == null) {
           stringArrayAdapter = new ArrayAdapter<String>(AddCaseActivity.this, R.layout.layout_spinner, getResources().getStringArray(R.array.year)) {

                @Override
                public boolean isEnabled(int position) {

                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            stringArrayAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
            sprSYear.setAdapter(stringArrayAdapter);
        }
        if (sprDay.getAdapter() == null) {
            stringArrayAdapter = new ArrayAdapter<String>(AddCaseActivity.this, R.layout.layout_spinner, getResources().getStringArray(R.array.day)) {

                @Override
                public boolean isEnabled(int position) {

                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            stringArrayAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
            sprDay.setAdapter(stringArrayAdapter);
        }
        if (sprMonth.getAdapter() == null) {
            stringArrayAdapter = new ArrayAdapter<String>(AddCaseActivity.this, R.layout.layout_spinner, getResources().getStringArray(R.array.month)) {

                @Override
                public boolean isEnabled(int position) {

                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            stringArrayAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
            sprMonth.setAdapter(stringArrayAdapter);
        }
        if (sprYear.getAdapter() == null) {
            stringArrayAdapter = new ArrayAdapter<String>(AddCaseActivity.this, R.layout.layout_spinner, getResources().getStringArray(R.array.year)) {

                @Override
                public boolean isEnabled(int position) {

                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            stringArrayAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
            sprYear.setAdapter(stringArrayAdapter);
        }
        if (sprNDay.getAdapter() == null) {
           stringArrayAdapter = new ArrayAdapter<String>(AddCaseActivity.this, R.layout.layout_spinner, getResources().getStringArray(R.array.day)) {

                @Override
                public boolean isEnabled(int position) {

                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            stringArrayAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
            sprNDay.setAdapter(stringArrayAdapter);
        }
        if (sprNMonth.getAdapter() == null) {
           stringArrayAdapter = new ArrayAdapter<String>(AddCaseActivity.this, R.layout.layout_spinner, getResources().getStringArray(R.array.month)) {

                @Override
                public boolean isEnabled(int position) {

                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            stringArrayAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
            sprNMonth.setAdapter(stringArrayAdapter);
        }
        if (sprNYear.getAdapter() == null) {
            stringArrayAdapter = new ArrayAdapter<String>(AddCaseActivity.this, R.layout.layout_spinner, getResources().getStringArray(R.array.year)) {

                @Override
                public boolean isEnabled(int position) {

                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            stringArrayAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
            sprNYear.setAdapter(stringArrayAdapter);
        }
        sprCaseType.setOnItemSelectedListener(AddCaseActivity.this);
        sprDay.setOnItemSelectedListener(AddCaseActivity.this);
        sprMonth.setOnItemSelectedListener(AddCaseActivity.this);
        sprYear.setOnItemSelectedListener(AddCaseActivity.this);
        sprNDay.setOnItemSelectedListener(AddCaseActivity.this);
        sprNMonth.setOnItemSelectedListener(AddCaseActivity.this);
        sprNYear.setOnItemSelectedListener(AddCaseActivity.this);
        sprSDay.setOnItemSelectedListener(AddCaseActivity.this);
        sprSMonth.setOnItemSelectedListener(AddCaseActivity.this);
        sprSYear.setOnItemSelectedListener(AddCaseActivity.this);
        sprTime.setOnItemSelectedListener(AddCaseActivity.this);
        sprNtime.setOnItemSelectedListener(AddCaseActivity.this);



    }
   /* public void onResume() {
        super.onResume();
        final TextServicesManager tsm = (TextServicesManager) getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE);
        mScs = tsm.newSpellCheckerSession(null, null, this, true);
    }

    public void onPause() {
        super.onPause();
        if (mScs != null) {
            mScs.close();
        }
    }*/
    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(AddCaseActivity.this, SelectDateActivity.class);
        startActivity(intent1);
        finish();
        SelectDateActivity.txtTitle.setText("Home");
        super.onBackPressed();

    }


   /* @Override
    public void onGetSuggestions(SuggestionsInfo[] arg0) {

        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < arg0.length; ++i) {
            // Returned suggestions are contained in SuggestionsInfo
            final int len = arg0[i].getSuggestionsCount();
            sb.append('\n');

            for (int j = 0; j < len; ++j) {
                sb.append("," + arg0[i].getSuggestionAt(j));
            }

            sb.append(" (" + len + ")");
        }

    }

    @Override
    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] sentenceSuggestionsInfos) {

    }*/
   public void addCase() {
       try {
           final ProgressDialog pDialog = new ProgressDialog(AddCaseActivity.this);
           pDialog.setMessage("Loading...");
           pDialog.show();

           Log.e("", "SIGNUP " + MapAppConstant.API + "add_case");
           StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "add_case", new Response.Listener<String>() {
               @Override
               public void onResponse(String response) {
                   pDialog.dismiss();
                   Log.d("", ".......response====" + response.toString());

                   ////////
                   try {
                       JSONObject object = new JSONObject(response);
                       String serverCode = object.getString("code");
                       String serverMessage = object.getString("message");
                       Toast.makeText(AddCaseActivity.this, serverMessage,Toast.LENGTH_LONG).show();

                       if (serverCode.equalsIgnoreCase("0")) {

                       }
                       if (serverCode.equalsIgnoreCase("1")) {
                           try {
                               if ("1".equals(serverCode)) {

                               }

                           } catch (Exception e) {
                               e.printStackTrace();
                           }

                           Intent intent1 = new Intent(AddCaseActivity.this, SelectDateActivity.class);
                           startActivity(intent1);
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
                       Toast.makeText(AddCaseActivity.this, "Timeout Error",
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
                   params.put("case_number", strNumber);
                   params.put("case_title", strTitle);
                   params.put("case_type", strType);
                   params.put("case_court_name", strCourt);
                   params.put("case_position_status", strStatus);
                   params.put("case_previous_date", strPreviousDt);
                   params.put("case_next_date", strNextDt);
                   params.put("case_opposite_counselor_name", strOCName);
                   params.put("case_opposite_counselor_contact", strOCContact);
                   params.put("case_retained_name", strRName);
                   params.put("case_retained_contact", strRContact);
                   params.put("case_comment", strComment);
                   params.put("case_started", strStartDate);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;
        if(spinner.getId()==R.id.spinner_type)
        {
            if(i==3)
            {
                edtCaseType.setVisibility(View.VISIBLE);
                edtCaseType.requestFocus();

            }
            else if(i==0)
            {
                strType="";
            }
            else
            {
                edtCourtName.requestFocus();
                edtCaseType.setVisibility(View.GONE);
                strType= sprCaseType.getSelectedItem().toString();
                Log.e("status", strType);
            }
        }
        if(spinner.getId()==R.id.spinner_day)
        {
            if (i==0)
            {
                strDay="";
            }
            else {
                strDay= sprDay.getSelectedItem().toString();
                Log.e("day", strDay);
            }
        }
        if(spinner.getId()==R.id.spinner_month)
        {

            if (i==0)
            {
                strMonth="";
            }
            else {
                strMonth= String.valueOf(i);
                Log.e("day", strMonth);
            }
        }
        if(spinner.getId()==R.id.spinner_year)
        {
            if (i==0)
            {
                strYear="";
            }
            else {
                strYear=sprYear.getSelectedItem().toString();
                Log.e("year", strYear);

            }
        }
        if(spinner.getId()==R.id.spinner_nday)
        {
            if (i==0)
            {
                strNDay="";
            }
            else {
                strNDay=sprNDay.getSelectedItem().toString();
                Log.e("year", strNDay);

            }
        }
        if(spinner.getId()==R.id.spinner_nmonth)
        {
            if (i==0)
            {
                strNMonth="";
            }
            else {
                strNMonth=String.valueOf(i);
                Log.e("year", strNMonth);
            }
        }
        if(spinner.getId()==R.id.spinner_nyear)
        {
            if (i==0)
            {
                strNYear="";
            }
            else {
                strNYear= sprNYear.getSelectedItem().toString();
                Log.e("year", strNYear);
            }
        }
        if(spinner.getId()==R.id.spinner_sday)
        {
            if (i==0)
            {
                strSDay="";
            }
            else {
                strSDay= sprSDay.getSelectedItem().toString();
                Log.e("year", strSDay);
            }
        }
        if(spinner.getId()==R.id.spinner_smonth)
        {
            if (i==0)
            {
                strSMonth="";
            }
            else {
                strSMonth= String.valueOf(i);
                Log.e("year", String.valueOf(i));
            }
        }
        if(spinner.getId()==R.id.spinner_syear)
        {
            if (i==0)
            {
                strSYear="";
            }
            else {

                strSYear=  sprSYear.getSelectedItem().toString();
                String startDt=strSYear+"-"+strSMonth+"-"+strSDay;
                try {
                    Date dt1=dateFormatter2.parse(startDt);
                    strStartDate=dateFormatter2.format(dt1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.e("year", strStartDate);
            }
        }
        if(spinner.getId()==R.id.spinner_time)
        {
            if (i==0)
            {
                strTime="";
            }
            else {
                edtCaseTitle.requestFocus();
                strTime= sprTime.getSelectedItem().toString();

                prevTime= strTime.substring(0, strTime.length()-2);
                Log.e("year", prevTime);


                String prevDate=strYear+"-"+strMonth+"-"+strDay+" "+prevTime;
                try {
                    Date date=dateFormatter.parse(prevDate);
                    strPreviousDt=dateFormatter.format(date);
                    Log.e("year", strPreviousDt);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        }
        if(spinner.getId()==R.id.spinner_ntime)
        {
            if (i==0)
            {
                strNTime="";
            }
            else {
                edtRetainName.requestFocus();
                strNTime=sprNtime.getSelectedItem().toString();


                nextTime=strNTime.substring(0, strNTime.length()-2);
                Log.e("year", nextTime);


                String nextDate=strNYear+"-"+strNMonth+"-"+strNDay+" "+nextTime;
                try {
                    Date dt=dateFormatter.parse(nextDate);
                    strNextDt=dateFormatter.format(dt);
                    Log.e("year", strNextDt);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
