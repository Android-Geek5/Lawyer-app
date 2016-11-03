package com.eweblog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.eweblog.common.ConnectionDetector;
import com.eweblog.common.MapAppConstant;
import com.eweblog.common.Prefshelper;
import com.eweblog.common.VolleySingleton;
import com.eweblog.fragment.AboutUS;
import com.eweblog.fragment.CaseListFragment;
import com.eweblog.fragment.ChangePasswordFragment;
import com.eweblog.model.CaseListModel;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


public class SelectDateActivity extends AppCompatActivity {

    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private CharSequence mDrawerTitle;
    FrameLayout f;
    ActionBarDrawerToggle mDrawerToggle;
    public static TextView txtTitle;
    CompactCalendarView compactCalendar;
    TextView txtMonth;
    ImageView imgPrevious, imgNext;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM yyyy", Locale.US);
    String  nextDate, comment, daySelected;
    Prefshelper prefshelper;
    FloatingActionButton fab;
    List<CaseListModel> caseList = new ArrayList<>();
    List<CaseListModel> caseListArray=new ArrayList<>();

    Date dateEvent;

    ConnectionDetector cd;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_date);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtTitle = (TextView) findViewById(R.id.toolbar_title);
        txtTitle.setText("Home");
        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setItemIconTintList(null);
        txtMonth = (TextView) findViewById(R.id.txt_month);
       // txtCase = (TextView) findViewById(R.id.txt_caseTitle);
        prefshelper = new Prefshelper(this);
        cd = new ConnectionDetector(getApplicationContext());
        imgNext = (ImageView) findViewById(R.id.image_next);
        imgPrevious = (ImageView) findViewById(R.id.image_previous);
        compactCalendar.setLocale(TimeZone.getDefault(), Locale.ENGLISH);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        compactCalendar.setSelected(true);


        imgPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendar.showPreviousMonth();
                if (cd.isConnectingToInternet()) {

                } else  {
                    caseList = prefshelper.getList();

                }


            }
        });
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendar.showNextMonth();
                if (cd.isConnectingToInternet()) {

                } else {
                    caseList = prefshelper.getList();

                }


            }
        });

        fab = (FloatingActionButton)findViewById(R.id.fab);

        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00bcd5")));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SelectDateActivity.this, AddCaseActivity.class);
                startActivity(intent);

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
                Log.e("day selected", daySelected);

                if (cd.isConnectingToInternet()) {
                    caseList();
                } else  {
                    caseList = prefshelper.getList();

                    Log.e("list locl", prefshelper.getList()+"");
                }

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                dateEvent = firstDayOfNewMonth;
                txtMonth.setText(dateFormatForMonth.format(firstDayOfNewMonth));

            }
        });


        if (drawerLayout != null) {
            drawerLayout.setDrawerShadow(R.drawable.list_back, GravityCompat.START);

            mDrawerToggle = new ActionBarDrawerToggle(SelectDateActivity.this, drawerLayout,
                    toolbar, R.string.drawer_open, R.string.drawer_close) {

                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    invalidateOptionsMenu();
                }

                public void onDrawerOpened(View drawerView) {
                    getSupportActionBar().setTitle(mDrawerTitle);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    super.onDrawerOpened(drawerView);
                    invalidateOptionsMenu();

                }
            };
            drawerLayout.setDrawerListener(mDrawerToggle);

        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.drawer_home:
                        txtTitle.setText("Home");
                        Intent intent = new Intent(SelectDateActivity.this, SelectDateActivity.class);
                        overridePendingTransition(0, 0);
                        startActivity(intent);
                        finish();
                        return true;

                    case R.id.changePwd:
                        txtTitle.setText("Change Password");
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new ChangePasswordFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.about_us:
                        txtTitle.setText("About us");
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new AboutUS());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.drawer_add:
                        txtTitle.setText("Add Case");
                        Intent intent1 = new Intent(SelectDateActivity.this, AddCaseActivity.class);
                        startActivity(intent1);
                        finish();
                        return true;
                    case R.id.drawer_logout:

                        final Dialog dialog = new Dialog(SelectDateActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.back_layout);
                        TextView txt = (TextView) dialog.findViewById(R.id.text);
                        txt.setText("Are you sure you want to logout ?");
                        Button yes = (Button) dialog.findViewById(R.id.bt_yes);
                        Button no = (Button) dialog.findViewById(R.id.bt_no);
                        no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                prefshelper.getPreferences().edit().clear().apply();
                                ExitActivity.exitApplication(SelectDateActivity.this);
                            }
                        });
                        dialog.show();
                        return true;

                    case R.id.drawer_share:
                        shareapp();
                        return true;
                    // For rest of the options we just show a toast on click

                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

    }

    @Override

    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.

        mDrawerToggle.syncState();

    }


    @Override

    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        // Pass any configuration change to the drawer toggles

        mDrawerToggle.onConfigurationChanged(newConfig);

    }



    @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(SelectDateActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.back_layout);

        Button yes = (Button) dialog.findViewById(R.id.bt_yes);
        Button no = (Button) dialog.findViewById(R.id.bt_no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);


            }
        });
        dialog.show();
    }

    public void shareapp() {
        String message = "https://play.google.com/store/apps/details?id=com.eweblog";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Share Via"));
    }

    public void caseList() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(SelectDateActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "list " + MapAppConstant.API + "get_user_cases");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "get_user_cases", new Response.Listener<String>() {
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

                        if (serverCode.equalsIgnoreCase("0"))
                        {

                            Toast.makeText(SelectDateActivity.this, serverMessage,Toast.LENGTH_LONG).show();

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
                                    prefshelper.setList(caseList);


                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("list", (Serializable) caseList);
                            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
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
                        Toast.makeText(SelectDateActivity.this, "Timeout Error",
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
                    params.put("case_detail_hearing_date",daySelected);
                    return params;
                }
            };
            sr.setShouldCache(true);

            sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(SelectDateActivity.this.getApplicationContext()).addToRequestQueue(sr);

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

    private CaseListModel model2( String caseid,String nextDate, String comment) {
        CaseListModel model = new CaseListModel();
        model.setCaseId(caseid);
        model.setNextDate(nextDate);
        model.setComment(comment);
        return model;
    }
}
