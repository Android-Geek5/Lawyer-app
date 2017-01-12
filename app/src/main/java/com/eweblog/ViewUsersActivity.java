package com.eweblog;

import android.content.Intent;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eweblog.adapter.ListAdapter;
import com.eweblog.adapter.ViewUserAdapter;
import com.eweblog.common.SlidingTabLayout;

public class ViewUsersActivity extends AppCompatActivity {
    ViewPager pager;
    ViewUserAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Active","Inactive"};
    int Numboftabs =2;
    LinearLayout llBack;
    TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtTitle = (TextView) findViewById(R.id.toolbar_title);
        txtTitle.setText("View Users");
        adapter =  new ViewUserAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    return getResources().getColor(R.color.tabsScrollColor,null);
                }
                else
                {
                    return getResources().getColor(R.color.tabsScrollColor);
                }
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        llBack=(LinearLayout)findViewById(R.id.imageView_back);

        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
                Intent in = new Intent(ViewUsersActivity.this, SelectDateActivity.class);
                startActivity(in);
            }
        });
    }
}
