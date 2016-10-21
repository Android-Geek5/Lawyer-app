package com.lawyerapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.lawyerapp.common.AlarmReceiver;
import com.lawyerapp.common.MapAppConstant;
import com.lawyerapp.common.Prefshelper;
import com.lawyerapp.common.VolleySingleton;
import com.lawyerapp.fragment.AboutUS;
import com.lawyerapp.fragment.CaseListFragment;
import com.lawyerapp.fragment.ChangePasswordFragment;
import com.lawyerapp.model.CaseListModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
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

    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private CharSequence mDrawerTitle;
    FrameLayout f;
    ActionBarDrawerToggle mDrawerToggle;
    public static TextView txtTitle;
    CompactCalendarView compactCalendar;
    TextView txtMonth, txtCase;
    ImageView imgPrevious, imgNext;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    String event="", e="", nextDate, previousDate, comment;
    Prefshelper prefshelper;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
    List<CaseListModel> caseList;
    FloatingActionButton fab;
    List<String> nextDates = new ArrayList<>();
    List<String> prevDates = new ArrayList<>();
    List<String> comments = new ArrayList<>();
    List<String> displayingDates = new ArrayList<>();
    List<Event> events;
    Date newDt, dateEvent;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_date);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtTitle=(TextView)findViewById(R.id.toolbar_title);
        txtTitle.setText("Home");
        compactCalendar=(CompactCalendarView)findViewById(R.id.compactcalendar_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView=(NavigationView)findViewById(R.id.navigation);
        navigationView.setItemIconTintList(null);
        txtMonth=(TextView)findViewById(R.id.txt_month);
        txtCase=(TextView)findViewById(R.id.txt_caseTitle);
        prefshelper=new Prefshelper(this);


        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.HOUR, 5);
        calendar.set(Calendar.AM_PM, Calendar.PM);
        Intent myIntent = new Intent(this , AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        imgNext=(ImageView)findViewById(R.id.image_next);
        imgPrevious=(ImageView)findViewById(R.id.image_previous);
        compactCalendar.setLocale(TimeZone.getDefault(),Locale.ENGLISH);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        caseList=new ArrayList<>();

        imgPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendar.showPreviousMonth();
                if(prefshelper.getMode().equalsIgnoreCase("offline"))
                {

                }
                else
                {
                    caseList();
                }


            }
        });
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendar.showNextMonth();
                if(prefshelper.getMode().equalsIgnoreCase("offline"))
                {

                }
                else
                {
                    caseList();
                }


            }
        });


        txtMonth.setText(dateFormatForMonth.format(compactCalendar.getFirstDayOfCurrentMonth()));



        // define a listener to receive callbacks when certain events happen.
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendar.getEvents(dateClicked);
                txtCase.setText("");
                e="";
                if(events.size()<=0)
                {
                    txtCase.setText("");
                    e="";
                    txtCase.setText("No Case Found");
                }
                else
                {

                    for(int i=0; i<events.size();i++)
                    {
                        txtCase.setText("");
                        e="";
                        Log.e("eventss sizeeee", events.size()+"");
                        event= String.valueOf(events.get(i).getData());
                        String event1=event.substring(0, event.length()-24);
                        String event2 = event.substring(38, event.length());
                        String newEvent=event1+", "+event2;
                        e = e + "\n " + newEvent;
                        txtCase.setText(e);
                    }
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                dateEvent=firstDayOfNewMonth;
                txtMonth.setText(dateFormatForMonth.format(firstDayOfNewMonth));

                caseList();
                List<Event> events = compactCalendar.getEvents(firstDayOfNewMonth);
                txtCase.setText("");
                e="";
                if(events.size()<=0)
                {
                    txtCase.setText("");
                    e="";
                    txtCase.setText("No Case Found");
                }
                else
                {
                    for(int i=0; i<events.size();i++)
                    {
                        txtCase.setText("");
                        e="";
                        Log.e("eventss sizeeee", events.size()+"");
                        event= String.valueOf(events.get(i).getData());
                        String event1=event.substring(0, event.length()-24);
                        String event2 = event.substring(38, event.length());
                        String newEvent=event1+", "+event2;
                        e = e + "\n " + newEvent;
                        txtCase.setText(e);
                    }
                }

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
        fab = (FloatingActionButton)findViewById(R.id.fab);

        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00bcd5")));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txtTitle.setText("Case List");
                Bundle bundle= new Bundle();
                bundle.putSerializable("list", (Serializable) caseList);
                android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                CaseListFragment caseListFragment=new CaseListFragment();
                caseListFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.content_frame, caseListFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
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
                        overridePendingTransition(0,0);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.drawer_list:
                        txtTitle.setText("Case List");
                        Bundle bundle= new Bundle();
                        bundle.putSerializable("list", (Serializable) caseList);
                        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                        CaseListFragment caseListFragment=new CaseListFragment();
                        caseListFragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.content_frame, caseListFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.changePwd:
                        txtTitle.setText("Change Password");
                        fragmentManager=getSupportFragmentManager();
                        fragmentTransaction=fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new ChangePasswordFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.about_us:
                        txtTitle.setText("About us");
                        fragmentManager=getSupportFragmentManager();
                        fragmentTransaction=fragmentManager.beginTransaction();
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
                        prefshelper.getPreferences().edit().clear().commit();
                        ExitActivity.exitApplication(SelectDateActivity.this);

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
        if(prefshelper.getMode().equalsIgnoreCase("offline"))
        {

        }
        else {


        }
           caseList();
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

    private void loadEvents() {
        addEvents(-1, -1);
    }


    private void addEvents(int month, int year) {

        Log.e("size", nextDates.size()+"");

               if(nextDates.size()>0) {
                   for (int i = 0; i < nextDates.size(); i++) {

                       try {
                           newDt = dateFormatForDisplaying.parse(nextDates.get(i));

                           currentCalender.setTime(formatter.parse(formatter.format(newDt)));

                           if (month > -1) {
                               currentCalender.set(Calendar.MONTH, month);
                           }
                           if (year > -1) {
                               currentCalender.set(Calendar.ERA, GregorianCalendar.AD);
                               currentCalender.set(Calendar.YEAR, year);
                           }
                       } catch (ParseException e1) {
                           e1.printStackTrace();
                       }

                       long timeInMillis = currentCalender.getTimeInMillis();


                       events = getEvents(timeInMillis, i);

                       compactCalendar.addEvents(events);
                   }
                   if (events.size() <= 0) {
                       txtCase.setText("");
                       e = "";
                       txtCase.setText("No Case Found");
                   } else {

                       for (int k = 0; k <events.size(); k++) {
                           txtCase.setText("");
                           e = "";
                           Log.e("eventss sizeeee", events.size() + "");
                           event = String.valueOf(events.get(k).getData());

                              String event1 = event.substring(0, event.length() - 24);

                              String event2 = event.substring(38, event.length());

                              String newEvent = event1 + ", " + event2;
                              e = e + "\n " + newEvent;
                              txtCase.setText(e);

                       }
                   }

        }
    }

    private List<Event> getEvents(long timeInMillis, int day) {

        if (day < 2) {
            return Arrays.asList(new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Case on " + new Date(timeInMillis)));
        } else if ( day > 2 && day <= 4) {
            return Arrays.asList(
                    new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Case on " + new Date(timeInMillis)),
                    new Event(Color.argb(255, 100, 68, 65), timeInMillis, "Case on " + new Date(timeInMillis)));
        } else {
            return Arrays.asList(
                    new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Case on " + new Date(timeInMillis) ),
                    new Event(Color.argb(255, 100, 68, 65), timeInMillis, "Case on " + new Date(timeInMillis)),
                    new Event(Color.argb(255, 70, 68, 65), timeInMillis, "Case on " + new Date(timeInMillis)));
        }
    }


    @Override
    public  void onBackPressed()
    {
        new AlertDialog.Builder(this)
                .setTitle("Alert !")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);

                    }
                }).create().show();
    }
    public void shareapp(){
        String message = "http://erginus.com/";
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
                    nextDates.clear();
                     ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");

                        if (serverCode.equalsIgnoreCase("0")) {
                           // Toast.makeText(SelectDateActivity.this, serverMessage,Toast.LENGTH_LONG).show();
                            prefshelper.message(serverMessage);
                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            prefshelper.message("");
                            try {
                                if ("1".equals(serverCode)) {

                                    JSONArray jsonArray=object.getJSONArray("data");
                                    if(jsonArray.length()>0)
                                    {
                                        for(int i=0; i<jsonArray.length(); i++)
                                        {
                                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                                                    String caseId=jsonObject.getString("case_id");
                                                    String caseNumber=jsonObject.getString("case_number");
                                                    String caseTitle=jsonObject.getString("case_title");
                                                    String caseType=jsonObject.getString("case_type");
                                                    String casePositionStatus=jsonObject.getString("case_position_status");
                                                    String retainedName=jsonObject.getString("case_retained_name");
                                                    String retainedContact=jsonObject.getString("case_retained_contact");
                                                    String counselorName=jsonObject.getString("case_opposite_counselor_name");
                                                    String counselorContact=jsonObject.getString("case_opposite_counselor_contact");
                                                    String courtName=jsonObject.getString("case_court_name");
                                                    String caseStarted=jsonObject.getString("case_started");
                                                    JSONArray jsonArray2=jsonObject.getJSONArray("case_comments_array");
                                                    if(jsonArray2.length()>0)
                                                    {
                                                        for(int k=0; k<jsonArray2.length(); k++)
                                                        {
                                                            JSONObject jsonObject2=jsonArray2.getJSONObject(k);
                                                            previousDate=jsonObject2.getString("case_detail_previous_date");
                                                            nextDate=jsonObject2.getString("case_detail_next_date");
                                                            comment=jsonObject2.getString("case_detail_comment");
                                                            nextDates.add(nextDate);
                                                            prevDates.add(previousDate);
                                                            comments.add(comment);

                                                        }

                                                    }

                                                    caseList.add(model(caseId,caseNumber,caseTitle,caseType,casePositionStatus,
                                                            retainedName,retainedContact,counselorName,counselorContact,courtName,
                                                            caseStarted, nextDates, prevDates,comments));

                                                }

                                    }
                                    setlist(caseList);

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            compactCalendar.removeAllEvents();
                            loadEvents();
                            compactCalendar.invalidate();
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


    private CaseListModel model(String casId, String caseNmber, String casTitle,
                                   String casType,String casePstnStatus,String retainedNm, String retainedCntact,
                                   String counselorNm,String counselorContct,String courtNm,String caseStrted,
                             List<String> nextDate,List<String> previousDate, List<String> comment)
    {
        CaseListModel model=new CaseListModel();

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
        model.setNextDateArray(nextDate);
        model.setPrevDateArray(previousDate);
        model.setCommentsArray(comment);

        return  model;
    }

}
