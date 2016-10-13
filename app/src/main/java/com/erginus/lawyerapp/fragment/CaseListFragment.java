package com.erginus.lawyerapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import com.erginus.lawyerapp.AddCaseActivity;
import com.erginus.lawyerapp.common.MapAppConstant;
import com.erginus.lawyerapp.common.Prefshelper;
import com.erginus.lawyerapp.common.VolleySingleton;
import com.erginus.lawyerapp.model.CaseListModel;
import com.erginus.lawyerapp.R;
import com.erginus.lawyerapp.SelectDateActivity;
import com.erginus.lawyerapp.adapter.CaseListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaseListFragment extends Fragment {

    List<CaseListModel> caseList;
    ListView listView;
    FloatingActionButton fab;
    Prefshelper prefshelper;
    String startDt, lastDt;

    public CaseListFragment() {
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
        View rootview = inflater.inflate(R.layout.fragment_case_list, container, false);
        prefshelper=new Prefshelper(getActivity());
        listView = (ListView) rootview.findViewById(R.id.listView);
        SelectDateActivity.txtTitle.setText("Case List");
        caseList=new ArrayList<>();
       /* Bundle bundle = this.getArguments();
        startDt = bundle.getString("istDate");
        lastDt = bundle.getString("lastDate");*/

        fab = (FloatingActionButton)rootview.findViewById(R.id.fab);

        fab.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor("#00bcd5")));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), AddCaseActivity.class);
                startActivity(intent);

            }
        });
        caseList();
        return rootview;
    }
    public void caseList() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "list " + MapAppConstant.API + "get_user_cases_by_date");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "get_user_cases_by_date", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());
                    getlist().clear();
                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");

                        if (serverCode.equalsIgnoreCase("0")) {
                            Toast.makeText(getActivity(), serverMessage,Toast.LENGTH_LONG).show();
                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {
                                if ("1".equals(serverCode)) {

                                    JSONArray jsonArray=object.getJSONArray("data");
                                    if(jsonArray.length()>0)
                                    {
                                        for(int i=0; i<jsonArray.length(); i++)
                                        {
                                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                                            JSONArray jsonArray1=jsonObject.getJSONArray("case_details");
                                            if(jsonArray1.length()>0)
                                            {
                                                for(int j=0; j<jsonArray1.length(); j++) {
                                                    JSONObject jsonObject1=jsonArray1.getJSONObject(j);
                                                    String previousDate=jsonObject1.getString("case_detail_previous_date");
                                                    String nextDate=jsonObject1.getString("case_detail_next_date");
                                                    String comment=jsonObject1.getString("case_detail_comment");
                                                    String caseId=jsonObject1.getString("case_id");
                                                    String caseNumber=jsonObject1.getString("case_number");
                                                    String caseTitle=jsonObject1.getString("case_title");
                                                    String caseType=jsonObject1.getString("case_type");
                                                    String casePositionStatus=jsonObject1.getString("case_position_status");
                                                    String retainedName=jsonObject1.getString("case_retained_name");
                                                    String retainedContact=jsonObject1.getString("case_retained_contact");
                                                    String counselorName=jsonObject1.getString("case_opposite_counselor_name");
                                                    String counselorContact=jsonObject1.getString("case_opposite_counselor_contact");
                                                    String courtName=jsonObject1.getString("case_court_name");
                                                    String caseStarted=jsonObject1.getString("case_started");

                                                caseList.add(model(previousDate,nextDate,comment,caseId,caseNumber,caseTitle,caseType,casePositionStatus,
                                                        retainedName,retainedContact,counselorName,counselorContact,courtName,caseStarted));
                                                }

                                                }


                                        }
                                        setlist(caseList);
                                    }
                                    listView.setAdapter(new CaseListAdapter(getActivity(), caseList));
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

                    params.put("user_id", prefshelper.getUserIdFromPreference());
                    params.put("user_security_hash", prefshelper.getUserSecHashFromPreference());
                    params.put("start_date", prefshelper.getIstDay());
                    params.put("end_date", prefshelper.getlastDay());

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
    public List<CaseListModel> getlist() {
        return caseList;
    }
    public void setlist(List<CaseListModel> list) {
        this.caseList = list;
    }
    private CaseListModel model(String previousDt,String nextDt,String commnt,String casId, String caseNmber, String casTitle,
                                String casType,String casePstnStatus,String retainedNm, String retainedCntact,
                                String counselorNm,String counselorContct,String courtNm,String caseStrted)
    {
        CaseListModel model=new CaseListModel();
        model.setCasePrevDate(previousDt);
        model.setNextDate(nextDt);
        model.setComment(commnt);
        model.setCaseId(casId);
        model.setCaseNumber(caseNmber);
        model.setCaseTitle(casTitle);
        model.setCaseType(casType);
        model.setCaseStatus(casePstnStatus);
        model.setRetainName(retainedNm);
        model.setRetainContact(retainedCntact);
        model.setCounsellorName(counselorNm);
        model.setCounsellorContact(counselorContct);
        model.setCourtName(courtNm);
        model.setCaseStartDate(caseStrted);

        return  model;
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
                        SelectDateActivity.txtTitle.setText("Home");

                    }

                    return true;

                }

                return false;
            }
        });
    }

}
