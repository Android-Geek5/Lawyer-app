package com.eweblog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eweblog.common.CircularImageView;
import com.eweblog.common.ConnectionDetector;
import com.eweblog.common.Prefshelper;
import com.eweblog.fragment.AboutUS;
import com.eweblog.fragment.ChangePasswordFragment;
import com.eweblog.fragment.EditProfileFragment;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class CorporateUserMainActivity extends AppCompatActivity {

    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private CharSequence mDrawerTitle;
    FrameLayout f;
    ActionBarDrawerToggle mDrawerToggle;
    public static TextView txtTitle;
    Prefshelper prefshelper;
    static TextView text_name, email_name;
    ConnectionDetector cd;
    LinearLayout linearLayout;
    String pic,name,email;
    public static CircularImageView pimage;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_select_date);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtTitle = (TextView) findViewById(R.id.toolbar_title);
        txtTitle.setText("Home");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setItemIconTintList(null);
        prefshelper = new Prefshelper(this);
        cd = new ConnectionDetector(getApplicationContext());

            linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.drawer_header, null);
            navigationView.addHeaderView(linearLayout);
            pimage = (CircularImageView) linearLayout.findViewById(R.id.profile_img);
            text_name = (TextView) linearLayout.findViewById(R.id.txt_usrName);
            email_name = (TextView) linearLayout.findViewById(R.id.txt_userEmail);

            pic = prefshelper.getProfileImage();
            name = prefshelper.getName();
            email = prefshelper.getEmail();
            text_name.setText(name);
            email_name.setText(email);
//            Picasso.with(this).load(pic).into(pimage);



        if (drawerLayout != null) {
            drawerLayout.setDrawerShadow(R.drawable.list_back, GravityCompat.START);

            mDrawerToggle = new ActionBarDrawerToggle(CorporateUserMainActivity.this, drawerLayout,
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
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

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
                        Intent intent = new Intent(CorporateUserMainActivity.this, CorporateUserMainActivity.class);
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
                    case R.id.drawer_edit:
                        txtTitle.setText("Edit Profile");
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new EditProfileFragment());
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
                    case R.id.drawer_view:

                        Intent in = new Intent(CorporateUserMainActivity.this, ViewUsersActivity.class);
                        startActivity(in);
                        return true;
                    case R.id.drawer_case:
                        prefshelper.storeSearch("case_status");
                        Intent intnt = new Intent(CorporateUserMainActivity.this, SearchActivity.class);
                        startActivity(intnt);
                        return true;
                    case R.id.drawer_add:
                        txtTitle.setText("Add Case");
                        Intent intent1 = new Intent(CorporateUserMainActivity.this, AddCaseActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.drawer_logout:

                        final Dialog dialog = new Dialog(CorporateUserMainActivity.this);
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
                                ExitActivity.exitApplication(CorporateUserMainActivity.this);
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

    public void onClick()
    {
        Intent intent = new Intent(CorporateUserMainActivity.this, AddCaseActivity.class);
        startActivity(intent);
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
        final Dialog dialog = new Dialog(CorporateUserMainActivity.this);
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

    public void shareapp()
    {
        String message = "https://play.google.com/store/apps/details?id=com.eweblog";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Share Via"));
    }

}
