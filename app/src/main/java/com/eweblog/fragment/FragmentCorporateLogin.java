package com.eweblog.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.eweblog.ForgotPasswordActivity;
import com.eweblog.R;
import com.eweblog.RegisterationActivity;
import com.eweblog.SelectDateActivity;
import com.eweblog.common.ConnectionDetector;
import com.eweblog.common.MapAppConstant;
import com.eweblog.common.Prefshelper;
import com.eweblog.common.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class FragmentCorporateLogin extends Fragment {

    EditText edtContact,edtPwd, edtCorporateId;
    String strContact, strPwd, strCorporateId, userID, userSecHash, userName, userEmail,
            userContact, userEmailVerified, userMobileVerified,userStatus, imgUrl, corporateUser;
    Prefshelper prefshelper;
    ConnectionDetector cd;

    public FragmentCorporateLogin() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootview= inflater.inflate(R.layout.fragment_corporate_login, container, false);

        cd = new ConnectionDetector(getActivity().getApplicationContext());
        edtContact = (EditText)rootview.findViewById(R.id.email);
        edtPwd = (EditText) rootview.findViewById(R.id.password);
        edtCorporateId= (EditText) rootview.findViewById(R.id.id);
        prefshelper=new Prefshelper(getActivity());

        Button mEmailSignInButton = (Button)rootview.findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View focusView = null;
                boolean cancelLogin = false;

                strContact=edtContact.getText().toString();
                strPwd=edtPwd.getText().toString();
                strCorporateId=edtCorporateId.getText().toString();
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
                if (TextUtils.isEmpty(strCorporateId)) {
                    edtCorporateId.setError("Field must not be empty.");
                    focusView = edtCorporateId;
                    cancelLogin = true;
                }
                if (cancelLogin) {
                    // error in login
                    focusView.requestFocus();
                }
                else
                {
                    if(cd.isConnectingToInternet())
                    {
                        login();
                    }
                    else
                    {
                        dialog();
                    }
                }

            }
        });

        return  rootview;
    }
    private boolean isValidPhone(String pass) {
        return pass != null && pass.length() == 10;
    }
    private boolean isValidPass(String pass) {
        return pass != null && pass.length() >= 6;
    }

    public void dialog() {
        final Dialog dialog = new Dialog(getActivity());
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
    public void login() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
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

                        if (serverCode.equalsIgnoreCase("0")) {
                            if(serverMessage.contains("|"))
                            {

                                Toast.makeText(getActivity(),  serverMessage.replace("|"," "),Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getActivity(), serverMessage.replace("|", ""), Toast.LENGTH_LONG).show();
                            }
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
                                    imgUrl=jsonObject.getString("user_profile_image_url");
                                    corporateUser=jsonObject.getString("group_id");
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
                            prefshelper.storeProfileImage(imgUrl);
                            prefshelper.storeCorporateUser("1");
                            Intent intent = new Intent(getActivity(), SelectDateActivity.class);
                            startActivity(intent);


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
                        Toast.makeText(getActivity(), "No Internet Connection",
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
                    params.put("corporation_id", strCorporateId);
                    return params;
                }
            };
            sr.setShouldCache(true);

            sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @TargetApi(Build.VERSION_CODES.M)

    @Override
    public void onResume() {

        super.onResume();


    }

}