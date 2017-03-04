package com.eweblog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.eweblog.common.UserInfo;
import com.eweblog.common.MapAppConstant;
import com.eweblog.common.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

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

public class AddCommentActivity extends AppCompatActivity{

    Button btnAdd;
    EditText edtComments, edtNextDate;
    TextView txtNextError;
    String strId, strNextDt, strComment;
    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    DateFormat dateFormatter2 = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
    LinearLayout linearLayout, llCaseStatus;
    Calendar cal = Calendar.getInstance();
    Date sysDate = cal.getTime();
    ArrayAdapter<String> caseStatusAdapter;
    Spinner sprCaseStaus;
    List<String> commonList=new ArrayList<>();
    HashMap<String,String> hash = new HashMap<String,String>();
    int selectedStatusCaseId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
              //  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_comment);
        inflateLayout();
    }
    public void inflateLayout()
    {
        btnAdd=(Button)findViewById(R.id.add);
        llCaseStatus = (LinearLayout) findViewById(R.id.ll_case_status);
        sprCaseStaus = (Spinner) findViewById(R.id.spinner_case_status);
        txtNextError=(TextView)findViewById(R.id.tw_next_error);
        edtComments=(EditText) findViewById(R.id.textView_comments);
        edtNextDate=(EditText) findViewById(R.id.textView_nextdt);
        edtNextDate.setHint(dateFormatter2.format(sysDate));
        linearLayout = (LinearLayout) findViewById(R.id.ll_navi);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        /** Show case status column only if Piad user **/
        if(Utils.getUserPreferencesBoolean(AddCommentActivity.this, UserInfo.COMMON_PAID))
        {
            llCaseStatus.setVisibility(View.VISIBLE);
            getCaseStatuses();
        }
        else
        {
            llCaseStatus.setVisibility(View.GONE);
        }
        strId=getIntent().getStringExtra("id");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAddComment();
            }
        });
        edtNextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDatePicker();
            }
        });
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
    /** List of case status for paid users **/
    public void getCaseStatuses() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(AddCommentActivity.this);
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
                        Toast.makeText(AddCommentActivity.this, serverMessage,Toast.LENGTH_LONG).show();

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
                            if (sprCaseStaus.getAdapter() == null) {
                                caseStatusAdapter = new ArrayAdapter<String>(AddCommentActivity.this, R.layout.layout_spinner, commonList)
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
                                caseStatusAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
                                sprCaseStaus.setAdapter(caseStatusAdapter);
                                sprCaseStaus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        for (Map.Entry<String, String> entry : hash.entrySet()) {
                                            if (entry.getValue().equals(commonList.get(i))) {
                                                selectedStatusCaseId=Integer.valueOf(entry.getKey());
                                                Log.e("KEY",""+selectedStatusCaseId);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                                sprCaseStaus.setSelection(1);

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
                        Toast.makeText(AddCommentActivity.this, "No Internet Connection",
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

                    params.put("user_id", Utils.getUserPreferences(AddCommentActivity.this, UserInfo.USER_ID));
                    params.put("user_security_hash", Utils.getUserPreferences(AddCommentActivity.this, UserInfo.USER_SECURITY_HASH));

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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    /** Check validations before adding comment**/
    public void onClickAddComment()
    {
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

    public void addComment() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(AddCommentActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String ADD_COMMENT_URL=MapAppConstant.API + MapAppConstant.ADD_COMMENT;
            Log.e("ADD COMMENT URL",ADD_COMMENT_URL);
            StringRequest sr = new StringRequest(Request.Method.POST, ADD_COMMENT_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("ADD COMMENT RESPONSE", response);

                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Utils.showToast(AddCommentActivity.this, serverMessage);

                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {
                                if ("1".equals(serverCode)) {

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            //Intent intent1 = new Intent(AddCommentActivity.this,  MainAcitivity.class);
                            //startActivity(intent1);
                            onBackPressed();
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
                        Utils.showToast(AddCommentActivity.this, "Timeout Error");
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

                    params.put(UserInfo.USER_ID, Utils.getUserPreferences(AddCommentActivity.this, UserInfo.USER_ID));
                    params.put(UserInfo.USER_SECURITY_HASH, Utils.getUserPreferences(AddCommentActivity.this, UserInfo.USER_SECURITY_HASH));
                    params.put(UserInfo.CASE_ID, strId);
                    params.put(UserInfo.CASE_DETAIL_COMMENT, strComment);
                    params.put(UserInfo.CASE_NEXT_DATE, strNextDt);
                    if(Utils.getUserPreferencesBoolean(AddCommentActivity.this, UserInfo.COMMON_PAID))
                    {
                        params.put(UserInfo.CASE_STATUSES_ID,String.valueOf(selectedStatusCaseId));
                    }
                    Log.e("ADD COMMENT REQUEST",params.toString());
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
