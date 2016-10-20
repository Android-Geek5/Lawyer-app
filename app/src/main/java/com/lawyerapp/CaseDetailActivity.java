package com.lawyerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
import com.lawyerapp.common.MapAppConstant;
import com.lawyerapp.common.Prefshelper;
import com.lawyerapp.common.VolleySingleton;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CaseDetailActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    Button btnAdd;
    FloatingActionButton fab;
    Prefshelper prefshelper;
    TextView txtCaseNumber, txtCaseTitle, txtCaseType, txtCourtName, txtStatus, txtPrevDate, txtNextDt, txtCName, txtCContact
            ,txtComment, txtRName, txtRContact, txtStartDt;
    String caseId, caseNumber, caseTitle, courtName, status,prevDate, nextDate,startDate, counsellorName, counsellorContact,
    comment, retainedName, retainedContact, caseType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_case_detail);
        linearLayout=(LinearLayout)findViewById(R.id.ll_navi);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        fab = (FloatingActionButton)findViewById(R.id.fab);
        prefshelper=new Prefshelper(this);
        btnAdd=(Button)findViewById(R.id.add);
        caseId=getIntent().getStringExtra("id");
        caseNumber=getIntent().getStringExtra("cnumber");
        caseType=getIntent().getStringExtra("ctype");
        caseTitle=getIntent().getStringExtra("ctitle");
        courtName=getIntent().getStringExtra("court");
        status=getIntent().getStringExtra("status");
      //  prevDate=getIntent().getStringExtra("pdate");
      //  nextDate=getIntent().getStringExtra("ndate");
        startDate=getIntent().getStringExtra("sdate");
        counsellorName=getIntent().getStringExtra("oname");
        counsellorContact=getIntent().getStringExtra("ocontact");
        comment=getIntent().getStringExtra("comment");
        retainedName=getIntent().getStringExtra("rname");
        retainedContact=getIntent().getStringExtra("rcontact");

        txtCaseNumber=(TextView)findViewById(R.id.textView_number);
        txtCaseTitle=(TextView)findViewById(R.id.textView_title);
        txtCaseType=(TextView)findViewById(R.id.textView_type);
        txtCourtName=(TextView)findViewById(R.id.textView_name);
        txtStatus=(TextView)findViewById(R.id.textView_position);
        txtPrevDate=(TextView)findViewById(R.id.textView_pdate);
        txtNextDt=(TextView)findViewById(R.id.textView_duedate);
        txtCName=(TextView)findViewById(R.id.textView_cname);
        txtCContact=(TextView)findViewById(R.id.textView_cContact);
        txtComment=(TextView)findViewById(R.id.textView_comments);
        txtRName=(TextView)findViewById(R.id.textView_retainNm);
        txtRContact=(TextView)findViewById(R.id.textView_retainContact);
        txtStartDt=(TextView)findViewById(R.id.textView_start);
        try {
            // obtain date and time from initial string
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT).parse(prevDate);
            // set date string
            String stringDate = new SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(date).toUpperCase(Locale.ROOT);
            // set time string
           // txtPrevDate.setText(stringDate);
        } catch (ParseException e) {
            // wrong input
        }
        try {
            // obtain date and time from initial string
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT).parse(nextDate);
            // set date string
            String stringDate = new SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(date).toUpperCase(Locale.ROOT);
            // set time string
        //   txtNextDt.setText(stringDate);
        } catch (ParseException e) {
            // wrong input
        }
        try {
            // obtain date and time from initial string
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT).parse(startDate);
            // set date string
            String stringDate = new SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(date).toUpperCase(Locale.ROOT);
            // set time string
            txtStartDt.setText(stringDate);
        } catch (ParseException e) {
            // wrong input
        }
        txtCaseNumber.setText(caseNumber);
        txtCaseTitle.setText(caseTitle);
        txtCaseType.setText(caseType);
        txtCourtName.setText(courtName);
        txtStatus.setText(status);
        txtCName.setText(counsellorName);
        txtCContact.setText(counsellorContact);
        txtComment.setText(comment);
        txtRName.setText(retainedName);
        txtRContact.setText(retainedContact);


        fab.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor("#00bcd5")));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                  sendCaseDetail();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CaseDetailActivity.this, AddCommentActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void sendCaseDetail() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(CaseDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "SIGNUP " + MapAppConstant.API + "send_case_details_email");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "send_case_details_email", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Toast.makeText(CaseDetailActivity.this, serverMessage,Toast.LENGTH_LONG).show();

                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {
                                if ("1".equals(serverCode)) {


                                }
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
                        Toast.makeText(CaseDetailActivity.this, "Timeout Error",
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
                    params.put("case_id", caseId);
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
