package com.eweblog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompatSideChannelService;
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
import com.eweblog.adapter.CommonListAdapter;
import com.eweblog.common.ConnectionDetector;
import com.eweblog.common.MapAppConstant;
import com.eweblog.common.Prefshelper;
import com.eweblog.common.VolleySingleton;
import com.eweblog.model.CaseListModel;
import com.eweblog.model.CommonList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


public class AddCaseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    LinearLayout linearLayout, llCaseStatus,llAssignedTo,smsLayout;
    Spinner sprCaseType, sprCaseStaus ,sprAssignedTo ;

    ArrayAdapter<String> stringArrayAdapter;
    ArrayAdapter<String> caseStatusAdapter;
    ArrayAdapter<String> assignedToAdapter;
    Button btnAdd;
    EditText edtCourtName, edtCaseNumber, edtCaseTitle, edtCaseType, edtStatus, edtRetainName, edtRetainMobile,
            edtOppositeName, edtOppositeNumber, edtComments, edtStartDate, edtPrevDate, edtNextDate, edtClientContact,edtCaseFee;
    Prefshelper prefshelper;
    String strNumber, strTitle, strType, strCourt, strStatus, strPreviousDt, strNextDt, strOCName, strOCContact, strRName, strRContact,
            strComment, strStartDate, strDay, strMonth, strYear, strNDay, strNMonth, strNYear, strSDay, strSMonth, strSYear,strFee,strSmsNumber;
    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    DateFormat dateFormatter2 = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
    TextView txtStartError, txtPrevError, txtNextError;
    ConnectionDetector cd;
    Calendar cal = Calendar.getInstance();
    Date sysDate = cal.getTime();
    CheckBox chkSmsAlert;
    List<CaseListModel> list;
    List<String> commonList=new ArrayList<>();
    List<String> commonList2=new ArrayList<>();
    HashMap<String,String> hash = new HashMap<String,String>();
    HashMap<String,String> hash2 = new HashMap<String,String>();
    int selectedStatusCaseId=0;
    int selectedChildUser=0;
    int smsAlertStatus=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
              //  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_add_case);
         prefshelper = new Prefshelper(AddCaseActivity.this);
        list= new ArrayList<>();
        inflateLayout();
        setOnClicks();
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
         showHideItems();
         setDropDownOnCaseType();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainAcitivity.txtTitle.setText("Home");
        finish();
        //Intent intent1 = new Intent(AddCaseActivity.this, CorporateUserMainActivity.class);
        //startActivity(intent1);
        //CorporateUserMainActivity.txtTitle.setText("Home");
    }


    public void addCase() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(AddCaseActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

            Log.e("ADD CASE URL", MapAppConstant.API + MapAppConstant.ADD_CASE);
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "add_case", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("ADD CASE RESPONSE",  response);

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

                            Intent intent1 = new Intent(AddCaseActivity.this, MainAcitivity.class);
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

                    params.put("user_id", Utils.getUserPreferences(AddCaseActivity.this,Prefshelper.USER_ID));
                    params.put("user_security_hash", Utils.getUserPreferences(AddCaseActivity.this,Prefshelper.USER_SECURITY_HASH));
                    params.put("case_number", strNumber);
                    params.put("case_title", strTitle);
                    params.put("case_type", strType);
                    params.put("case_court_name", strCourt);
                    if(!Utils.getUserPreferencesBoolean(AddCaseActivity.this,Prefshelper.FREE_OR_PAID)){
                    params.put("case_position_status", strStatus);}
                    if(Utils.getUserPreferencesBoolean(AddCaseActivity.this,Prefshelper.FREE_OR_PAID) ||
                            Utils.getUserPreferencesBoolean(AddCaseActivity.this,Prefshelper.CORPORATE_OR_NOT)) {
                        params.put("case_statuses_id", String.valueOf(selectedStatusCaseId));
                        if (smsAlertStatus==1) {
                            params.put("case_sms_contact", edtClientContact.getText().toString());
                        params.put("case_sms_alert_status", "1");}
                        else params.put("case_sms_alert_status","0");
                    }

                    if(Utils.getUserPreferencesBoolean(AddCaseActivity.this,Prefshelper.CORPORATE_OR_NOT))
                    {params.put("case_assigned_to",String.valueOf(selectedChildUser));}
                    params.put("case_previous_date", strPreviousDt);
                    params.put("case_next_date", strNextDt);
                    params.put("case_opposite_counselor_name", strOCName);
                    params.put("case_opposite_counselor_contact", strOCContact);
                    params.put("case_retained_name", strRName);
                    params.put("case_retained_contact", strRContact);
                    params.put("case_detail_comment", strComment);
                    params.put("case_start_date", strStartDate);
                    params.put("case_fee",strFee);
                    Log.e("ADD CASE REQUEST",params.toString());
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
                        Toast.makeText(AddCaseActivity.this, serverMessage,Toast.LENGTH_LONG).show();

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
                             caseStatusAdapter = new ArrayAdapter<String>(AddCaseActivity.this, R.layout.layout_spinner, commonList)
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
                          /* caseStatusAdapter=new CommonListAdapter(AddCaseActivity.this, R.layout.layout_spinner_dropdown,commonList);
                                sprCaseStaus.setAdapter(caseStatusAdapter);

                                sprCaseStaus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                                     selectedStatusCaseId=commonList.get(position).getId();
                                        Log.e("SELECTED CASE STATUS ID",String.valueOf(selectedStatusCaseId));
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parentView) {
                                        // your code here
                                    }

                                });
*/
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

                    params.put("user_id", Utils.getUserPreferences(AddCaseActivity.this,Prefshelper.USER_ID));
                    params.put("user_security_hash", Utils.getUserPreferences(AddCaseActivity.this,Prefshelper.USER_SECURITY_HASH));

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
                Intent intent1 = new Intent(AddCaseActivity.this,  MainAcitivity.class);
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

                if(Utils.getUserPreferences(AddCaseActivity.this,Prefshelper.DATE).equalsIgnoreCase("start")) {
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
                else  if(Utils.getUserPreferences(AddCaseActivity.this,Prefshelper.DATE).equalsIgnoreCase("previous"))
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
                else  if(Utils.getUserPreferences(AddCaseActivity.this,Prefshelper.DATE).equalsIgnoreCase("next"))
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
            Utils.storeUserPreferences(AddCaseActivity.this,Prefshelper.DATE,"previous");
           dialogDatePicker();
        }
        if(view==edtNextDate)
        {
            Utils.storeUserPreferences(AddCaseActivity.this,Prefshelper.DATE,"next");
            dialogDatePicker();
        }
        if(view==edtStartDate)
        {
            Utils.storeUserPreferences(AddCaseActivity.this,Prefshelper.DATE,"start");
            dialogDatePicker();
        }

    }
    private CaseListModel model( String id,String name) {
        CaseListModel model = new CaseListModel();
        model.setCaseId(id);
        model.setNextDate(name);

        return model;
    }

    public void getChildUsers()
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(AddCaseActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String URL_VIEW_CHILD=MapAppConstant.API + MapAppConstant.VIEW_CHILD;
            Log.e("VIEW CHILD URL" ,URL_VIEW_CHILD);
            StringRequest sr = new StringRequest(Request.Method.POST, URL_VIEW_CHILD, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("VIEW CHILD RESPONSE",response);

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
                                    JSONObject jsonObject=object.getJSONObject("data");
                                    String userId=Utils.getUserPreferences(AddCaseActivity.this,Prefshelper.USER_ID);
                                    String userName="Me("+Utils.getUserPreferences(AddCaseActivity.this,Prefshelper.USER_NAME)+")";
                                      commonList2.add(userName);
                                      hash2.put(userId,userName);
                                    selectedChildUser=Integer.valueOf(userId);
                                    JSONArray inactiveUsers=jsonObject.getJSONArray("inactive_users");
                                    if(inactiveUsers.length()>0)
                                    {
                                        for(int i = 0; i<inactiveUsers.length(); i++)
                                        {
                                            JSONObject jsonObject2=inactiveUsers.getJSONObject(i);
                                            String id=jsonObject2.getString(Prefshelper.USER_ID);
                                            String name=jsonObject2.getString(Prefshelper.USER_DETAILS);
                                            hash2.put(id,name);
                                            commonList2.add(name);
                                        }
                                    }
                                    JSONArray activeUsers=jsonObject.getJSONArray("active_users");
                                    if(activeUsers.length()>0)
                                    {
                                        for(int i = 0; i<activeUsers.length(); i++)
                                        {
                                            JSONObject jsonObject3=activeUsers.getJSONObject(i);
                                            String id=jsonObject3.getString(Prefshelper.USER_ID);
                                            String name=jsonObject3.getString(Prefshelper.USER_DETAILS);
                                            hash2.put(id,name);
                                            commonList2.add(name);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (sprAssignedTo.getAdapter() == null) {
                                assignedToAdapter = new ArrayAdapter<String>(AddCaseActivity.this, R.layout.layout_spinner, commonList2)
                                {
                                    @Override
                                    public boolean isEnabled(int position) {

                                        return position != 0;
                                    }

                                    @Override
                                    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                                        View view = super.getDropDownView(position, convertView, parent);
                                        TextView tv = (TextView) view;
                                      /*  if (position == 0) {
                                            // Set the hint text color gray
                                            tv.setTextColor(Color.GRAY);
                                        } else {*/
                                            tv.setTextColor(Color.BLACK);
                                        //}

                                        return view;
                                    }
                                };
                                assignedToAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
                                sprAssignedTo.setAdapter(assignedToAdapter);
                                sprAssignedTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        for (Map.Entry<String, String> entry : hash2.entrySet()) {
                                            if (entry.getValue().equals(commonList2.get(i))) {
                                                selectedChildUser=Integer.valueOf(entry.getKey());
                                                Log.e("KEY",""+selectedChildUser);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
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

                    params.put("user_id", Utils.getUserPreferences(AddCaseActivity.this,Prefshelper.USER_ID));
                    params.put("user_security_hash", Utils.getUserPreferences(AddCaseActivity.this,Prefshelper.USER_SECURITY_HASH));

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
    public void inflateLayout()
    {
        sprCaseType = (Spinner) findViewById(R.id.spinner_type);
        sprCaseStaus = (Spinner) findViewById(R.id.spinner_case_status);
        sprAssignedTo=(Spinner) findViewById(R.id.spinner_assigned_to);
        btnAdd = (Button) findViewById(R.id.add);
        txtStartError = (TextView) findViewById(R.id.tw_start_error);
        txtPrevError = (TextView) findViewById(R.id.tw_prev_error);
        txtNextError = (TextView) findViewById(R.id.tw_next_error);
        linearLayout = (LinearLayout) findViewById(R.id.ll_navi);
        llCaseStatus = (LinearLayout) findViewById(R.id.ll_case_status);
        llAssignedTo = (LinearLayout) findViewById(R.id.ll_assigned_to);
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
        edtCaseFee=(EditText) findViewById(R.id.textView_caseFee);
        chkSmsAlert=(CheckBox)findViewById(R.id.checkedTextView);
        smsLayout=(LinearLayout) findViewById(R.id.smsLayout);
        edtNextDate.setHint(dateFormatter2.format(sysDate));
        edtPrevDate.setHint(dateFormatter2.format(sysDate));
        edtStartDate.setHint(dateFormatter2.format(sysDate));
    }
    public void setOnClicks()
    {
        edtStartDate.setOnClickListener(this);
        edtPrevDate.setOnClickListener(this);
        edtNextDate.setOnClickListener(this);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAdd();
            }
        });

    }

    public void showHideItems()
    {
        if(Utils.getUserPreferencesBoolean(AddCaseActivity.this,Prefshelper.CORPORATE_OR_NOT))
        {
            llAssignedTo.setVisibility(View.VISIBLE);
            getChildUsers();
            commonPaid();
        }
        else {
            llAssignedTo.setVisibility(View.GONE);
            if (Utils.getUserPreferencesBoolean(AddCaseActivity.this, Prefshelper.FREE_OR_PAID)) {
                commonPaid();
            } else {
                llCaseStatus.setVisibility(View.GONE);
                edtStatus.setVisibility(View.VISIBLE);
                smsLayout.setVisibility(View.GONE);
            }
        }
    }

    public void onClickAdd()
    {
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
        strFee=edtCaseFee.getText().toString();
        if(strStartDate==null)
        {
            strStartDate=dateFormatter.format(sysDate);
            Log.d("start date", strStartDate);
        }
        if (edtCaseType.getVisibility() == View.VISIBLE)
        {
            strType = edtCaseType.getText().toString();
            focusView = edtCaseType;
            cancelLogin = true;
        }
        if (smsAlertStatus==1)
        {
            strSmsNumber = edtClientContact.getText().toString();
            if (TextUtils.isEmpty(strSmsNumber)) {
                edtClientContact.setError("Field must not be empty.");
                focusView = edtClientContact;
                cancelLogin = true;
            }
            if(Utils.isValidPhone(strSmsNumber))
            {
                edtClientContact.setError("Enter a valid mobile number.");
                focusView = edtClientContact;
                cancelLogin = true;
            }
        }
        if (strPreviousDt==null)
        {
            txtPrevError.setVisibility(View.VISIBLE);
            focusView = txtPrevError;
            cancelLogin = true;
        }
        else
        {
            txtPrevError.setVisibility(View.GONE);
        }
        if (strNextDt==null)
        {
            txtNextError.setVisibility(View.VISIBLE);
            focusView = txtPrevError;
            cancelLogin = true;
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
        if (TextUtils.isEmpty(strFee)) {
            edtCaseFee.setError("Field must not be empty.");
            focusView = edtCaseFee;
            cancelLogin = true;
        }
        if (cancelLogin) {
            // error in login
            focusView.requestFocus();
        } else {

            addCase();
        }


    }

    public void setDropDownOnCaseType()
    {

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
    }
    public void commonPaid() {
        llCaseStatus.setVisibility(View.VISIBLE);
        edtStatus.setVisibility(View.GONE);
        getCaseStatuses();
        if (Utils.getUserPreferencesBoolean(AddCaseActivity.this, Prefshelper.SMS_ALERT)) {

            smsLayout.setVisibility(View.VISIBLE);
            edtClientContact.setVisibility(View.GONE);
            chkSmsAlert.setChecked(false);
            chkSmsAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (compoundButton.isChecked()) {
                        smsAlertStatus = 1;
                        edtClientContact.setVisibility(View.VISIBLE);
                    } else {
                        smsAlertStatus = 0;
                        edtClientContact.setVisibility(View.GONE);
                    }
                }
            });
        }
        else
        {smsLayout.setVisibility(View.GONE);}
    }
}
