package com.eweblog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
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
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class CaseDetailActivity extends AppCompatActivity {
    LinearLayout linearLayout, linearLayoutMain;
    Button btnAdd;
    FloatingActionButton fab;
    Prefshelper prefshelper;
    TextView txtCaseNumber, txtCaseTitle, txtCaseType, txtCourtName, txtStatus,  txtRetain, txtOpposite, txtCName, txtCContact
            ,txtComment, txtRName, txtRContact, txtStartDt;
    String caseId,caseNumber, caseTitle, courtName,status,startDate, counsellorName, counsellorContact, retainedName ,retainedContact
            ,caseType;
    LinearLayout llCaseNumber, llCaseTitle, llCaseType, llCourtName, llStatus, llCName, llCContact, llRName, llRcontact,llStartDt;
    List<CaseListModel> caseArray;
    ConnectionDetector cd;
    List<CaseListModel> caseListModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_case_detail);
        cd=new ConnectionDetector(CaseDetailActivity.this);
        linearLayout=(LinearLayout)findViewById(R.id.ll_navi);
        linearLayoutMain=(LinearLayout)findViewById(R.id.ll_main);
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


        txtCaseNumber=(TextView)findViewById(R.id.textView_number);
        txtCaseTitle=(TextView)findViewById(R.id.textView_title);
        txtCaseType=(TextView)findViewById(R.id.textView_type);
        txtCourtName=(TextView)findViewById(R.id.textView_name);
        txtStatus=(TextView)findViewById(R.id.textView_position);
        txtRetain=(TextView)findViewById(R.id.textView_retain);
        txtOpposite=(TextView)findViewById(R.id.textView_opposite);
        txtCName=(TextView)findViewById(R.id.textView_cname);
        txtCContact=(TextView)findViewById(R.id.textView_cContact);

        txtRName=(TextView)findViewById(R.id.textView_retainNm);
        txtRContact=(TextView)findViewById(R.id.textView_retainContact);
        txtStartDt=(TextView)findViewById(R.id.textView_start);
        llCaseNumber=(LinearLayout)findViewById(R.id.ll_number);
        llCaseTitle=(LinearLayout)findViewById(R.id.ll_title);
        llCaseType=(LinearLayout)findViewById(R.id.ll_type);
        llStartDt=(LinearLayout)findViewById(R.id.ll_startdt);
        llStatus=(LinearLayout)findViewById(R.id.ll_status);
        llRName=(LinearLayout)findViewById(R.id.ll_retain_nm);
        llRcontact=(LinearLayout)findViewById(R.id.ll_retain_contact);
        llCName=(LinearLayout)findViewById(R.id.ll_oname);
        llCContact=(LinearLayout)findViewById(R.id.ll_ocontact);
        llCourtName=(LinearLayout)findViewById(R.id.ll_name);

        caseArray= (List<CaseListModel>) getIntent().getSerializableExtra("list1");


        if(caseNumber.equalsIgnoreCase(""))
        {
            llCaseNumber.setVisibility(View.GONE);
        }
        else
        {
            txtCaseNumber.setText(caseNumber);
        }

        if(startDate.equalsIgnoreCase(""))
        {
            llStartDt.setVisibility(View.GONE);
        }
        else
        {
            txtStartDt.setText(startDate);
        }

        if(caseTitle.equalsIgnoreCase(""))
        {
            llCaseTitle.setVisibility(View.GONE);
        }
        else
        {
            txtCaseTitle.setText(caseTitle);
        }

        if(caseType.equalsIgnoreCase(""))
        {
            llCaseType.setVisibility(View.GONE);
        }
        else
        {
            txtCaseType.setText(caseType);
        }

        if(courtName.equalsIgnoreCase(""))
        {
            llCourtName.setVisibility(View.GONE);
        }
        else
        {
            txtCourtName.setText(courtName);
        }

        if(status.equalsIgnoreCase(""))
        {
            llStatus.setVisibility(View.GONE);
        }
        else
        {
            txtStatus.setText(status);
        }

        if(counsellorName.equalsIgnoreCase(""))
        {
            llCName.setVisibility(View.GONE);
        }
        else
        {
            txtCName.setText(counsellorName);
        }

        if(counsellorContact.equalsIgnoreCase(""))
        {
            llCContact.setVisibility(View.GONE);
        }
        else
        {
            txtCContact.setText(counsellorContact);
        }

        if(counsellorName.equalsIgnoreCase("") && counsellorContact.equalsIgnoreCase(""))
        {
            txtOpposite.setVisibility(View.GONE);
        }

        if(retainedName.equalsIgnoreCase("") && retainedContact.equalsIgnoreCase(""))
        {
            txtRetain.setVisibility(View.GONE);
        }

        if(retainedName.equalsIgnoreCase(""))
        {
            llRName.setVisibility(View.GONE);
        }
        else
        {
            txtRName.setText(retainedName);
        }

        if(retainedContact.equalsIgnoreCase(""))
        {
            llRcontact.setVisibility(View.GONE);
        }
        else
        {
            txtRContact.setText(retainedContact);
        }

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
        if(cd.isConnectingToInternet()) {
            if (caseArray != null) {
                Log.e(caseArray.size() + "", "sizeeeeee");
                for (int i = 0; i < caseArray.size(); i++) {

                    if (caseArray.get(i).getCaseId().equalsIgnoreCase(caseId)) {
                        Log.e(caseArray.get(i).getCaseId() + "", caseId);

                        LinearLayout llv = new LinearLayout(this);
                        llv.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout ll = new LinearLayout(this);
                        ll.setOrientation(LinearLayout.HORIZONTAL);
                        // Create TextView
                        TextView txtPrevious = new TextView(this);
                        txtPrevious.setTextSize(16);
                        txtPrevious.setPadding(16, 16, 0, 16);
                        txtPrevious.setTextColor(Color.BLACK);
                        txtPrevious.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                        txtPrevious.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                        ll.addView(txtPrevious);
                        // Create TextView
                        TextView txtComment = new TextView(this);
                        txtComment.setTextSize(16);
                        txtComment.setPadding(16, 16, 0, 16);
                        txtComment.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                        txtComment.setTextColor(Color.BLACK);
                        txtComment.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                        ll.addView(txtComment);
                        llv.addView(ll);
                        LinearLayout lLayout = new LinearLayout(this);
                        lLayout.setBackgroundColor(Color.BLACK);
                        lLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                4));
                        llv.addView(lLayout);
                        String date = caseArray.get(i).getNextDate();
                        String comment = caseArray.get(i).getComment();
                        txtPrevious.setText(date);
                        txtComment.setText(comment);
                        linearLayoutMain.addView(llv);
                    }

                }

            }
        }
        else {
            if (caseArray != null) {
                Log.e(caseArray.size() + "", "sizeeeeee");
                for (int i = 0; i <Math.sqrt(caseArray.size()); i++) {


                        LinearLayout llv = new LinearLayout(this);
                        llv.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout ll = new LinearLayout(this);
                        ll.setOrientation(LinearLayout.HORIZONTAL);
                        // Create TextView
                        TextView txtPrevious = new TextView(this);
                        txtPrevious.setTextSize(16);
                        txtPrevious.setPadding(16, 16, 0, 16);
                        txtPrevious.setTextColor(Color.BLACK);
                        txtPrevious.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                        txtPrevious.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                        ll.addView(txtPrevious);
                        // Create TextView
                        TextView txtComment = new TextView(this);
                        txtComment.setTextSize(16);
                        txtComment.setPadding(16, 16, 0, 16);
                        txtComment.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                        txtComment.setTextColor(Color.BLACK);
                        txtComment.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                        ll.addView(txtComment);
                        llv.addView(ll);
                        LinearLayout lLayout = new LinearLayout(this);
                        lLayout.setBackgroundColor(Color.BLACK);
                        lLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                4));
                        llv.addView(lLayout);
                        String date = caseArray.get(i).getNextDate();
                        String comment = caseArray.get(i).getComment();
                        txtPrevious.setText(date);
                        txtComment.setText(comment);
                        linearLayoutMain.addView(llv);

                    }

               /* List<CaseListModel> uniques = new ArrayList<>();
                for (CaseListModel element : caseListModels)
                {
                    if (!uniques.contains(element)) {
                        uniques.add(element);
                    }
                }


                    if(uniques.size()>0)
                    {
                        for(int j=0; j<uniques.size(); j++)
                        {
                            LinearLayout llv = new LinearLayout(this);
                            llv.setOrientation(LinearLayout.VERTICAL);
                            LinearLayout ll = new LinearLayout(this);
                            ll.setOrientation(LinearLayout.HORIZONTAL);
                            // Create TextView
                            TextView txtPrevious = new TextView(this);
                            txtPrevious.setTextSize(16);
                            txtPrevious.setPadding(16, 16, 0, 16);
                            txtPrevious.setTextColor(Color.BLACK);
                            txtPrevious.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            txtPrevious.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                            ll.addView(txtPrevious);
                            // Create TextView
                            TextView txtComment = new TextView(this);
                            txtComment.setTextSize(16);
                            txtComment.setPadding(16, 16, 0, 16);
                            txtComment.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            txtComment.setTextColor(Color.BLACK);
                            txtComment.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                            ll.addView(txtComment);
                            llv.addView(ll);
                            LinearLayout lLayout = new LinearLayout(this);
                            lLayout.setBackgroundColor(Color.BLACK);
                            lLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    4));
                            llv.addView(lLayout);
                            String date = uniques.get(j).getNextDate();
                            String comment = uniques.get(j).getComment();
                            txtPrevious.setText(date);
                            txtComment.setText(comment);
                            linearLayoutMain.addView(llv);
                        }



                }
*/


            }
        }

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
            pDialog.setCancelable(false);
            pDialog.show();

            Log.e("", "SIGNUP " + MapAppConstant.API + "send_case_details_email");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "send_case_details_email", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response);

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
                        Toast.makeText(CaseDetailActivity.this, "No Internet Connection",
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
