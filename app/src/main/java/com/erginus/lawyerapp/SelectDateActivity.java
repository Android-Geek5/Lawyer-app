package com.erginus.lawyerapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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

import com.erginus.lawyerapp.common.AlarmReceiver;
import com.erginus.lawyerapp.common.Prefshelper;
import com.erginus.lawyerapp.fragment.AboutUS;
import com.erginus.lawyerapp.fragment.ChangePasswordFragment;
import com.erginus.lawyerapp.fragment.CaseListFragment;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;



public class SelectDateActivity extends AppCompatActivity {

    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.getDefault());
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private CharSequence mDrawerTitle;
    FrameLayout f;
    ActionBarDrawerToggle mDrawerToggle;
    public static TextView txtTitle;
    CompactCalendarView compactCalendar;
    TextView txtMonth, txtCase,  txtMore;
    ImageView imgPrevious, imgNext;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    List<String> list;
    String event="", e="";
    Prefshelper prefshelper;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    Date newDate;

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
        txtMore=(TextView)findViewById(R.id.txt_more);
        imgNext=(ImageView)findViewById(R.id.image_next);
        imgPrevious=(ImageView)findViewById(R.id.image_previous);
        compactCalendar.setLocale(TimeZone.getDefault(),Locale.ENGLISH);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        list=new ArrayList<>();

        imgPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendar.showPreviousMonth();
                prefshelper.storeFirstDay(String.valueOf(compactCalendar.getFirstDayOfCurrentMonth()));
                String date= String.valueOf(Calendar.DAY_OF_MONTH+Calendar.MONTH+Calendar.YEAR);
                try {
                    newDate=dateFormat.parse(date);

                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                if(compactCalendar.getFirstDayOfCurrentMonth().equals(newDate))
                {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.MONTH, +0);
                    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                    Date lastDayOfMonth = cal.getTime();
                    prefshelper.storeLastDay(String.valueOf(lastDayOfMonth));
                }
                else {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.MONTH, -1);
                    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                    Date lastDayOfMonth = cal.getTime();
                    prefshelper.storeLastDay(String.valueOf(lastDayOfMonth));
                }
            }
        });
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendar.showNextMonth();
                prefshelper.storeFirstDay(String.valueOf(compactCalendar.getFirstDayOfCurrentMonth()));

                String date= String.valueOf(Calendar.DAY_OF_MONTH+Calendar.MONTH+Calendar.YEAR);
                try {
                    newDate=dateFormat.parse(date);

                } catch (ParseException e1) {
                    e1.printStackTrace();
                }

                if(compactCalendar.getFirstDayOfCurrentMonth().equals(newDate))
                {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.MONTH, +0);
                    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                    Date lastDayOfMonth = cal.getTime();
                    prefshelper.storeLastDay(String.valueOf(lastDayOfMonth));
                }
                else {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.MONTH, +1);
                    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                    Date lastDayOfMonth = cal.getTime();
                    prefshelper.storeLastDay(String.valueOf(lastDayOfMonth));
                }
            }
        });


        txtMonth.setText(dateFormatForMonth.format(compactCalendar.getFirstDayOfCurrentMonth()));
        loadEvents();
        loadEventsForYear(Calendar.YEAR);
        compactCalendar.invalidate();
        logEventsByMonth(compactCalendar);
        prefshelper.storeFirstDay(String.valueOf(compactCalendar.getFirstDayOfCurrentMonth()));

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, +0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date lastDayOfMonth = cal.getTime();
        prefshelper.storeLastDay(String.valueOf(lastDayOfMonth));

        // define a listener to receive callbacks when certain events happen.
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                txtCase.setText("");
                e="";
                List<Event> events = compactCalendar.getEvents(dateClicked);
               /* DateFormat dateFormat = new SimpleDateFormat("dd");
                Calendar cal = Calendar.getInstance();
                String slctdDt=dateFormat.format(cal.getTime());
                DateFormat format = new SimpleDateFormat("dd", Locale.ENGLISH);
                Date date = null;
                try {
                    date= format.parse(slctdDt);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                if(dateClicked.equals(date))
                {
                    if(events.size()<=0)
                    {
                        txtCase.setText("No Case Found");
                    }
                    else
                    {

                        for(int i=0; i<events.size();i++)
                        {

                            event= String.valueOf(events.get(i).getData());
                            e=e+ " \n"+event;
                            txtCase.setText(e);
                        }



                    }
                }*/
                if(events.size()<=0)
                {
                    txtCase.setText("No Case Found");
                }
                else
                {

                    for(int i=0; i<events.size();i++)
                    {

                        event= String.valueOf(events.get(i).getData());
                        e=e+ "\n "+event;
                        txtCase.setText(e);
                    }
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                txtMonth.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });
        txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
                Date lastDayOfMonth = cal.getTime();


                txtTitle.setText("Case List");
               /* Bundle bundle = new Bundle();
                bundle.putString("istDate", String.valueOf(compactCalendar.getFirstDayOfCurrentMonth()) );
                bundle.putString("lastDate",String.valueOf(lastDayOfMonth));*/
                android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                CaseListFragment caseListFragment=new CaseListFragment();
                //caseListFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.content_frame, caseListFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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
                        overridePendingTransition(0,0);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.drawer_list:
                        txtTitle.setText("Case List");
                        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new CaseListFragment());
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
                        ExitActivity.exitApplication(SelectDateActivity.this);
                        prefshelper.getPreferences().edit().clear().commit();
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
        Calendar calendar =  Calendar.getInstance();
        //calendar.set(2014,Calendar.getInstance().get(Calendar.MONTH),Calendar.SUNDAY , 8, 00, 00);
        calendar.set(2016,10,13,12,22,00);
        long when = calendar.getTimeInMillis();
        // notification time
        Log.d("time", when+" ");
        Intent intentAlarm = new Intent(this, AlarmReceiver.class);

         // create the object
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, when, AlarmManager.INTERVAL_DAY * 7, PendingIntent.getBroadcast(this,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
        //set the alarm for particular time
        //alarmManager.set(AlarmManager.RTC_WAKEUP,when, PendingIntent.getBroadcast(this,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
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
        addEvents(Calendar.DECEMBER, -1);
        addEvents(Calendar.NOVEMBER, -1);
    }

    private void loadEventsForYear(int year) {
        addEvents(Calendar.DECEMBER, year);
        addEvents(Calendar.NOVEMBER, year);
    }

    private void logEventsByMonth(CompactCalendarView compactCalendarView) {
        currentCalender.setTime(new Date());
        currentCalender.set(Calendar.DAY_OF_MONTH, 1);
        currentCalender.set(Calendar.MONTH, Calendar.OCTOBER);
        List<String> dates = new ArrayList<>();
        for (Event e : compactCalendarView.getEventsForMonth(new Date())) {
            dates.add(dateFormatForDisplaying.format(e.getTimeInMillis()));
        }
        Log.d("Select Date", "Events for Aug with simple date formatter: " + dates);
        Log.d("Select Date", "Events for Aug month using default local and timezone: " + compactCalendarView.getEventsForMonth(currentCalender.getTime()));
    }

    private void addEvents(int month, int year) {
        currentCalender.setTime(new Date());
        currentCalender.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = currentCalender.getTime();
        for (int i = 0; i < 6; i++) {
            currentCalender.setTime(firstDayOfMonth);
            if (month > -1) {
                currentCalender.set(Calendar.MONTH, month);
            }
            if (year > -1) {
                currentCalender.set(Calendar.ERA, GregorianCalendar.AD);
                currentCalender.set(Calendar.YEAR, year);
            }
            currentCalender.add(Calendar.DATE, i);
            setToMidnight(currentCalender);
            long timeInMillis = currentCalender.getTimeInMillis();

            List<Event> events = getEvents(timeInMillis, i);

            compactCalendar.addEvents(events);
        }
    }

    private List<Event> getEvents(long timeInMillis, int day) {
        if (day < 2) {
            return Arrays.asList(new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis)));
        } else if ( day > 2 && day <= 4) {
            return Arrays.asList(
                    new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis)),
                    new Event(Color.argb(255, 100, 68, 65), timeInMillis, "Event 2 at " + new Date(timeInMillis)));
        } else {
            return Arrays.asList(
                    new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis) ),
                    new Event(Color.argb(255, 100, 68, 65), timeInMillis, "Event 2 at " + new Date(timeInMillis)),
                    new Event(Color.argb(255, 70, 68, 65), timeInMillis, "Event 3 at " + new Date(timeInMillis)));
        }
    }

    private void setToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
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
}
