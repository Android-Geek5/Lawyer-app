package com.lawyerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    EditText edtContact,edtPwd;
    String strContact, strPwd, userID, userSecHash, userName, userEmail, userContact, userEmailVerified, userMobileVerified,userStatus;
    Prefshelper prefshelper;

    TextView txtNotAUser, txtForgotPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        edtContact = (EditText) findViewById(R.id.email);
        edtPwd = (EditText) findViewById(R.id.password);
        txtNotAUser = (TextView) findViewById(R.id.textView);
        txtForgotPwd = (TextView) findViewById(R.id.textView_forgot);
        prefshelper=new Prefshelper(this);
        txtNotAUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterationActivity.class);
                startActivity(intent);
            }
        });
        txtForgotPwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                View focusView = null;
                boolean cancelLogin = false;

                strContact=edtContact.getText().toString();
                strPwd=edtPwd.getText().toString();
            
                if (TextUtils.isEmpty(strContact)) {
                    edtContact.setError("Field must not be empty.");
                    focusView = edtContact;
                    cancelLogin = true;
                }else if (!isValidPhone((strContact))) {
                    edtContact.setError("Mobile number must be of digits 10.");
                    focusView = edtContact;
                    cancelLogin = true;
                }
              
                if (TextUtils.isEmpty(strPwd)) {
                    edtPwd.setError("Field must not be empty.");
                    focusView = edtPwd;
                    cancelLogin = true;
                } else if (!isValidPass(strPwd)) {
                    edtPwd.setError("Password must be of digits 6.");
                    focusView = edtPwd;
                    cancelLogin = true;
                }
               
                if (cancelLogin) {
                    // error in login
                    focusView.requestFocus();
                } else {
                   
                    login();
                }

            }
        });


    }
    private boolean isValidPhone(String pass) {
        return pass != null && pass.length() == 10;
    }
    private boolean isValidPass(String pass) {
        return pass != null && pass.length() >= 6;
    }


    public void login() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "SIGNUP " + MapAppConstant.API + "login");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "login", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Toast.makeText(LoginActivity.this, serverMessage,Toast.LENGTH_LONG).show();

                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {
                                if ("1".equals(serverCode)) {
                                    JSONObject jsonObject=object.getJSONObject("data");
                                     userID=jsonObject.getString("user_id");
                                     userSecHash=jsonObject.getString("user_security_hash");
                                    userName=jsonObject.getString("user_name");
                                     userEmail=jsonObject.getString("user_email");
                                     userContact=jsonObject.getString("user_contact");
                                     userEmailVerified=jsonObject.getString("user_email_verification_status");
                                     userMobileVerified=jsonObject.getString("user_mobile_verification_status");
                                     userStatus=jsonObject.getString("user_status");



                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            prefshelper.storeUserIdToPreference(userID);
                            prefshelper.storeSecHashToPreference(userSecHash);
                            prefshelper.storeEmailToPreference(userEmail);
                            prefshelper.storeUserNameToPreference(userName);
                            prefshelper.storeUserContactToPreference(userContact);
                            prefshelper.storeUserStatusToPreference(userStatus);
                            prefshelper.storeEmailVerification(userEmailVerified);
                            prefshelper.storeMobileVerification(userMobileVerified);
                            Intent intent = new Intent(LoginActivity.this, SelectDateActivity.class);
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
                        Toast.makeText(LoginActivity.this, "Timeout Error",
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

                    params.put("user_login", strContact);
                    params.put("user_login_password", strPwd);
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

