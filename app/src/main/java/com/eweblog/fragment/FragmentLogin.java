package com.eweblog.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.eweblog.ForgotPasswordActivity;
import com.eweblog.LoginActivity;
import com.eweblog.OTPScreenActivity;
import com.eweblog.R;
import com.eweblog.RegisterationActivity;
import com.eweblog.Utils;
import com.eweblog.common.UserInfo;
import com.eweblog.common.ConnectionDetector;
import com.eweblog.common.MapAppConstant;
import com.eweblog.common.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class FragmentLogin extends Fragment {

    EditText edtContact,edtPwd;
    String strContact, strPwd, userID, userSecHash, userName, userEmail, userContact,
            userEmailVerified, userMobileVerified,userStatus, imgUrl,userLastName,
            userStateOfPractice,userCityofPractice,userSpecialization,userStateOfPractice2,userCityofPractice2;;;
    UserInfo userInfo;
    ConnectionDetector cd;
    TextView txtNotAUser, txtForgotPwd;
    int groupId;
    int corporatePlansId=0;
    int parentId=0;
    String versionName,versionResponse,linkDownload;
    int expiry=0;
    public FragmentLogin() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getActivity() fragment
       View rootview= inflater.inflate(R.layout.fragment_login, container, false);
        cd = new ConnectionDetector(getActivity().getApplicationContext());
        edtContact = (EditText)rootview.findViewById(R.id.email);
        edtPwd = (EditText) rootview.findViewById(R.id.password);
        txtNotAUser = (TextView)rootview. findViewById(R.id.textView);
        txtForgotPwd = (TextView) rootview.findViewById(R.id.textView_forgot);
        versionName=Utils.GetAppVersion(getActivity());
      /*  txtNotAUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegisterationActivity.class);
                startActivity(intent);
            }
        });*/
        txtNotAUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              showDialog();
            }
        });
        txtForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        Button mEmailSignInButton = (Button)rootview.findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
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
            String LOGIN_URL=MapAppConstant.API + MapAppConstant.LOGIN;
            Log.e("CLogin URL",LOGIN_URL);
            StringRequest sr = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("Login Response",response);

                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");

                        if (serverCode.equalsIgnoreCase("0")) {
                            Utils.showToast(getActivity(),serverMessage.replace(" | "," "));
                           /* if(serverMessage.contains("|"))
                            {

                                Toast.makeText(getActivity(),  serverMessage.replace("|"," "),Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getActivity(), serverMessage.replace("|", ""), Toast.LENGTH_LONG).show();
                            }*/
                        }
                        if (serverCode.equalsIgnoreCase("-1")) {
                            Utils.showToast(getActivity(), serverMessage);
                        }
                        if (serverCode.equalsIgnoreCase("1") || serverCode.equalsIgnoreCase("2")) {

                            try {
                                //if ("1".equals(serverCode)) {
                                    JSONObject jsonObject=object.getJSONObject("data");
                                    userID=jsonObject.getString("user_id");
                                    userSecHash=jsonObject.getString("user_security_hash");
                                    userName=jsonObject.getString("user_name");
                                    userEmail=jsonObject.getString("user_email");
                                    userContact=jsonObject.getString("user_contact");
                                    userEmailVerified=jsonObject.getString("user_email_verification_status");
                                    userMobileVerified=jsonObject.getString("user_mobile_verification_status");
                                    userStatus=jsonObject.getString("user_status");
                                    userStateOfPractice=jsonObject.getString(UserInfo.USER_STATE_OF_PRACTISE);
                                    userCityofPractice=jsonObject.getString(UserInfo.USER_CITY_OF_PRACTISE);
                                userStateOfPractice2=jsonObject.getString(UserInfo.USER_STATE_OF_PRACTISE1);
                                userCityofPractice2=jsonObject.getString(UserInfo.USER_CITY_OF_PRACTISE1);
                                    userLastName=jsonObject.getString(UserInfo.USER_LAST_NAME);
                                    parentId=jsonObject.getInt(UserInfo.USER_PARENT_ID);
                                    groupId=jsonObject.getInt("group_id");
                                if(jsonObject.getString(UserInfo.USER_PROFILE_IMAGE).equalsIgnoreCase(""))
                                {imgUrl="";}
                                else {
                                    imgUrl = jsonObject.getString(UserInfo.USER_PROFILE_IMAGE_URL);
                                }
                                if((groupId==4 || groupId==5)&& parentId==0)//Check if paid or business user
                                        corporatePlansId=jsonObject.getInt(UserInfo.CORPORATE_PLANS_ID);
                                    userSpecialization=jsonObject.getString(UserInfo.USER_SPECIALIZATION);
                                JSONObject configurations=jsonObject.getJSONObject(UserInfo.CONFIGURATIONS);
                                versionResponse=configurations.getString(UserInfo.VERSION);
                                linkDownload=configurations.getString(UserInfo.URL_DOWNLOAD);
                                expiry=jsonObject.getInt(UserInfo.USER_IS_EXPIRED);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Utils.saveTypeOfUser(getActivity(),groupId,parentId);
                            Utils.checkSmsAlert(getActivity(),corporatePlansId);
                            Utils.storeUserPreferences(getActivity(), UserInfo.USER_ID,userID);
                            Utils.storeUserPreferences(getActivity(), UserInfo.USER_SECURITY_HASH,userSecHash);
                            Utils.storeUserPreferences(getActivity(), UserInfo.USER_EMAIL,userEmail);
                            Utils.storeUserPreferences(getActivity(), UserInfo.USER_NAME,userName);
                            Utils.storeUserPreferences(getActivity(), UserInfo.USER_CONTACT,userContact);
                            Utils.storeUserPreferences(getActivity(), UserInfo.USER_STATUS,userStatus);
                            Utils.storeUserPreferences(getActivity(), UserInfo.USER_PROFILE_IMAGE_URL,imgUrl);
                            Utils.storeUserPreferences(getActivity(), UserInfo.USER_LAST_NAME,userLastName);
                            Utils.storeUserPreferences(getActivity(), UserInfo.USER_STATE_OF_PRACTISE,userStateOfPractice);
                            Utils.storeUserPreferences(getActivity(), UserInfo.USER_CITY_OF_PRACTISE,userCityofPractice);
                            Utils.storeUserPreferences(getActivity(), UserInfo.USER_SPECIALIZATION,userSpecialization);
                            Utils.storeUserPreferences(getActivity(), UserInfo.USER_STATE_OF_PRACTISE1,userStateOfPractice2);
                            Utils.storeUserPreferences(getActivity(), UserInfo.USER_CITY_OF_PRACTISE1,userCityofPractice2);
                            if(userEmailVerified.equalsIgnoreCase("1"))
                                Utils.storeUserPreferencesBoolean(getActivity(), UserInfo.USER_EMAIL_VERIFICATION_STATUS,true);
                            else
                                Utils.storeUserPreferencesBoolean(getActivity(), UserInfo.USER_EMAIL_VERIFICATION_STATUS,false);
                            if(userMobileVerified.equalsIgnoreCase("1"))
                                Utils.storeUserPreferencesBoolean(getActivity(), UserInfo.USER_MOBILE_VERIFICATION_STATUS,true);
                            else
                                Utils.storeUserPreferencesBoolean(getActivity(), UserInfo.USER_MOBILE_VERIFICATION_STATUS,false);

                            Utils.storeUserPreferences(getActivity(), UserInfo.GROUP_ID,String.valueOf(groupId));
                            if(serverCode.equalsIgnoreCase("1") ) {

                                if(versionName.equalsIgnoreCase(versionResponse))
                                {
                                    Log.e("Version","Same");

                                    if(versionName.equalsIgnoreCase(versionResponse))
                                    {
                                        Log.e("Version","Same");
                                        if(expiry==0) {
                                            ((LoginActivity)getActivity()).getAllCases();
                                            // Utils.showToast(getActivity(),serverMessage.replace(" | "," "));
                                        }
                                        else if(expiry==1)
                                        {dialogExpiry(1,serverMessage);}
                                        else if(expiry==2)
                                        {dialogExpiry(2,serverMessage);}
                                    }

                                }
                                else{
                                    Log.e("Version","Not same");
                                    dialogUpdate(linkDownload);
                                }

                            }
                            else if(serverCode.equalsIgnoreCase("2"))
                            {

                                Intent intent = new Intent(getActivity(), OTPScreenActivity.class);
                                startActivity(intent);
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
                    Log.e("Login request",params.toString());
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
    public void showDialog()
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle("Title");

        TextView text = (TextView) dialog.findViewById(R.id.textDialog);
        TextView type1text= (TextView) dialog.findViewById(R.id.type1);
        TextView type2text= (TextView) dialog.findViewById(R.id.type2);
        type1text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Utils.showToast(getActivity(),"Premium User");
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(MapAppConstant.REGISTER_AS_PAID));
                startActivity(i);
                dialog.dismiss();
            }
        });
        type2text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dialog.dismiss();
                Intent intent = new Intent(getActivity(), RegisterationActivity.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }
    public void dialogUpdate(final String url) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.update_layout);

        Button yes = (Button) dialog.findViewById(R.id.bt_yes);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        dialog.show();
    }
    public void dialogExpiry(int expiryInteger,final String serverMessage) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.update_layout);
        TextView textView=(TextView) dialog.findViewById(R.id.text1);
        Button yes = (Button) dialog.findViewById(R.id.bt_yes);
        if(expiryInteger==1) {
            textView.setText("Your package is about to expire.");
            yes.setText("OK");
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    ((LoginActivity)getActivity()).getAllCases();
                    Utils.showToast(getActivity(),serverMessage.replace(" | "," "));
                }
            });
        }
        if(expiryInteger==2)
        {
            textView.setText("You subscription is expired. Please renew your package first to use unlimited services");
            yes.setText("BUY NOW");
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(MapAppConstant.REGISTER_AS_PAID));
                    startActivity(i);
                }
            });
        }

        dialog.show();
    }
}
