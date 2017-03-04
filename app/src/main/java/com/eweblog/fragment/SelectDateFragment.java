package com.eweblog.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
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
import com.eweblog.MainAcitivity;
import com.eweblog.R;
import com.eweblog.Utils;
import com.eweblog.ViewCaseListActivity;
import com.eweblog.common.UserInfo;
import com.eweblog.common.ConnectionDetector;
import com.eweblog.common.MapAppConstant;
import com.eweblog.common.VolleySingleton;
import com.eweblog.model.CaseList;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
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
    String  daySelected;
    FloatingActionButton fab;
     List<CaseList> allCaseList = new ArrayList<>();
    List<CaseList> searchedList=new ArrayList<>();
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
        //userInfo=new UserInfo(getActivity());
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
            Gson gson = new Gson();
            String json = Utils.getUserPreferences(getActivity(), UserInfo.ALL_CASE_LIST);
            Type type = new TypeToken<List<CaseList>>(){}.getType();
            List<CaseList> list=gson.fromJson(json, type);
            allCaseList.addAll(list);
        }
         if(Utils.getUserPreferencesBoolean(getActivity(), UserInfo.CHILD_USER_OR_NOT))
         {
             fab.setVisibility(View.GONE);
         }
        else
         {
             fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00bcd5")));

             fab.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {

                     Intent intent = new Intent(getActivity(), AddCaseActivity.class);
                     startActivity(intent);

                 }
             });
         }
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
                Utils.storeUserPreferences(getActivity(), UserInfo.SELECTED_DATE,dateFormat.format(dateClicked));
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

                Log.e("day selected", daySelected+"  "+allCaseList.size());

                if (cd.isConnectingToInternet())
                {
                    caseList();
                }
                else
                {
                     searchedList.clear();
                    if(allCaseList.size()>0)
                    {
                        for(int i=0; i<allCaseList.size(); i++)
                        {

                            try {
                                Date date3=dateFormatForDisplaying.parse(daySelected);
                                Log.e("date",allCaseList.get(i).getCase_date()+"  "+dateFormat.format(date3));
                                if ((allCaseList.get(i).getCase_date()).equalsIgnoreCase(dateFormat.format(date3)))
                                {
                                    searchedList.add(allCaseList.get(i));
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }



                        }
                    }
                    if(searchedList.size()>0)
                    {
                        Intent intent=new Intent(getActivity(), ViewCaseListActivity.class);
                        intent.putParcelableArrayListExtra(UserInfo.SEARCHED_CASE_LIST, (ArrayList<? extends Parcelable>) searchedList);
                        startActivity(intent);
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
            StringRequest sr = new StringRequest(Request.Method.POST, URL_GET_CASES_LIST, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("GET CASES RESPONSE",response);
                    //getlist().clear();
                    //getNlist().clear();
                    searchedList.clear();

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

                                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                                            CaseList caseListObject =new CaseList();
                                            caseListObject.setCase_id(jsonObject.getInt(UserInfo.CASE_ID));
                                            Date date1 = dateFormatForDisplaying.parse(daySelected);
                                            caseListObject.setCase_date(dateFormat.format(date1));
                                            caseListObject.setCase_title(jsonObject.getString(UserInfo.CASE_TITLE));
                                            searchedList.add(caseListObject);


                                        }

                                    }

                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Intent intent=new Intent(getActivity(), ViewCaseListActivity.class);
                            intent.putParcelableArrayListExtra(UserInfo.SEARCHED_CASE_LIST, (ArrayList<? extends Parcelable>) searchedList);
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

                    params.put("user_id", Utils.getUserPreferences(getActivity(), UserInfo.USER_ID));
                    params.put("user_security_hash", Utils.getUserPreferences(getActivity(), UserInfo.USER_SECURITY_HASH));
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
            String GET_ALL_CASES_URL=MapAppConstant.API+MapAppConstant.GET_USER_ALL_CASES;
            Log.e("GET ALL CASES URL", GET_ALL_CASES_URL);
            StringRequest sr = new StringRequest(Request.Method.POST,GET_ALL_CASES_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("GET ALL CASES RESPONSE", response);
                    allCaseList.clear();
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
                                    Log.e("json array",""+jsonArray.length());
                                    if (jsonArray.length() > 0)
                                    {
                                        for (int i = 0; i < jsonArray.length(); i++)
                                        {

                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String date=jsonObject.getString("date");
                                            JSONArray jsonArray1=jsonObject.getJSONArray("details");
                                            Log.e("json array",""+jsonArray1.length());
                                            if(jsonArray1.length()>0)
                                            {
                                                for (int j = 0; j < jsonArray1.length(); j++)
                                                {
                                                    JSONObject jsonObject1=jsonArray1.getJSONObject(j);
                                                    CaseList caseListObject =new CaseList();
                                                    caseListObject.setCase_id(jsonObject1.getInt(UserInfo.CASE_ID));
                                                    Date date2 = dateFormatForDisplaying.parse(date);
                                                    caseListObject.setCase_date(dateFormat.format(date2));
                                                    caseListObject.setCase_title(jsonObject1.getString(UserInfo.CASE_TITLE));
                                                    allCaseList.add(caseListObject);

                                                }
                                            }
                                        }
                                    }
                                    Gson gson1 = new Gson();
                                    String json1 = gson1.toJson(allCaseList);
                                    Utils.storeUserPreferences(getActivity(), UserInfo.ALL_CASE_LIST,json1);

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
                    params.put("user_id", Utils.getUserPreferences(getActivity(), UserInfo.USER_ID));
                    params.put("user_security_hash", Utils.getUserPreferences(getActivity(), UserInfo.USER_SECURITY_HASH));

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
        //     getView().setFocusableInTouchMode(true);
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
