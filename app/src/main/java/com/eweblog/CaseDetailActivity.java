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
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.eweblog.common.UserInfo;
import com.eweblog.common.ConnectionDetector;
import com.eweblog.common.MapAppConstant;
import com.eweblog.common.VolleySingleton;
import com.eweblog.model.CommentModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class CaseDetailActivity extends AppCompatActivity {
    LinearLayout linearLayout, linearLayoutMain;
    Button btnAdd;
    FloatingActionButton fab;
    TextView txtCaseNumber, txtCaseTitle, txtCaseType, txtCourtName, txtStatus,  txtRetain, txtOpposite, txtCName, txtCContact
            , txtRName, txtRContact, txtStartDt,txtAssignedTo;
    String caseId,caseNumber, caseTitle, courtName,status,startDate, counsellorName, counsellorContact, retainedName ,retainedContact
            ,caseType,assignedTo;
    LinearLayout llCaseNumber, llCaseTitle, llCaseType, llCourtName, llStatus, llCName, llCContact, llRName, llRcontact,llStartDt,llAssignedTo;
    ConnectionDetector cd;
    ScrollView mainLayout;
    List<CommentModel> commentList=new ArrayList<>();
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
              //  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_case_detail);
        cd=new ConnectionDetector(CaseDetailActivity.this);
        inflateLayout();
        getIntentInformation();
        checkInternetCheck();
        setFabButton();
    }
    public void inflateLayout()
    {
        linearLayout=(LinearLayout)findViewById(R.id.ll_navi);
        linearLayoutMain=(LinearLayout)findViewById(R.id.ll_main);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        fab = (FloatingActionButton)findViewById(R.id.fab);

        btnAdd=(Button)findViewById(R.id.add);
        mainLayout=(ScrollView) findViewById(R.id.main_layout);
        txtCaseNumber=(TextView)findViewById(R.id.textView_number);
        txtCaseTitle=(TextView)findViewById(R.id.textView_title);
        txtCaseType=(TextView)findViewById(R.id.textView_type);
        txtCourtName=(TextView)findViewById(R.id.textView_name);
        txtStatus=(TextView)findViewById(R.id.textView_position);
        txtRetain=(TextView)findViewById(R.id.textView_retain);
        txtOpposite=(TextView)findViewById(R.id.textView_opposite);
        txtCName=(TextView)findViewById(R.id.textView_cname);
        txtCContact=(TextView)findViewById(R.id.textView_cContact);
        txtAssignedTo=(TextView) findViewById(R.id.textView_assigned);

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
        llAssignedTo=(LinearLayout) findViewById(R.id.ll_assigned_to);

    }
    /** Show dialog if no internet**/
    public void checkInternetCheck() {
        if(cd.isConnectingToInternet())
        {
            getCaseDetail();
            fab.setVisibility(View.VISIBLE);
        }
        else
        {
            mainLayout.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            dialog();
        }
    }
    /** Send case detail through email **/
    public void sendCaseDetail() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(CaseDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String SEND_CASE_DETAILS_EMAIL=MapAppConstant.API + MapAppConstant.SEND_DETAILS_BY_EMAIL;
            Log.e("SEND CASE DETAILS URL", SEND_CASE_DETAILS_EMAIL );
            StringRequest sr = new StringRequest(Request.Method.POST,SEND_CASE_DETAILS_EMAIL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("SEND CASE DETAILS RESP", response);


                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Utils.showToast(CaseDetailActivity.this, serverMessage);

                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
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
                    params.put(UserInfo.USER_ID, Utils.getUserPreferences(CaseDetailActivity.this, UserInfo.USER_ID));
                    params.put(UserInfo.USER_SECURITY_HASH, Utils.getUserPreferences(CaseDetailActivity.this, UserInfo.USER_SECURITY_HASH));
                    params.put(UserInfo.CASE_ID, caseId);
                    Log.e("SEND CASE DETAILS REQ",params.toString());
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
    /** Dialog for no internet connection **/
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
    /** get case id from intent extras **/
    public void getIntentInformation()
    {
        caseId=getIntent().getStringExtra("id");

    }
    /*Set information after getting case details **/
    public void setIntentInformation()
    {

        mainLayout.setVisibility(View.VISIBLE);
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
        // Show assigned to field only for corporate users
         if(Utils.getUserPreferencesBoolean(CaseDetailActivity.this, UserInfo.CORPORATE_OR_NOT))
         {
             llAssignedTo.setVisibility(View.VISIBLE);
             txtAssignedTo.setText(assignedTo);
         }
        else
         {
             llAssignedTo.setVisibility(View.GONE);
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

    }

    public void setFabButton()
    {
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
    }
    /* Programmatically create comment layout **/
    public void setComment(List<CommentModel> caseArray)
    {
        for (int i = 0; i <caseArray.size(); i++) {

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
            String date = caseArray.get(i).getDate();
            String comment = caseArray.get(i).getDetails();
            txtPrevious.setText(date);
            txtComment.setText(comment);
            linearLayoutMain.addView(llv);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CaseDetailActivity.this.finish();
    }
    /** Get case details **/
    public void getCaseDetail()
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(CaseDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String URL_VIEW_CASE=MapAppConstant.API + MapAppConstant.VIEW_CASE;

            Log.e("VIEW CASE URL",URL_VIEW_CASE);
            StringRequest sr = new StringRequest(Request.Method.POST,URL_VIEW_CASE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("VIEW CASE RESPONSE", response);

                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                         Utils.showToast(CaseDetailActivity.this,serverMessage);
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {

                            JSONObject jsonObject=object.getJSONObject("data");
                            caseNumber=jsonObject.getString(UserInfo.CASE_NUMBER);
                            caseType=jsonObject.getString(UserInfo.CASE_TYPE);
                            caseTitle=jsonObject.getString(UserInfo.CASE_TITLE);
                            courtName=jsonObject.getString(UserInfo.COURT_NAME);
                            // Get case statuses id only if paid user
                            if(Utils.getUserPreferencesBoolean(CaseDetailActivity.this, UserInfo.COMMON_PAID))
                            {
                                int caseStatusesId=jsonObject.getInt(UserInfo.CASE_STATUSES_ID);
                                // If foreign key is greater than 0,the only fetch the information from status table
                                if(caseStatusesId>0)
                                    status=jsonObject.getString(UserInfo.CASE_STATUS_NAME);
                                else
                                    status="";
                            }
                            else // Get simply the position of status for free users
                            {
                                status = jsonObject.getString(UserInfo.CASE_POSITION_STATUS);
                            }
                            startDate=jsonObject.getString(UserInfo.CASE_START_DATE);
                            counsellorName=jsonObject.getString(UserInfo.CASE_OPPOSITE_COUNSELLOR_NAME);
                            counsellorContact=jsonObject.getString(UserInfo.CASE_OPPOSITE_COUNSELLOR_CONTACT);
                            retainedName=jsonObject.getString(UserInfo.CASE_RETAINED_NAME);
                            retainedContact=jsonObject.getString(UserInfo.CASE_RETAINED_CONTACT);
                            // Get assigned to me for business user
                            if(Utils.getUserPreferencesBoolean(CaseDetailActivity.this, UserInfo.CORPORATE_OR_NOT))
                            {
                                // Check the assigned person on the basis of assigned id.
                                // If user id is different,then fetch parent name and last name
                                int userId=jsonObject.getInt(UserInfo.USERS_ID);
                                int assignedBy=jsonObject.getInt(UserInfo.CASE_ASSIGNED_BY);
                                if(userId==assignedBy)
                                {
                                    assignedTo="Me";
                                }
                                else
                                {
                                    assignedTo=jsonObject.getString(UserInfo.USER_NAME)+" "+jsonObject.getString(UserInfo.USER_LAST_NAME);
                                }

                            }

                            String currentDate=jsonObject.getString(UserInfo.CURRENT_DATE);
                            String lastHearingDate=jsonObject.getString(UserInfo.CASE_LATEST_HEARING_DATE);
                            /* Show more comments only if Current date is equal to greater than Last Hearing date **/
                            if(dateFormatForDisplaying.parse(currentDate).before(dateFormatForDisplaying.parse(lastHearingDate)))
                            {
                                btnAdd.setVisibility(View.GONE);
                            }
                            else
                            {
                                btnAdd.setVisibility(View.VISIBLE);
                                btnAdd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent=new Intent(CaseDetailActivity.this, AddCommentActivity.class);
                                        intent.putExtra("id", caseId);
                                        startActivity(intent);
                                        finish();

                                    }
                                });
                            }
                            /* Fetch comments **/
                            JSONArray jsonArray=jsonObject.getJSONArray(UserInfo.CASE_DETAILS);
                            if(jsonArray.length()>0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    CommentModel commentModel=new CommentModel();
                                    JSONObject jsonObject2=jsonArray.getJSONObject(i);
                                    if(i==0 && Utils.getUserPreferencesBoolean(CaseDetailActivity.this, UserInfo.COMMON_PAID))
                                    {
                                        status=jsonObject2.getString(UserInfo.CASE_STATUS_NAME);
                                    }
                                    Date date1 = dateFormatForDisplaying.parse(jsonObject2.getString(UserInfo.CASE_DETAIL_HEARING_DATE));
                                    commentModel.setDate(dateFormat.format(date1));
                                    commentModel.setDetails(jsonObject2.getString(UserInfo.CASE_DETAIL_COMMENT));
                                    commentList.add(commentModel);
                                }
                               // if(Utils.getUserPreferencesBoolean(CaseDetailActivity.this,UserInfo.COMMON_PAID))
                                    getCaseStatuses(status);
                               // else
                               // setIntentInformation();
                                setComment(commentList);
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
                        Utils.showToast(CaseDetailActivity.this, "No Internet Connection");
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
                    params.put(UserInfo.USER_ID, Utils.getUserPreferences(CaseDetailActivity.this, UserInfo.USER_ID));
                    params.put(UserInfo.USER_SECURITY_HASH, Utils.getUserPreferences(CaseDetailActivity.this, UserInfo.USER_SECURITY_HASH));
                    params.put(UserInfo.CASE_ID, caseId);
                    Log.e("VIEW CASE REQUEST",params.toString());
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
    /*Get case status for Paid users to change the colour of status**/
    public void getCaseStatuses(final String status) {
        try {
            final ProgressDialog pDialog = new ProgressDialog(CaseDetailActivity.this);
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

                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {
                                if ("1".equals(serverCode)) {
                                    JSONArray jsonArray = object.getJSONArray("data");

                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String id = jsonObject.getString(UserInfo.CASE_STATUS_ID);
                                            String name = jsonObject.getString(UserInfo.CASE_STATUS_NAME);
                                            // Check status name from the list of case status
                                           if(status.equalsIgnoreCase(name))
                                           {
                                               // Get int array from resources with color codes
                                               int[] mTestArray = getResources().getIntArray(R.array.testArray);
                                               int length=mTestArray.length;
                                               if(length>0) {
                                                   // If ineteger array length is greater than equal to i+1,then show as such colour
                                                   if (length >=i+1) {
                                                       txtStatus.setTextColor(mTestArray[i+1]);
                                                   }
                                                   else // In case,color codes are less,repeat them for the case statuses
                                                   {
                                                       int position=(i+1) % mTestArray.length;
                                                       txtStatus.setTextColor(mTestArray[position]);
                                                   }
                                               }
                                           }
                                            setIntentInformation();
                                        }
                                    }

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
                        Utils.showToast(CaseDetailActivity.this, "No Internet Connection");
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

                    params.put(UserInfo.USER_ID, Utils.getUserPreferences(CaseDetailActivity.this, UserInfo.USER_ID));
                    params.put(UserInfo.USER_SECURITY_HASH, Utils.getUserPreferences(CaseDetailActivity.this, UserInfo.USER_SECURITY_HASH));

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
