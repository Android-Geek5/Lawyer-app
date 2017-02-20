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
import com.eweblog.common.ConnectionDetector;
import com.eweblog.common.MapAppConstant;
import com.eweblog.common.Prefshelper;
import com.eweblog.common.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OTPScreenActivity extends AppCompatActivity {
    Button btnRegister;
    EditText edtOtp;
    Prefshelper prefshelper;
    String otp;
    TextView txtNotReceived;
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_otpscreen);
        cd = new ConnectionDetector(OTPScreenActivity.this);
        edtOtp = (EditText) findViewById(R.id.password);
        prefshelper = new Prefshelper(OTPScreenActivity.this);
        btnRegister = (Button) findViewById(R.id.email_sign_in_button);
        txtNotReceived = (TextView) findViewById(R.id.textView);

        txtNotReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendOtp();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View focusView = null;
                boolean cancelLogin = false;

                otp = edtOtp.getText().toString();


                if (TextUtils.isEmpty(otp)) {
                    edtOtp.setError("Field must not be empty.");
                    focusView = edtOtp;
                    cancelLogin = true;
                } else if (!isValidOTP((otp))) {
                    edtOtp.setError("OTP must be of digits 6.");
                    focusView = edtOtp;
                    cancelLogin = true;
                }

                if (cancelLogin) {
                    // error in login
                    focusView.requestFocus();
                } else {

                    if (cd.isConnectingToInternet()) {
                        verifyOtp();
                    } else {
                        dialog();
                    }
                }

            }
        });
    }

    private boolean isValidOTP(String pass) {
        return pass != null && pass.length() == 6;
    }

    public void verifyOtp() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(OTPScreenActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String URL_VERIFY_OTP = MapAppConstant.API + MapAppConstant.VERIFY_OTP;
            Log.e("VERIFY OTP URL", URL_VERIFY_OTP);
            StringRequest sr = new StringRequest(Request.Method.POST, URL_VERIFY_OTP, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("VERIFY OTP RESPONSE", response);

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");

                        Log.e("error", response);
                        if (serverCode.equalsIgnoreCase("0")) {
                            Toast.makeText(OTPScreenActivity.this, serverMessage, Toast.LENGTH_LONG).show();
                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            if (Utils.getUserPreferencesBoolean(OTPScreenActivity.this, Prefshelper.COMMON_PAID)) {
                                if (Utils.getUserPreferencesBoolean(OTPScreenActivity.this, Prefshelper.USER_EMAIL_VERIFICATION_STATUS)) {
                                    Utils.storeUserPreferencesBoolean(OTPScreenActivity.this, Prefshelper.USER_MOBILE_VERIFICATION_STATUS, true);
                                    Toast.makeText(OTPScreenActivity.this, serverMessage, Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(OTPScreenActivity.this, SplashScreenActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    resendEmailVerification();
                                }
                            } else {
                                Utils.clearData(OTPScreenActivity.this);
                                finish();
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
                        Toast.makeText(OTPScreenActivity.this, "No Internet Connection",
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
                    params.put("user_id", Utils.getUserPreferences(OTPScreenActivity.this, Prefshelper.USER_ID));
                    params.put("user_security_hash", Utils.getUserPreferences(OTPScreenActivity.this, Prefshelper.USER_SECURITY_HASH));
                    params.put("user_otp", otp);
                    Log.e("VERIFY OTP REQUEST", params.toString());
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

    public void resendOtp() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(OTPScreenActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String RESEND_OTP_URL = MapAppConstant.API + MapAppConstant.RESEND_OTP;
            Log.e("RESEND OTP URL", RESEND_OTP_URL);
            StringRequest sr = new StringRequest(Request.Method.POST, RESEND_OTP_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("RESEND OTP RESPONSE", response);

                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");


                        if (serverCode.equalsIgnoreCase("0")) {
                            Toast.makeText(OTPScreenActivity.this, serverMessage, Toast.LENGTH_LONG).show();
                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            Toast.makeText(OTPScreenActivity.this, serverMessage, Toast.LENGTH_LONG).show();


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
                        Toast.makeText(OTPScreenActivity.this, "No Internet Connection",
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
                    params.put("user_id", Utils.getUserPreferences(OTPScreenActivity.this, Prefshelper.USER_ID));
                    params.put("user_security_hash", Utils.getUserPreferences(OTPScreenActivity.this, Prefshelper.USER_SECURITY_HASH));
                    Log.e("RESEND OTP REQUEST", params.toString());
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

    public void resendEmailVerification() {
        final ProgressDialog pDialog = new ProgressDialog(OTPScreenActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        String RESEND_EMAIL_URL = MapAppConstant.API + MapAppConstant.RESEND_EMAIL_VERIFICATION;
        Log.e("RESEND EMAIL URL", RESEND_EMAIL_URL);
        StringRequest sr = new StringRequest(Request.Method.POST, RESEND_EMAIL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                Log.e("RESEND email RESPONSE", response);

                try {
                    JSONObject object = new JSONObject(response);
                    String serverCode = object.getString("code");
                    String serverMessage = object.getString("message");


                    if (serverCode.equalsIgnoreCase("0")) {
                        Toast.makeText(OTPScreenActivity.this, serverMessage, Toast.LENGTH_LONG).show();
                    }
                    if (serverCode.equalsIgnoreCase("1")) {
                        Toast.makeText(OTPScreenActivity.this, serverMessage, Toast.LENGTH_LONG).show();
                        Utils.clearData(OTPScreenActivity.this);
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
                    Toast.makeText(OTPScreenActivity.this, "No Internet Connection",
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
                params.put("user_id", Utils.getUserPreferences(OTPScreenActivity.this, Prefshelper.USER_ID));
                params.put("user_security_hash", Utils.getUserPreferences(OTPScreenActivity.this, Prefshelper.USER_SECURITY_HASH));
                Log.e("RESEND EMAIL REQUEST", params.toString());
                return params;
            }
        };
        sr.setShouldCache(true);

        sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(sr);
    }
}

