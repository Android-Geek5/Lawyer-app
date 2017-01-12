package com.eweblog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
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
import com.eweblog.model.CaseListModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class AddCaseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    LinearLayout linearLayout, llCaseStatus;
    Spinner sprCaseType, sprCaseStaus ;

    ArrayAdapter<String> stringArrayAdapter;
    ArrayAdapter<CaseListModel> stringArrayAdapter2;
    Button btnAdd;
    EditText edtCourtName, edtCaseNumber, edtCaseTitle, edtCaseType, edtStatus, edtRetainName, edtRetainMobile,
            edtOppositeName, edtOppositeNumber, edtComments, edtStartDate, edtPrevDate, edtNextDate, edtClientContact;
    Prefshelper prefshelper;
    String strNumber, strTitle, strType, strCourt, strStatus, strPreviousDt, strNextDt, strOCName, strOCContact, strRName, strRContact,
            strComment, strStartDate, strDay, strMonth, strYear, strNDay, strNMonth, strNYear, strSDay, strSMonth, strSYear;
    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    DateFormat dateFormatter2 = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
    TextView txtStartError, txtPrevError, txtNextError;
    ConnectionDetector cd;
    Calendar cal = Calendar.getInstance();
    Date sysDate = cal.getTime();
    CheckBox chkSmsAlert;
    List<CaseListModel> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_add_case);
         prefshelper = new Prefshelper(AddCaseActivity.this);
        list= new ArrayList<>();
        sprCaseType = (Spinner) findViewById(R.id.spinner_type);
        sprCaseStaus = (Spinner) findViewById(R.id.spinner_case_status);
        btnAdd = (Button) findViewById(R.id.add);
        txtStartError = (TextView) findViewById(R.id.tw_start_error);
        txtPrevError = (TextView) findViewById(R.id.tw_prev_error);
        txtNextError = (TextView) findViewById(R.id.tw_next_error);
        linearLayout = (LinearLayout) findViewById(R.id.ll_navi);
        llCaseStatus = (LinearLayout) findViewById(R.id.ll_case_status);
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
        edtStartDate = (EditText) findViewById(R.id.textView_startdt);
        edtPrevDate = (EditText) findViewById(R.id.textView_prevdt);
        edtNextDate = (EditText) findViewById(R.id.textView_nextdt);
        edtClientContact = (EditText) findViewById(R.id.textView_clientnumber);
        chkSmsAlert=(CheckBox)findViewById(R.id.checkedTextView);
        edtNextDate.setHint(dateFormatter2.format(sysDate));
        edtPrevDate.setHint(dateFormatter2.format(sysDate));
        edtStartDate.setHint(dateFormatter2.format(sysDate));
        edtStartDate.setOnClickListener(this);
        edtPrevDate.setOnClickListener(this);
        edtNextDate.setOnClickListener(this);
        if(prefshelper.getCorporateUser().equalsIgnoreCase("1"))
        {
            llCaseStatus.setVisibility(View.VISIBLE);
            edtStatus.setVisibility(View.GONE);
        }
        else
        {
            llCaseStatus.setVisibility(View.GONE);
            edtStatus.setVisibility(View.VISIBLE);
        }
        chkSmsAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked())
                {
                  edtClientContact.setEnabled(true);
                }
                else
                {
                    edtClientContact.setEnabled(false);
                }
            }
        });
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

                if(strStartDate==null)
                {
                    strStartDate=dateFormatter.format(sysDate);
                    Log.d("start date", strStartDate);
                }
                if (edtCaseType.getVisibility() == View.VISIBLE)
                {
                    strType = edtCaseType.getText().toString();
                }

                if (strPreviousDt==null)
                {
                    txtPrevError.setVisibility(View.VISIBLE);
                }
                else
                {
                    txtPrevError.setVisibility(View.GONE);
                }
                if (strNextDt==null)
                {
                    txtNextError.setVisibility(View.VISIBLE);
                }
                else
                {
                    txtNextError.setVisibility(View.GONE);
                }

                if (TextUtils.isEmpty(strTitle)) {
                    edtCaseTitle.setError("Field must not be empty.");
                    focusView = edtCaseTitle;
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

                    return position != 0;
                }

                @Override
                public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
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


        sprCaseType.setOnItemSelectedListener(AddCaseActivity.this);
        sprCaseStaus.setOnItemSelectedListener(AddCaseActivity.this);
        getCaseStatuses();
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
            pDialog.setCancelable(false);
            pDialog.show();

            Log.e("", "SIGNUP " + MapAppConstant.API + "add_case");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "add_case", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response);

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");


                        if (serverCode.equalsIgnoreCase("0")) {
                            Toast.makeText(AddCaseActivity.this, serverMessage, Toast.LENGTH_LONG).show();
                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {
                                if ("1".equals(serverCode)) {
                                    Toast.makeText(AddCaseActivity.this, serverMessage, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(AddCaseActivity.this, "No Internet Connection",
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
                    Map<String, String> params = new HashMap<>();
                   Log.e(strPreviousDt, strNextDt);
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
                    params.put("case_detail_comment", strComment);
                    params.put("case_start_date", strStartDate);
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

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void getCaseStatuses() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(AddCaseActivity.this);
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
                        Toast.makeText(AddCaseActivity.this, serverMessage,Toast.LENGTH_LONG).show();
                        Log.e("error", response);
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {
                                if ("1".equals(serverCode)) {
                                    JSONArray jsonArray=object.getJSONArray("data");
                                    if(jsonArray.length()>0)
                                    {
                                        for(int i=0; i<jsonArray.length();i++)
                                        {
                                           JSONObject jsonObject=jsonArray.getJSONObject(i);
                                            String id=jsonObject.getString("case_status_id");
                                            String name=jsonObject.getString("case_status_name");
                                            list.add(model(id, name));
                                        }
                                    }
                                    setlist(list);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (sprCaseStaus.getAdapter() == null) {
                                stringArrayAdapter2 = new ArrayAdapter<CaseListModel>(AddCaseActivity.this, R.layout.layout_spinner, list)
                                {
                                    @Override
                                    public boolean isEnabled(int position) {

                                        return position != 0;
                                    }

                                    @Override
                                    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
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
                                stringArrayAdapter2.setDropDownViewResource(R.layout.layout_spinner_dropdown);
                                sprCaseStaus.setAdapter(stringArrayAdapter2);
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
                        Toast.makeText(AddCaseActivity.this, "No Internet Connection",
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
    public List<CaseListModel> getlist() {
        return list;
    }

    public void setlist(List<CaseListModel> list) {
        this.list = list;
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
    public void dialogDatePicker()
    {
        final Dialog dialog = new Dialog(AddCaseActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_datepicker);
        final DatePicker simpleDatePicker = (DatePicker)dialog.findViewById(R.id.datePicker);
        final Button btnOk=(Button)dialog.findViewById(R.id.bt_yes);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int day = simpleDatePicker.getDayOfMonth();
                int month = simpleDatePicker.getMonth() + 1;
                int year =  simpleDatePicker.getYear();
                String strDay, strMonth;
                if(day<=9)
                {
                    strDay="0"+day;
                }
                else
                {
                    strDay= String.valueOf(day);
                }

                if(month<=9)
                {
                    strMonth="0"+month;
                }
                else
                {
                    strMonth= String.valueOf(month);
                }

                if(prefshelper.getDate().equalsIgnoreCase("start")) {
                    dialog.dismiss();

                    strStartDate=year + "-" + strMonth + "-" + strDay;
                    try {
                        edtStartDate.setText(dateFormatter2.format(dateFormatter.parse(strStartDate)));
                        edtStartDate.setFocusable(false);
                        edtPrevDate.setFocusable(true);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else  if(prefshelper.getDate().equalsIgnoreCase("previous"))
                {
                    dialog.dismiss();
                    strPreviousDt=year + "-" + strMonth + "-" + strDay;
                    try {
                         edtPrevDate.setText(dateFormatter2.format(dateFormatter.parse(strPreviousDt)));
                         edtCaseTitle.setFocusable(true);
                        if (strPreviousDt==null)
                        {
                            txtPrevError.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            txtPrevError.setVisibility(View.GONE);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else  if(prefshelper.getDate().equalsIgnoreCase("next"))
                {

                    dialog.dismiss();
                    strNextDt=year + "-" + strMonth + "-" + strDay;
                    try {
                        edtNextDate.setText(dateFormatter2.format(dateFormatter.parse(strNextDt)));
                        if (strNextDt==null)
                        {
                            txtNextError.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            txtNextError.setVisibility(View.GONE);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    dialog.show();
}

    @Override
    public void onClick(View view) {

        if(view==edtPrevDate)
        {
            prefshelper.storeDate("previous");
           dialogDatePicker();
        }
        if(view==edtNextDate)
        {
            prefshelper.storeDate("next");
            dialogDatePicker();
        }
        if(view==edtStartDate)
        {
            prefshelper.storeDate("start");
            dialogDatePicker();
        }

    }
    private CaseListModel model( String id,String name) {
        CaseListModel model = new CaseListModel();
        model.setCaseId(id);
        model.setNextDate(name);

        return model;
    }
}
