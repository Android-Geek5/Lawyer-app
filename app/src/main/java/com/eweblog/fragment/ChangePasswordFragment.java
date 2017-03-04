package com.eweblog.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.eweblog.MainAcitivity;
import com.eweblog.R;
import com.eweblog.Utils;
import com.eweblog.common.ConnectionDetector;
import com.eweblog.common.MapAppConstant;
import com.eweblog.common.UserInfo;
import com.eweblog.common.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordFragment extends Fragment {

    EditText edtPwd,edtConfirmPwd;
    String strPwd, strConfirmPwd;
    ConnectionDetector cd;

    public ChangePasswordFragment() {
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
        View rootview = inflater.inflate(R.layout.fragment_change_password, container, false);
        MainAcitivity.txtTitle.setText("Change Password");
        cd=new ConnectionDetector(getActivity());
        edtPwd = (EditText) rootview.findViewById(R.id.password);
        edtConfirmPwd = (EditText) rootview.findViewById(R.id.cpassword);
        Button submit = (Button)rootview.findViewById(R.id.email_sign_in_button);
        if (!cd.isConnectingToInternet())
        {
            dialog();
            submit.setEnabled(false);
        }
        else
        {
            submit.setEnabled(true);
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View focusView = null;
                boolean cancelLogin = false;

                strPwd=edtPwd.getText().toString();
                strConfirmPwd=edtConfirmPwd.getText().toString();

                if (TextUtils.isEmpty(strPwd)) {
                    edtPwd.setError("Field must not be empty.");
                    focusView = edtPwd;
                    cancelLogin = true;
                }else if (!isValidPass((strPwd))) {
                    edtPwd.setError("Password must be of digits 6.");
                    focusView = edtPwd;
                    cancelLogin = true;
                }

                if (TextUtils.isEmpty(strConfirmPwd)) {
                    edtConfirmPwd.setError("Field must not be empty.");
                    focusView = edtConfirmPwd;
                    cancelLogin = true;
                } else if (!isValidPass(strConfirmPwd)) {
                    edtConfirmPwd.setError("Password must be of digits 6.");
                    focusView = edtConfirmPwd;
                    cancelLogin = true;
                }
                if(!strConfirmPwd.equalsIgnoreCase(strPwd))
                {
                    edtConfirmPwd.setError("Password doesn't match.");
                    focusView = edtConfirmPwd;
                    cancelLogin = true;
                }
                if (cancelLogin) {
                    // error in login
                    focusView.requestFocus();
                } else {

                    changePassword();
                }

            }
        });


    

        return rootview;
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
    public void changePassword() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();
            String URL_CHANGE_PASSWORD=MapAppConstant.API + MapAppConstant.CHANGE_PASSWORD;
            Log.e("CHANGE PASSWORD URL",URL_CHANGE_PASSWORD);
            StringRequest sr = new StringRequest(Request.Method.POST, URL_CHANGE_PASSWORD, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("CHANGE PASSWORD RESP", response);

                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Toast.makeText(getActivity(), serverMessage,Toast.LENGTH_LONG).show();

                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {
                                if ("1".equals(serverCode)) {
                                    JSONObject jsonObject=object.getJSONObject("data");

                                    String userSecHash=jsonObject.getString("user_security_hash");
                                    Utils.storeUserPreferences(getActivity(), UserInfo.USER_SECURITY_HASH,userSecHash);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(getActivity(),  MainAcitivity.class);
                            startActivity(intent);
                            getActivity().finish();

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
                        Toast.makeText(getActivity(), "Timeout Error",
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
                    params.put("user_id", Utils.getUserPreferences(getActivity(), UserInfo.USER_ID));
                    params.put("user_security_hash", Utils.getUserPreferences(getActivity(), UserInfo.USER_SECURITY_HASH));
                    params.put("user_login_password", strPwd);
                    params.put("confirm_login_password", strConfirmPwd);
                    Log.e("CHANGE PASSWORD REQ",params.toString());
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
    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    if(getFragmentManager().getBackStackEntryCount() > 0) {


                        getFragmentManager().popBackStack();
                        //CorporateUserMainActivity.txtTitle.setText("Home");
                        MainAcitivity.txtTitle.setText("Home");

                    }



                    return true;

                }

                return false;
            }
        });
    }
}
