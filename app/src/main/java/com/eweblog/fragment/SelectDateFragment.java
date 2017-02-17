package com.eweblog.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.eweblog.AddCaseActivity;
import com.eweblog.R;
import com.eweblog.Utils;
import com.eweblog.common.ConnectionDetector;
import com.eweblog.common.MapAppConstant;
import com.eweblog.common.Prefshelper;
import com.eweblog.common.VolleySingleton;
import com.eweblog.model.CaseListModel;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class SelectDateFragment extends Fragment {
    CompactCalendarView compactCalendar;
    TextView txtMonth;
    ImageView imgPrevious, imgNext;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM yyyy", Locale.US);
    String  nextDate, comment, daySelected;
    Prefshelper prefshelper;
    FloatingActionButton fab;
    List<CaseListModel> caseList = new ArrayList<>();
    List<CaseListModel> caseListArray=new ArrayList<>();
    List<CaseListModel> allCaseList = new ArrayList<>();
    List<CaseListModel> allCaseListArray=new ArrayList<>();
    List<CaseListModel> searchedList=new ArrayList<>();
    Date dateEvent;
    ConnectionDetector cd;
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
    FrameLayout f;
    public SelectDateFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_select_date, container, false);
        prefshelper=new Prefshelper(getActivity());
        compactCalendar = (CompactCalendarView)rootView.findViewById(R.id.compactcalendar_view);
        txtMonth = (TextView) rootView.findViewById(R.id.txt_month);
        fab = (FloatingActionButton)rootView.findViewById(R.id.fabbutton);
        cd = new ConnectionDetector(getActivity().getApplicationContext());
        imgNext = (ImageView)rootView.findViewById(R.id.image_next);
        imgPrevious = (ImageView)rootView.findViewById(R.id.image_previous);
        if (cd.isConnectingToInternet())
        {
            getAllCases();
        }
        else
        {
            allCaseList = prefshelper.getList();

        }

        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00bcd5")));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), AddCaseActivity.class);
                startActivity(intent);

            }
        });
        calender();
        return rootView;
    }
    public void calender()
    {
        compactCalendar.setLocale(TimeZone.getDefault(), Locale.ENGLISH);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        compactCalendar.setSelected(true);


        imgPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendar.showPreviousMonth();
            }
        });
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendar.showNextMonth();
            }
        });
        txtMonth.setText(dateFormatForMonth.format(compactCalendar.getFirstDayOfCurrentMonth()));


        // define a listener to receive callbacks when certain events happen.
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    compactCalendar.setCurrentDayBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                }
                else
                {
                    compactCalendar.setCurrentDayBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                daySelected = dateFormatForDisplaying.format(dateClicked);
                Utils.storeUserPreferences(getActivity(),Prefshelper.SELECTED_DATE,dateFormat.format(dateClicked));
                Calendar cal = Calendar.getInstance();
                Date sysDate = cal.getTime();
                if(daySelected.equalsIgnoreCase(dateFormatForDisplaying.format(sysDate)))
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        compactCalendar.setCurrentDayBackgroundColor(getResources().getColor(R.color.buttonbg, null));
                    }
                    else
                    {
                        compactCalendar.setCurrentDayBackgroundColor(getResources().getColor(R.color.buttonbg));
                    }
                }

                Log.e("day selected", daySelected);

                if (cd.isConnectingToInternet())
                {
                    caseList();
                }
                else
                {

                    if(allCaseList.size()>0)
                    {
                        for(int i=0; i<allCaseList.size(); i++)
                        {

                            if ((allCaseList.get(i).getDate()).equalsIgnoreCase(daySelected))
                            {
                                searchedList.add(allCaseList.get(i));
                            }


                        }
                    }
                    if(searchedList.size()>0)
                    {
                        Bundle bundle = new Bundle();

                        bundle.putSerializable("list", (Serializable) searchedList);
                        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        CaseListFragment caseListFragment = new CaseListFragment();
                        caseListFragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.content_frame, caseListFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "No cases found",Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                dateEvent = firstDayOfNewMonth;
                txtMonth.setText(dateFormatForMonth.format(firstDayOfNewMonth));

            }
        });

    }

    public void caseList() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String URL_GET_CASES_LIST= MapAppConstant.API + MapAppConstant.GET_USER_CASES;
            Log.e("GET CASES URL", URL_GET_CASES_LIST);
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "get_user_cases", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("GET CASES RESPONSE",response);
                    getlist().clear();
                    getNlist().clear();

                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");

                        if (serverCode.equalsIgnoreCase("0"))
                        {

                            Toast.makeText(getActivity(), serverMessage,Toast.LENGTH_SHORT).show();

                        }
                        if (serverCode.equalsIgnoreCase("1")) {

                            try {
                                if ("1".equals(serverCode)) {

                                    JSONArray jsonArray = object.getJSONArray("data");
                                    if (jsonArray.length() > 0) {

                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                                            String caseId = jsonObject.getString("case_id");
                                            String caseNumber = jsonObject.getString("case_number");
                                            String caseTitle = jsonObject.getString("case_title");
                                            String caseType = jsonObject.getString("case_type");
                                            String casePositionStatus = jsonObject.getString("case_position_status");
                                            String retainedName = jsonObject.getString("case_retained_name");
                                            String retainedContact = jsonObject.getString("case_retained_contact");
                                            String counselorName = jsonObject.getString("case_opposite_counselor_name");
                                            String counselorContact = jsonObject.getString("case_opposite_counselor_contact");
                                            String courtName = jsonObject.getString("case_court_name");
                                            String caseStarted = jsonObject.getString("case_start_date");
                                            JSONArray jsonArray2 = jsonObject.getJSONArray("case_details_array");
                                            if (jsonArray2.length() > 0) {

                                                for (int k = 0; k < jsonArray2.length(); k++) {
                                                    JSONObject jsonObject2 = jsonArray2.getJSONObject(k);
                                                    nextDate = jsonObject2.getString("case_detail_hearing_date");
                                                    comment = jsonObject2.getString("case_detail_comment");
                                                    caseListArray.add(model2(caseId,nextDate, comment));

                                                }
                                            }
                                            setNlist(caseListArray);
                                            caseList.add(model(caseId, caseNumber, caseTitle, caseType, casePositionStatus,
                                                    retainedName, retainedContact, counselorName, counselorContact, courtName,
                                                    caseStarted,caseListArray));

                                        }

                                    }

                                    setlist(caseList);
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Bundle bundle = new Bundle();

                            bundle.putSerializable("list", (Serializable) caseList);
                            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            CaseListFragment caseListFragment = new CaseListFragment();
                            caseListFragment.setArguments(bundle);
                            fragmentTransaction.replace(R.id.content_frame, caseListFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
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

                    params.put("user_id", Utils.getUserPreferences(getActivity(),Prefshelper.USER_ID));
                    params.put("user_security_hash", Utils.getUserPreferences(getActivity(),Prefshelper.USER_SECURITY_HASH));
                    params.put("case_detail_hearing_date",daySelected);
                    Log.e("GET CASES REQUEST",params.toString());
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
    public void getAllCases() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

            Log.e("", "list " + MapAppConstant.API + "get_user_all_cases");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "get_user_all_cases", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response);
                    getAllCaseList().clear();
                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        if (serverCode.equalsIgnoreCase("0"))
                        {

                        }
                        if (serverCode.equalsIgnoreCase("1"))
                        {
                            try {
                                if ("1".equals(serverCode))
                                {

                                    JSONArray jsonArray=object.getJSONArray("data");
                                    if (jsonArray.length() > 0)
                                    {

                                        for (int i = 0; i < jsonArray.length(); i++)
                                        {

                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String date=jsonObject.getString("date");
                                            JSONArray jsonArray1=jsonObject.getJSONArray("details");
                                            if(jsonArray1.length()>0)
                                            {

                                                for (int j = 0; j < jsonArray1.length(); j++)
                                                {

                                                    JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                                                    String caseId = jsonObject2.getString("case_id");
                                                    String caseNumber = jsonObject2.getString("case_number");
                                                    String caseTitle = jsonObject2.getString("case_title");
                                                    String caseType = jsonObject2.getString("case_type");
                                                    String casePositionStatus = jsonObject2.getString("case_position_status");
                                                    String retainedName = jsonObject2.getString("case_retained_name");
                                                    String retainedContact = jsonObject2.getString("case_retained_contact");
                                                    String counselorName = jsonObject2.getString("case_opposite_counselor_name");
                                                    String counselorContact = jsonObject2.getString("case_opposite_counselor_contact");
                                                    String courtName = jsonObject2.getString("case_court_name");
                                                    String caseStarted = jsonObject2.getString("case_start_date");
                                                    JSONArray jsonArray2 = jsonObject2.getJSONArray("case_details_array");

                                                    if (jsonArray2.length() > 0)
                                                    {

                                                        for (int k = 0; k < jsonArray2.length(); k++)
                                                        {
                                                            JSONObject jsonObject3 = jsonArray2.getJSONObject(k);
                                                            nextDate = jsonObject3.getString("case_detail_hearing_date");
                                                            comment = jsonObject3.getString("case_detail_comment");
                                                            allCaseListArray.add(model2(caseId,nextDate, comment));

                                                        }
                                                    }
                                                    setAllCaseListArray(allCaseListArray);
                                                    allCaseList.add(model3(date,caseId, caseNumber, caseTitle, caseType, casePositionStatus,
                                                            retainedName, retainedContact, counselorName, counselorContact, courtName,
                                                            caseStarted,allCaseListArray));

                                                }
                                            }

                                        }

                                    }

                                    setAllCaseList(allCaseList);
                                    prefshelper.setList(allCaseList);
                                    Log.e("list",allCaseList.toString());
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
                    params.put("user_id", Utils.getUserPreferences(getActivity(),Prefshelper.USER_ID));
                    params.put("user_security_hash", Utils.getUserPreferences(getActivity(),Prefshelper.USER_SECURITY_HASH));

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
    public List<CaseListModel> getNlist() {
        return caseListArray;
    }

    public void setNlist(List<CaseListModel> list) {
        this.caseListArray = list;
    }

    public List<CaseListModel> getAllCaseList() {
        return allCaseList;
    }

    public void setAllCaseList(List<CaseListModel> list) {
        this.allCaseList = list;
    }
    public List<CaseListModel> getAllCaseListArray() {
        return allCaseListArray;
    }

    public void setAllCaseListArray(List<CaseListModel> list) {
        this.allCaseListArray = list;
    }


    private CaseListModel model(String casId, String caseNmber, String casTitle,
                                String casType, String casePstnStatus, String retainedNm, String retainedCntact,
                                String counselorNm, String counselorContct, String courtNm, String caseStrted,
                                List<CaseListModel> list) {
        CaseListModel model = new CaseListModel();
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
        model.setArrayCaseList(list);

        return model;
    }
    private CaseListModel model3(String date,String casId, String caseNmber, String casTitle,
                                 String casType, String casePstnStatus, String retainedNm, String retainedCntact,
                                 String counselorNm, String counselorContct, String courtNm, String caseStrted,
                                 List<CaseListModel> list) {
        CaseListModel model = new CaseListModel();
        model.setDate(date);
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
        model.setArrayCaseList(list);

        return model;
    }

    private CaseListModel model2( String caseid,String nextDate, String comment) {
        CaseListModel model = new CaseListModel();
        model.setCaseId(caseid);
        model.setNextDate(nextDate);
        model.setComment(comment);
        return model;
    }

}
