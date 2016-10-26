package com.eweblog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.eweblog.common.ConnectionDetector;
import com.eweblog.common.MapAppConstant;
import com.eweblog.common.Prefshelper;
import com.eweblog.common.VolleySingleton;
import com.eweblog.model.CaseListModel;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CaseDetailActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    Button btnAdd;
    FloatingActionButton fab;
    Prefshelper prefshelper;
    TextView txtCaseNumber, txtCaseTitle, txtCaseType, txtCourtName, txtStatus, txtPrevDate, txtNextDt, txtCName, txtCContact
            ,txtComment, txtRName, txtRContact, txtStartDt;
    String caseId;
    String caseNumber;
    String caseTitle;
    String courtName;
    String status;

    String startDate;
    String counsellorName;
    String counsellorContact;

    String retainedName;
    String retainedContact;
    String caseType;
    List<CaseListModel> caseArray;
    ConnectionDetector cd;
    String prevDate="";
    String nextDate="";
    String c="";
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
        startDate=getIntent().getStringExtra("sdate");
        counsellorName=getIntent().getStringExtra("oname");
        counsellorContact=getIntent().getStringExtra("ocontact");
        retainedName=getIntent().getStringExtra("rname");
        retainedContact=getIntent().getStringExtra("rcontact");
        caseArray= (List<CaseListModel>) getIntent().getSerializableExtra("list1");
        Log.e("araaysd",  getIntent().getSerializableExtra("list1")+"");
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
        cd=new ConnectionDetector(CaseDetailActivity.this);

        fab.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor("#00bcd5")));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cd.isConnectingToInternet())
                {
                    dialog();

                }
                else
                {
                    sendCaseDetail();
                }

            }
        });
        if(caseArray!=null) {
            for (int j = 0; j < caseArray.size(); j++) {
                if(caseArray.get(j).getCaseId().equalsIgnoreCase(caseId)) {
                    try {
                        // obtain date and time from initial string
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT).parse(caseArray.get(j).getCasePrevDate());
                        // set date string

                        String stringDate = new SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(date).toUpperCase(Locale.ROOT);
                        // set time string
                        prevDate = prevDate + stringDate + "\n";

                    } catch (ParseException e) {
                        // wrong input
                    }
                    txtPrevDate.setText(prevDate);
                }
            }

            for (int j = 0; j < caseArray.size(); j++) {

                if(caseArray.get(j).getCaseId().equalsIgnoreCase(caseId)) {
                    try {
                        // obtain date and time from initial string
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT).parse((caseArray.get(j).getNextDate()));
                        // set date string

                        String stringDate = new SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(date).toUpperCase(Locale.ROOT);
                        // set time string
                        nextDate = nextDate + stringDate + "\n";

                    } catch (ParseException e) {
                        // wrong input
                    }
                    txtNextDt.setText(nextDate);
                }
            }

            for(int i=0; i<caseArray.size();i++)
            {
                if(caseArray.get(i).getCaseId().equalsIgnoreCase(caseId))
                {
                    c = c + caseArray.get(i).getComment() + "\n";
                }

            }
            txtComment.setText(c);
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

        txtRName.setText(retainedName);
        txtRContact.setText(retainedContact);



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cd.isConnectingToInternet())
                {
                    dialog();

                }
                else
                {
                    Intent intent=new Intent(CaseDetailActivity.this, AddCommentActivity.class);
                    intent.putExtra("id", caseId);
                    startActivity(intent);
                    finish();
                }

            }
        });
        if(status.contains("argument"))
        {
           txtStatus.setText(status);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
               txtStatus.setTextColor(getResources().getColor(R.color.argu, null));
            }
            else
            {
               txtStatus.setTextColor(getResources().getColor(R.color.argu));
            }
        }
        else if(status.equalsIgnoreCase("reply"))
        {
           txtStatus.setText(status);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
               txtStatus.setTextColor(getResources().getColor(R.color.reply, null));
            }
            else
            {
               txtStatus.setTextColor(getResources().getColor(R.color.reply));
            }
        }
        else if(status.equalsIgnoreCase("cross"))
        {
           txtStatus.setText(status);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
               txtStatus.setTextColor(getResources().getColor(R.color.cross, null));
            }
            else
            {
               txtStatus.setTextColor(getResources().getColor(R.color.cross));
            }
        }
        else if(status.equalsIgnoreCase("consideration"))
        {
           txtStatus.setText(status);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
               txtStatus.setTextColor(getResources().getColor(R.color.consider, null));
            }
            else
            {
               txtStatus.setTextColor(getResources().getColor(R.color.consider));
            }
        }
        else
        {
           txtStatus.setText(status);
        }
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
}
