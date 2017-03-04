package com.eweblog;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eweblog.common.AlarmReceiver;
import com.eweblog.common.CircularImageView;
import com.eweblog.common.UserInfo;
import com.eweblog.common.ConnectionDetector;
import com.eweblog.fragment.AboutUS;
import com.eweblog.fragment.ChangePasswordFragment;
import com.eweblog.fragment.CorporateUserMainFragment;
import com.eweblog.fragment.EditProfileFragment;
import com.eweblog.fragment.SelectDateFragment;
import com.squareup.picasso.Picasso;


public class MainAcitivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private CharSequence mDrawerTitle;
    FrameLayout f;
    ActionBarDrawerToggle mDrawerToggle;
    public static TextView txtTitle;
    static TextView text_name, email_name;
    android.support.v4.app.FragmentManager fragmentManager;
    android.support.v4.app.FragmentTransaction fragmentTransaction;
    LinearLayout linearLayout, searchLayout,nullLayout;
    String pic,name,email;
    public static CircularImageView pimage;
    boolean paidOrNot,corporateOrNot,childOrNot;
    Toolbar toolbar;
    ConnectionDetector cd;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            //    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        corporateOrNot=Utils.getUserPreferencesBoolean(MainAcitivity.this, UserInfo.CORPORATE_OR_NOT);
        paidOrNot=Utils.getUserPreferencesBoolean(MainAcitivity.this, UserInfo.FREE_OR_PAID);
        childOrNot=Utils.getUserPreferencesBoolean(MainAcitivity.this, UserInfo.CHILD_USER_OR_NOT);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtTitle = (TextView)toolbar. findViewById(R.id.toolbar_title);
        txtTitle.setText("Home");
        searchLayout=(LinearLayout)toolbar.findViewById(R.id.imageView_back2);
        nullLayout=(LinearLayout)toolbar.findViewById(R.id.imageView_back3);
        cd = new ConnectionDetector(getApplicationContext());
        inflateSearch();
        inflateLayout();
        paidLayout();
        hideItem();
        //alarmCancel();
        drawerLayoutAndNavigation();
    }

   /* public void onClick()
    {
        Intent intent = new Intent(MainAcitivity.this, AddCaseActivity.class);
        startActivity(intent);
    }*/

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

    public void inflateLayout()
    {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setItemIconTintList(null);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        /** Show main screen according to type of user-Business or Individual **/
        if(corporateOrNot)
            fragmentTransaction.replace(R.id.content_frame, new CorporateUserMainFragment());
        else
            fragmentTransaction.replace(R.id.content_frame, new SelectDateFragment());

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /** Show if user wants to exit app dialog **/
    @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(MainAcitivity.this);
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
    /** On click share app **/
    public void shareapp()
    {
        String message = "https://play.google.com/store/apps/details?id=com.eweblog";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Share Via"));
    }

    /** Hide items on Navigation drawer according to user type **/
    private void hideItem()
    {
        Menu nav_Menu = navigationView.getMenu();

        if(!corporateOrNot) {
            /**No user other than business can see the option to view child **/
        nav_Menu.findItem(R.id.drawer_view).setVisible(false);
            /** Free users cannot see edit profile and search by status options **/
        if (!paidOrNot) {
            nav_Menu.findItem(R.id.drawer_edit).setVisible(false);
            nav_Menu.findItem(R.id.drawer_case).setVisible(false);
        }
            /** If user is child,he cant have option to add case **/
            if(childOrNot)
            {
                nav_Menu.findItem(R.id.drawer_add).setVisible(false);
            }
    }
    }

    public void paidLayout()
    {
        /** if paid and corporate,show header view on Nav drawer **/
        if(corporateOrNot || paidOrNot) {
            linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.drawer_header, null);
            navigationView.addHeaderView(linearLayout);
            pimage = (CircularImageView) linearLayout.findViewById(R.id.profile_img);
            text_name = (TextView) linearLayout.findViewById(R.id.txt_usrName);
            email_name = (TextView) linearLayout.findViewById(R.id.txt_userEmail);

            name = Utils.getUserPreferences(MainAcitivity.this, UserInfo.USER_NAME);
            email = Utils.getUserPreferences(MainAcitivity.this, UserInfo.USER_EMAIL);
            text_name.setText(name);
            email_name.setText(email);

            String imageProfileUrl=Utils.getUserPreferences(MainAcitivity.this, UserInfo.USER_PROFILE_IMAGE_URL);
            Log.e("IMAGE URL",""+imageProfileUrl);
            if(!(imageProfileUrl).equalsIgnoreCase(""))
            {
                Picasso.with(MainAcitivity.this).load(imageProfileUrl).into(pimage);
            }
        }
    }

    public void drawerLayoutAndNavigation()
    {
        if (drawerLayout != null) {
            drawerLayout.setDrawerShadow(R.drawable.list_back, GravityCompat.START);

            mDrawerToggle = new ActionBarDrawerToggle(MainAcitivity.this, drawerLayout,
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
                        inflateSearch();
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        // Show corporate main screen if business user,otherwise the calender screen
                        if(corporateOrNot)
                            fragmentTransaction.replace(R.id.content_frame, new CorporateUserMainFragment());
                        else
                            fragmentTransaction.replace(R.id.content_frame, new SelectDateFragment());

                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.changePwd:
                        txtTitle.setText("Change Password");
                        searchLayout.setVisibility(View.GONE);
                        nullLayout.setVisibility(View.VISIBLE);
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new ChangePasswordFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.drawer_edit:
                        txtTitle.setText("Edit Profile");
                        searchLayout.setVisibility(View.GONE);
                        nullLayout.setVisibility(View.VISIBLE);
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new EditProfileFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.about_us:
                        txtTitle.setText("About us");
                        searchLayout.setVisibility(View.GONE);
                        nullLayout.setVisibility(View.VISIBLE);
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new AboutUS());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.drawer_add:
                        txtTitle.setText("Add Case");
                        Intent intent1 = new Intent(MainAcitivity.this, AddCaseActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.drawer_view:

                        Intent in = new Intent(MainAcitivity.this, ViewUsersActivity.class);
                        startActivity(in);
                        return true;
                    case R.id.drawer_case:
                        Utils.storeUserPreferences(MainAcitivity.this, UserInfo.SEARCH,"advanced_search");
                        Intent intnt = new Intent(MainAcitivity.this, SearchActivity.class);
                        startActivity(intnt);
                        return true;
                    case R.id.drawer_logout:

                        final Dialog dialog = new Dialog(MainAcitivity.this);
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
                                // Clear preferences here
                                Utils.clearData(MainAcitivity.this);
                                alarmCancel();  // Cancel the alarms
                                Intent intent2=new Intent(MainAcitivity.this,LoginActivity.class);
                                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent2);
                                finish();

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
    // Show search layout for paid users
    public void inflateSearch()
    {
        if(Utils.getUserPreferencesBoolean(MainAcitivity.this, UserInfo.COMMON_PAID)) {
            searchLayout.setVisibility(View.VISIBLE);
            nullLayout.setVisibility(View.GONE);
            searchLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.storeUserPreferences(MainAcitivity.this, UserInfo.SEARCH, "simple_search");
                    Intent intnt = new Intent(MainAcitivity.this, SearchActivity.class);
                    startActivity(intnt);
                }
            });
        }
        else
        {
            searchLayout.setVisibility(View.GONE);
            nullLayout.setVisibility(View.VISIBLE);
        }
    }
    // Cancel the alarm
    public void alarmCancel()
    {
        Intent intent1 = new Intent(this.getApplicationContext(), AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getActivity(this.getApplicationContext(),4, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(sender);
        sender.cancel();
    }
}
