package com.lawyerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterationActivity extends AppCompatActivity {

    Button btnRegister;
    EditText edtName,edtEmail,edtContact,edtPwd,edtCPwd;
    String strName, strEmail, strContact, strPwd, strCPwd, userID, userSecHash;
    Prefshelper prefshelper;
    String errorName, errorMobile, errorEmail, errorPassword, errorConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        btnRegister = (Button) findViewById(R.id.email_sign_in_button);
       edtName = (EditText) findViewById(R.id.name);
       edtEmail = (EditText) findViewById(R.id.email);
       edtContact = (EditText) findViewById(R.id.mobile);
       edtPwd = (EditText) findViewById(R.id.password);
       prefshelper=new Prefshelper(RegisterationActivity.this);
       edtCPwd = (EditText) findViewById(R.id.confirm_password);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strName=edtName.getText().toString();
                strEmail=edtEmail.getText().toString();
                strContact=edtContact.getText().toString();
                strPwd=edtPwd.getText().toString();
                strCPwd=edtCPwd.getText().toString();
                signUp();


            }
        });
    }
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPhone(String pass) {
        return pass != null && pass.length() == 10;
    }
    private boolean isValidPass(String pass) {
        return pass != null && pass.length() >= 6;
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
    public void signUp() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(RegisterationActivity.this);
            pDialog.setMessage("Loading...");
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
                            View focusView = null;
                            boolean cancelLogin = false;
                         //   Toast.makeText(RegisterationActivity.this, serverMessage,Toast.LENGTH_LONG).show();
                            JSONObject jsonObject=object.getJSONObject("data");
                            String error1=jsonObject.getString("user_name");
                            String error2=jsonObject.getString("user_email");
                            String error3=jsonObject.getString("user_contact");
                            String error4=jsonObject.getString("user_login_password");
                            String error5=jsonObject.getString("confirm_login_password");
                            if(error1!=null)
                            {
                                errorName=error1;
                            }
                            if(error2!=null)
                            {
                                errorEmail=error2;
                            }
                            if(error3!=null)
                            {
                                errorMobile=error3;
                            }
                            if(error4!=null)
                            {
                                errorPassword=error4;
                            }
                            if(error5!=null)
                            {
                                errorConfirm=error5;
                            }

                            if (TextUtils.isEmpty(strName)) {
                                edtName.setError(errorName);
                                focusView = edtName;
                                cancelLogin = true;
                            }
                            else if(strName.length()<2)
                            {
                                edtName.setError(errorName);
                                focusView = edtName;
                                cancelLogin = true;
                            }
                            if (TextUtils.isEmpty(strContact)) {
                                edtContact.setError(errorMobile);
                                focusView = edtContact;
                                cancelLogin = true;
                            }
                            else if(!isValidPhone(strContact))
                            {
                                edtContact.setError(errorMobile);
                                focusView = edtContact;
                                cancelLogin = true;
                            }
                            if(errorMobile.equalsIgnoreCase("The Phone field must contain a unique value."))
                            {
                                edtContact.setError(errorMobile);
                                focusView = edtContact;
                                cancelLogin = true;

                            }
                            if (TextUtils.isEmpty(strEmail)) {
                                edtEmail.setError(errorEmail);
                                focusView = edtEmail;
                                cancelLogin = true;
                            }
                            else if (!isValidEmail(strEmail)) {
                                edtEmail.setError(errorEmail);
                                focusView = edtEmail;
                                cancelLogin = true;
                            }
                            if(errorEmail.equalsIgnoreCase("The Email field must contain a unique value."))
                            {
                                edtEmail.setError(errorEmail);
                                focusView = edtEmail;
                                cancelLogin = true;
                            }
                            if (TextUtils.isEmpty(strPwd)) {
                                edtPwd.setError(errorPassword);
                                focusView = edtPwd;
                                cancelLogin = true;
                            }
                            else if (!isValidPass(strPwd)) {
                                edtPwd.setError(errorPassword);
                                focusView = edtPwd;
                                cancelLogin = true;
                            }
                            if (TextUtils.isEmpty(strCPwd)) {
                                edtCPwd.setError(errorConfirm);
                                focusView = edtCPwd;
                                cancelLogin = true;
                            }
                            else if (!isValidPass(strCPwd)) {
                                edtCPwd.setError(errorConfirm);
                                focusView = edtCPwd;
                                cancelLogin = true;
                            }
                             if(strCPwd.equalsIgnoreCase("The Confirm Password field does not match the Password field."))
                            {
                                edtCPwd.setError(errorConfirm);
                                focusView = edtCPwd;
                                cancelLogin = true;
                            }

                          if(cancelLogin) {
                                // error in login
                                focusView.requestFocus();
                            }


                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            Toast.makeText(RegisterationActivity.this, serverMessage,Toast.LENGTH_LONG).show();
                            try {
                                if ("1".equals(serverCode)) {
                                    JSONObject jsonObject=object.getJSONObject("data");
                                    userID=jsonObject.getString("user_id");
                                    userSecHash=jsonObject.getString("user_security_hash");


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
                        Toast.makeText(RegisterationActivity.this, "Timeout Error",
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
}
