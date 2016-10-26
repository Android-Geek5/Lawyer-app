package com.eweblog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.eweblog.common.ConnectionDetector;
import com.eweblog.common.MapAppConstant;
import com.eweblog.common.Prefshelper;
import com.eweblog.common.VolleySingleton;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class AddCaseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    LinearLayout linearLayout;
    Spinner sprDay, sprMonth, sprYear, sprNDay, sprNMonth, sprNYear, sprCaseType, sprSDay, sprSMonth, sprSYear;

    ArrayAdapter<String> stringArrayAdapter;
    Button btnAdd;
    EditText edtCourtName, edtCaseNumber, edtCaseTitle, edtCaseType, edtStatus, edtRetainName, edtRetainMobile,
            edtOppositeName, edtOppositeNumber, edtComments;
    Prefshelper prefshelper;
    String strNumber, strTitle, strType, strCourt, strStatus, strPreviousDt, strNextDt, strOCName, strOCContact, strRName, strRContact,
            strComment, strStartDate, strDay, strMonth, strYear, strNDay, strNMonth, strNYear, strSDay, strSMonth, strSYear, strTime, strNTime,
            prevTime, nextTime;

    DateFormat dateFormatter2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    TextView txtStartError, txtPrevError, txtNextError;
    ConnectionDetector cd;
    Calendar cal = Calendar.getInstance();
    Date sysDate = cal.getTime();
    Date dt1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_case);
        prefshelper = new Prefshelper(AddCaseActivity.this);
        sprDay = (Spinner) findViewById(R.id.spinner_day);
        sprMonth = (Spinner) findViewById(R.id.spinner_month);
        sprYear = (Spinner) findViewById(R.id.spinner_year);
        sprNDay = (Spinner) findViewById(R.id.spinner_nday);
        sprNMonth = (Spinner) findViewById(R.id.spinner_nmonth);
        sprNYear = (Spinner) findViewById(R.id.spinner_nyear);
        sprSDay = (Spinner) findViewById(R.id.spinner_sday);
        sprSMonth = (Spinner) findViewById(R.id.spinner_smonth);
        sprSYear = (Spinner) findViewById(R.id.spinner_syear);

        sprCaseType = (Spinner) findViewById(R.id.spinner_type);
        btnAdd = (Button) findViewById(R.id.add);
        txtStartError = (TextView) findViewById(R.id.tw_start_error);
        txtPrevError = (TextView) findViewById(R.id.tw_prev_error);
        txtNextError = (TextView) findViewById(R.id.tw_next_error);
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
        cd= new ConnectionDetector(AddCaseActivity.this);
        if (!cd.isConnectingToInternet())
        {
            dialog();
            btnAdd.setEnabled(false);
        }
        else
        {
            btnAdd.setEnabled(true);
        }
        edtCourtName = (EditText) findViewById(R.id.textView_name);
        edtCaseNumber = (EditText) findViewById(R.id.textView_number);
        edtCaseTitle = (EditText) findViewById(R.id.textView_title);
        edtCaseType = (EditText) findViewById(R.id.textView_type);
        edtStatus = (EditText) findViewById(R.id.textView_position);
        edtRetainName = (EditText) findViewById(R.id.textView_nm);
        edtRetainMobile = (EditText) findViewById(R.id.textView_mobile);
        edtOppositeName = (EditText) findViewById(R.id.textView_cname);
        edtOppositeNumber = (EditText) findViewById(R.id.textView_cmobile);
        edtComments = (EditText) findViewById(R.id.textView_comments);
        Log.e("current adte", dateFormatter2.format(sysDate));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View focusView = null;
                boolean cancelLogin = false;

                strNumber = edtCaseNumber.getText().toString();
                strTitle = edtCaseTitle.getText().toString();
                strCourt = edtCourtName.getText().toString();
                strOCName = edtOppositeName.getText().toString();
                strOCContact = edtOppositeNumber.getText().toString();
                strRName = edtRetainName.getText().toString();
                strRContact = edtRetainMobile.getText().toString();
                strComment = edtComments.getText().toString();
                strStatus = edtStatus.getText().toString();

                if (edtCaseType.getVisibility() == View.VISIBLE)
                {
                    strType = edtCaseType.getText().toString();
                }

                if (strStartDate == null)
                {
                    txtStartError.setVisibility(View.VISIBLE);

                }
                else
                {
                    txtStartError.setVisibility(View.GONE);
                }
                if (strPreviousDt == null )
                {
                    txtPrevError.setVisibility(View.VISIBLE);
                }
                else
                {
                    txtPrevError.setVisibility(View.GONE);
                }
                if (strNextDt == null)
                {
                    txtNextError.setVisibility(View.VISIBLE);
                }
                else
                {
                    txtNextError.setVisibility(View.GONE);
                }


                if(!strSYear.equalsIgnoreCase("") && !strSMonth.equalsIgnoreCase("") && !strSDay.equalsIgnoreCase(""))
                {
                    String startDt = strSYear + "-" + strSMonth + "-" + strSDay;
                    try {
                        dt1 = dateFormatter2.parse(startDt);
                         strStartDate = dateFormatter2.format(dt1);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Log.e("year", strSYear);

                }
                if(!strYear.equalsIgnoreCase("") && !strMonth.equalsIgnoreCase("") && !strDay.equalsIgnoreCase(""))
                {
                    String prevDate = strYear + "-" + strMonth + "-" + strDay;
                    try {
                        Date date = dateFormatter2.parse(prevDate);
                           strPreviousDt = dateFormatter2.format(date);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if(!strNYear.equalsIgnoreCase("") && !strNMonth.equalsIgnoreCase("") && !strNDay.equalsIgnoreCase(""))
                {
                    String nextDate = strNYear + "-" + strNMonth + "-" + strNDay;
                    try {
                        Date dt = dateFormatter2.parse(nextDate);
                        strNextDt = dateFormatter2.format(dt);


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (TextUtils.isEmpty(strTitle)) {
                    edtCaseTitle.setError("Field must not be empty.");
                    focusView = edtCaseTitle;
                    cancelLogin = true;
                }
                if (TextUtils.isEmpty(strNumber)) {
                    edtCaseNumber.setError("Field must not be empty.");
                    focusView = edtCaseNumber;
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
                if (TextUtils.isEmpty(strOCName)) {
                    edtOppositeName.setError("Field must not be empty.");
                    focusView = edtOppositeName;
                    cancelLogin = true;
                }
                if (TextUtils.isEmpty(strRContact)) {
                    edtRetainMobile.setError("Field must not be empty.");
                    focusView = edtRetainMobile;
                    cancelLogin = true;
                } else if (!isValidPhone((strRContact))) {
                    edtRetainMobile.setError("Mobile number must be of digits 10.");
                    focusView = edtRetainMobile;
                    cancelLogin = true;
                }
                if (TextUtils.isEmpty(strOCContact)) {
                    edtOppositeNumber.setError("Field must not be empty.");
                    focusView = edtOppositeNumber;
                    cancelLogin = true;
                } else if (!isValidPhone((strOCContact))) {
                    edtOppositeNumber.setError("Mobile number must be of digits 10.");
                    focusView = edtOppositeNumber;
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

    }


    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(AddCaseActivity.this, SelectDateActivity.class);
        startActivity(intent1);
        finish();
        SelectDateActivity.txtTitle.setText("Home");
        super.onBackPressed();

    }


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
                        Toast.makeText(AddCaseActivity.this, serverMessage, Toast.LENGTH_LONG).show();

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
        sprMonth.getSelectedView();
        sprMonth.setEnabled(false);
        sprSMonth.getSelectedView();
        sprSMonth.setEnabled(false);
        sprNMonth.getSelectedView();
        sprNMonth.setEnabled(false);
        sprYear.getSelectedView();
        sprYear.setEnabled(false);
        sprSYear.getSelectedView();
        sprSYear.setEnabled(false);
        sprNYear.getSelectedView();
        sprNYear.setEnabled(false);

        if (spinner.getId() == R.id.spinner_type) {
            if (i == 3) {
                edtCaseType.setVisibility(View.VISIBLE);
                edtCaseType.requestFocus();

            } else if (i == 0) {
                strType = "";
            } else {
                edtCourtName.requestFocus();
                edtCaseType.setVisibility(View.GONE);
                strType = sprCaseType.getSelectedItem().toString();

            }
        }
        if (spinner.getId() == R.id.spinner_day) {
            if (i == 0) {
                strDay = "";
            } else {
                strDay = sprDay.getSelectedItem().toString();
                sprMonth.setEnabled(true);

            }
        }
        if (spinner.getId() == R.id.spinner_month) {

            if (i == 0) {
                strMonth = "";
            } else {
                strMonth = String.valueOf(i);
                sprYear.setEnabled(true);
            }
        }
        if (spinner.getId() == R.id.spinner_year) {
            if (i == 0) {
                strYear = "";
            } else {
                if (txtPrevError.getVisibility() == View.VISIBLE) {
                    txtPrevError.setVisibility(View.GONE);
                }
                edtCaseTitle.requestFocus();
                strYear = sprYear.getSelectedItem().toString();


                if(!strMonth.equalsIgnoreCase("") && !strDay.equalsIgnoreCase(""))
                {
                    String prevDate = strYear + "-" + strMonth + "-" + strDay;
                    try {
                        Date date = dateFormatter2.parse(prevDate);
                        if(dt1!=null) {
                            if ((date.equals(dateFormatter2.parse(dateFormatter2.format(sysDate))) || (date.after(dt1)) &&
                                    date.before(dateFormatter2.parse(dateFormatter2.format(sysDate))))) {
                                txtPrevError.setVisibility(View.GONE);
                                strPreviousDt = dateFormatter2.format(date);
                            } else {
                                txtPrevError.setVisibility(View.VISIBLE);
                                txtPrevError.setText("Previous date should be equals to or after than start date");
                            }
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (spinner.getId() == R.id.spinner_nday) {
            if (i == 0) {
                strNDay = "";
            } else {
                strNDay = sprNDay.getSelectedItem().toString();
                sprNMonth.setEnabled(true);

            }
        }
        if (spinner.getId() == R.id.spinner_nmonth) {
            if (i == 0) {
                strNMonth = "";
            } else {
                strNMonth = String.valueOf(i);
                sprNYear.setEnabled(true);
            }
        }
        if (spinner.getId() == R.id.spinner_nyear) {
            if (i == 0) {
                strNYear = "";
            } else {
                if (txtNextError.getVisibility() == View.VISIBLE) {
                    txtNextError.setVisibility(View.GONE);
                }
                edtRetainName.requestFocus();
                strNYear = sprNYear.getSelectedItem().toString();

                if(!strNMonth.equalsIgnoreCase("") && !strNDay.equalsIgnoreCase("")) {
                    String nextDate = strNYear + "-" + strNMonth + "-" + strNDay;
                    try {
                        Date dt = dateFormatter2.parse(nextDate);
                        if(dt.after(dateFormatter2.parse(dateFormatter2.format(sysDate))) ||
                                dt.equals(dateFormatter2.parse(dateFormatter2.format(sysDate))))
                        {
                            strNextDt = dateFormatter2.format(dt);
                            txtNextError.setVisibility(View.GONE);
                        }
                        else
                        {
                            txtNextError.setVisibility(View.VISIBLE);
                            txtNextError.setText("Next date should be equals to or greater than current date");
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (spinner.getId() == R.id.spinner_sday) {
            if (i == 0) {
                strSDay = "";
            } else {
                strSDay = sprSDay.getSelectedItem().toString();
                sprSMonth.setEnabled(true);
            }
        }
        if (spinner.getId() == R.id.spinner_smonth) {
            if (i == 0) {
                strSMonth = "";
            } else {
                strSMonth = String.valueOf(i);
                sprSYear.setEnabled(true);
            }
        }
        if (spinner.getId() == R.id.spinner_syear) {
            if (i == 0)
            {
                strSYear = "";
            }
            else
            {

                if (txtStartError.getVisibility() == View.VISIBLE) {
                    txtStartError.setVisibility(View.GONE);
                }

                strSYear = sprSYear.getSelectedItem().toString();
                if(!strSMonth.equalsIgnoreCase("") && !strSDay.equalsIgnoreCase(""))
                {
                    String startDt = strSYear + "-" + strSMonth + "-" + strSDay;
                    try {
                         dt1 = dateFormatter2.parse(startDt);
                        if(dt1.before(dateFormatter2.parse(dateFormatter2.format(sysDate))) ||
                                dt1.equals(dateFormatter2.parse(dateFormatter2.format(sysDate))))
                        {
                            strStartDate = dateFormatter2.format(dt1);
                            txtStartError.setVisibility(View.GONE);
                        }
                        else
                        {
                            txtStartError.setVisibility(View.VISIBLE);
                            txtStartError.setText("Please select a date before current date");
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
      /*  if(spinner.getId()==R.id.spinner_time)
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

        }*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private boolean isValidPhone(String pass) {
        return pass != null && pass.length() == 10;
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
                Intent intent1 = new Intent(AddCaseActivity.this, SelectDateActivity.class);
                startActivity(intent1);
                finish();
            }
        });
        dialog.show();
    }
}
