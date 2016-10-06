package com.erginus.lawyerapp;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.erginus.lawyerapp.fragment.HomeFragment;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.DateFormat;
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
        txtTitle.setText("Add Date");
        compactCalendar=(CompactCalendarView)findViewById(R.id.compactcalendar_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView=(NavigationView)findViewById(R.id.navigation);
        txtMonth=(TextView)findViewById(R.id.txt_month);
        txtCase=(TextView)findViewById(R.id.txt_caseTitle);

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
            }
        });
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendar.showNextMonth();
            }
        });


        txtMonth.setText(dateFormatForMonth.format(compactCalendar.getFirstDayOfCurrentMonth()));
        loadEvents();
        loadEventsForYear(2016);
        compactCalendar.invalidate();
        logEventsByMonth(compactCalendar);

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
                txtTitle.setText("Home");
                android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, new HomeFragment());
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
                        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new HomeFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
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
}
