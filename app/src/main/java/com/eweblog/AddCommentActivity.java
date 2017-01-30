package com.eweblog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

public class AddCommentActivity extends AppCompatActivity{

 /*   Spinner sprNDay, sprNMonth, sprNYear;*/
    Button btnAdd;
    EditText edtComments, edtNextDate;
    TextView txtNextError;
    ArrayAdapter<String> stringArrayAdapter;
    Prefshelper prefshelper;
    String strId, strNextDt, strComment,  strNDay, strNMonth, strNYear, nextTime;
    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    DateFormat dateFormatter2 = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
    LinearLayout linearLayout;
    Calendar cal = Calendar.getInstance();
    Date sysDate = cal.getTime();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_comment);
        prefshelper=new Prefshelper(AddCommentActivity.this);
   /*     sprNDay = (Spinner) findViewById(R.id.spinner_nday);
        sprNMonth = (Spinner) findViewById(R.id.spinner_nmonth);
        sprNYear = (Spinner) findViewById(R.id.spinner_nyear);*/
        btnAdd=(Button)findViewById(R.id.add);
        txtNextError=(TextView)findViewById(R.id.tw_next_error);
        edtComments=(EditText) findViewById(R.id.textView_comments);
        edtNextDate=(EditText) findViewById(R.id.textView_nextdt);
        edtNextDate.setHint(dateFormatter2.format(sysDate));
        linearLayout = (LinearLayout) findViewById(R.id.ll_navi);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(AddCommentActivity.this, CorporateUserMainActivity.class);
                startActivity(intent1);
                finish();
                CorporateUserMainActivity.txtTitle.setText("Home");
            }
        });
        strId=getIntent().getStringExtra("id");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                            View focusView = null;
                boolean cancelLogin = false;

                strComment=edtComments.getText().toString();
              
                if(strNextDt==null)
                {
                    txtNextError.setVisibility(View.VISIBLE);
                }
                else
                {
                    txtNextError.setVisibility(View.GONE);
                }

              
                if (TextUtils.isEmpty(strComment)) {
                    edtComments.setError("Field must not be empty.");
                    focusView = edtComments;
                    cancelLogin = true;
                }
               

                if (cancelLogin) {
                    // error in login
                    focusView.requestFocus();
                } else {

                    addComment();
                }


            }
        });
        edtNextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDatePicker();
            }
        });
      /*  if (sprNDay.getAdapter() == null) {
            stringArrayAdapter = new ArrayAdapter<String>(AddCommentActivity.this, R.layout.layout_spinner, getResources().getStringArray(R.array.day)) {

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
            stringArrayAdapter = new ArrayAdapter<String>(AddCommentActivity.this, R.layout.layout_spinner, getResources().getStringArray(R.array.month)) {

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
            stringArrayAdapter = new ArrayAdapter<String>(AddCommentActivity.this, R.layout.layout_spinner, getResources().getStringArray(R.array.year)) {

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
        sprNDay.setOnItemSelectedListener(AddCommentActivity.this);
        sprNMonth.setOnItemSelectedListener(AddCommentActivity.this);
        sprNYear.setOnItemSelectedListener(AddCommentActivity.this);
*/
    }
    public void addComment() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(AddCommentActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

            Log.e("", "SIGNUP " + MapAppConstant.API + "add_comment" );
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "add_comment", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Toast.makeText(AddCommentActivity.this, serverMessage,Toast.LENGTH_LONG).show();

                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {
                                if ("1".equals(serverCode)) {

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Intent intent1 = new Intent(AddCommentActivity.this, CorporateUserMainActivity.class);
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
                        Toast.makeText(AddCommentActivity.this, "Timeout Error",
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

                    params.put("user_id", Utils.getUserPreferences(AddCommentActivity.this,Prefshelper.USER_ID));
                    params.put("user_security_hash", Utils.getUserPreferences(AddCommentActivity.this,Prefshelper.USER_SECURITY_HASH));
                    params.put("case_id", strId);
                    params.put("case_detail_comment", strComment);
                    params.put("case_next_date", strNextDt);
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
    public void dialogDatePicker()
    {
        final Dialog dialog = new Dialog(AddCommentActivity.this);
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



                    strNextDt=year + "-" + strMonth + "-" + strDay;
                    try {
                        edtNextDate.setText(dateFormatter2.format(dateFormatter.parse(strNextDt)));
                        if(strNextDt==null)
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
                dialog.dismiss();
            }
        });



        dialog.show();
    }

  /*  @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;
       
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
                if(txtNextError.getVisibility()==View.VISIBLE)
                {
                    txtNextError.setVisibility(View.GONE);
                }
               
                strNYear= sprNYear.getSelectedItem().toString();
                Log.e("year", strNYear);
                String nextDate=strNYear+"-"+strNMonth+"-"+strNDay;

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

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
*/}
