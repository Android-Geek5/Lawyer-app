package com.eweblog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterationActivity extends AppCompatActivity {

    Button btnRegister;
    EditText edtName,edtEmail,edtContact,edtPwd,edtCPwd,edtLastName;
    String strName, strEmail, strContact, strPwd, strCPwd, userID, userSecHash,strLastName;
    Prefshelper prefshelper;
    LinearLayout layout;
    String errorName, errorMobile, errorEmail, errorPassword, errorConfirm;
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
       inflateLayout();
       /* layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Utils.hideSoftKeyboard(RegisterationActivity.this);
            }
        });*/

        cd=new ConnectionDetector(RegisterationActivity.this);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRegister();


            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();

    }
    @Override
    public void onResume() {
        super.onResume();


    }
    @Override
    public void onPause() {
        super.onPause();


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

    public void signUp() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(RegisterationActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

            Log.e("", "SIGNUP " + MapAppConstant.API + "signup");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "signup", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");


                        if (serverCode.equalsIgnoreCase("0")) {

                            if(serverMessage.contains("|"))
                            {

                                Toast.makeText(RegisterationActivity.this,  serverMessage.replace("|"," "),Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(RegisterationActivity.this, serverMessage.replace("|", ""), Toast.LENGTH_LONG).show();
                            }

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            Toast.makeText(RegisterationActivity.this, serverMessage,Toast.LENGTH_LONG).show();
                            try {
                                if ("1".equals(serverCode)) {
                                    JSONObject jsonObject=object.getJSONObject("data");
                                    userID=jsonObject.getString("user_id");
                                    userSecHash=jsonObject.getString("user_security_hash");
                                    Utils.storeUserPreferences(RegisterationActivity.this,Prefshelper.USER_ID,userID);
                                    Utils.storeUserPreferences(RegisterationActivity.this,Prefshelper.USER_SECURITY_HASH,userSecHash);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Intent intent = new Intent(RegisterationActivity.this, OTPScreenActivity.class);
                            startActivity(intent);
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
                        Toast.makeText(RegisterationActivity.this, "No Internet Connection",
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
                    params.put("user_name", strName);
                    params.put("user_email", strEmail);
                    params.put(Prefshelper.USER_LAST_NAME,strLastName);
                    params.put("user_contact", strContact);
                    params.put("user_login_password", strPwd);
                    params.put("confirm_login_password", strCPwd);
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
    btnRegister = (Button) findViewById(R.id.email_sign_in_button);
    edtName = (EditText) findViewById(R.id.name);
    edtEmail = (EditText) findViewById(R.id.email);
    edtContact = (EditText) findViewById(R.id.mobile);
    edtPwd = (EditText) findViewById(R.id.password);
    edtLastName=(EditText) findViewById(R.id.last_name);
    prefshelper=new Prefshelper(RegisterationActivity.this);
    edtCPwd = (EditText) findViewById(R.id.confirm_password);
    layout=(LinearLayout) findViewById(R.id.email_login_form);
}
    public void onClickRegister()
    {
        View focusView = null;
        boolean cancelLogin = false;
        strName=edtName.getText().toString();
        strEmail=edtEmail.getText().toString();
        strContact=edtContact.getText().toString();
        strPwd=edtPwd.getText().toString();
        strCPwd=edtCPwd.getText().toString();
        strLastName=edtLastName.getText().toString();


        if (TextUtils.isEmpty(strName)) {
            edtName.setError("Field must not be empty.");
            focusView = edtName;
            cancelLogin = true;
        }
        else if(strName.length()<2)
        {
            edtName.setError("Field must be atleast of length 2.");
            focusView = edtName;
            cancelLogin = true;
        }
        if (TextUtils.isEmpty(strLastName)) {
            edtLastName.setError("Field must not be empty.");
            focusView = edtLastName;
            cancelLogin = true;
        }
        else if(strLastName.length()<2)
        {
            edtLastName.setError("Field must be atleast of length 2.");
            focusView = edtLastName;
            cancelLogin = true;
        }
        if (TextUtils.isEmpty(strContact)) {
            edtContact.setError("Field must not be empty.");
            focusView = edtContact;
            cancelLogin = true;
        }
        else if(!Utils.isValidPhone(strContact))
        {
            edtContact.setError("Mobile Number must be of 10 digits.");
            focusView = edtContact;
            cancelLogin = true;
        }

        if (TextUtils.isEmpty(strEmail)) {
            edtEmail.setError("Field must not be empty.");
            focusView = edtEmail;
            cancelLogin = true;
        }
        else if (!Utils.isValidEmail(strEmail)) {
            edtEmail.setError("Invalid Email.");
            focusView = edtEmail;
            cancelLogin = true;
        }

        if (TextUtils.isEmpty(strPwd)) {
            edtPwd.setError("Field must not be empty.");
            focusView = edtPwd;
            cancelLogin = true;
        }
        else if (!Utils.isValidPass(strPwd)) {
            edtPwd.setError("Password must be of length 6.");
            focusView = edtPwd;
            cancelLogin = true;
        }
        if (TextUtils.isEmpty(strCPwd)) {
            edtCPwd.setError("Field must not be empty.");
            focusView = edtCPwd;
            cancelLogin = true;
        }
        else if (!Utils.isValidPass(strCPwd)) {
            edtCPwd.setError("Password must be of length 6.");
            focusView = edtCPwd;
            cancelLogin = true;
        }
        else if(!strCPwd.equalsIgnoreCase(strPwd))
        {
            edtCPwd.setError("Passwords do not match.");
            focusView = edtCPwd;
            cancelLogin = true;
        }


        if(cancelLogin) {
            // error in login
            focusView.requestFocus();
        }
        else
        {
            if(cd.isConnectingToInternet())
            {
                signUp();
            }
            else
            {
                dialog();
            }
        }


    }
}
