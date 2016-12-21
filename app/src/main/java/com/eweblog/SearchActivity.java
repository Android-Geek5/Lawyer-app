package com.eweblog;

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
import com.eweblog.adapter.ViewPagerAdapter;
import com.eweblog.common.Prefshelper;
import com.eweblog.common.SlidingTabLayout;

public class SearchActivity extends AppCompatActivity {
    ViewPager pager;
    ListAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Weekly","All"};
    int Numboftabs =2;
    LinearLayout llBack;
    TextView txtType, txtCaseTitle, txtJudge;
    Prefshelper prefshelper;
    LinearLayout llFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefshelper=new Prefshelper(this);

       llFilters=(LinearLayout)findViewById(R.id.ll_filter);
        adapter =  new ListAdapter(getSupportFragmentManager(),Titles,Numboftabs);

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
        txtCaseTitle=(TextView)findViewById(R.id.text_title);
        txtType=(TextView)findViewById(R.id.text_type);
        txtJudge=(TextView)findViewById(R.id.text_judge);
        if(prefshelper.getSearch().equalsIgnoreCase("case_status"))
        {
          llFilters.setVisibility(View.GONE);
        }
        else
        {
            llFilters.setVisibility(View.VISIBLE);
        }
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        txtCaseTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    txtCaseTitle.setTextColor(getResources().getColor(R.color.tabsScrollColor,null));
                    txtType.setTextColor(getResources().getColor(R.color.colorText,null));
                    txtJudge.setTextColor(getResources().getColor(R.color.colorText,null));
                }
                else
                {
                    txtCaseTitle.setTextColor(getResources().getColor(R.color.tabsScrollColor));
                    txtType.setTextColor(getResources().getColor(R.color.colorText));
                    txtJudge.setTextColor(getResources().getColor(R.color.colorText));
                }
            }
        });
        txtJudge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    txtJudge.setTextColor(getResources().getColor(R.color.tabsScrollColor,null));
                    txtType.setTextColor(getResources().getColor(R.color.colorText,null));
                    txtCaseTitle.setTextColor(getResources().getColor(R.color.colorText,null));
                }
                else
                {
                    txtJudge.setTextColor(getResources().getColor(R.color.tabsScrollColor));
                    txtType.setTextColor(getResources().getColor(R.color.colorText));
                    txtCaseTitle.setTextColor(getResources().getColor(R.color.colorText));
                }
            }
        });
        txtType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    txtType.setTextColor(getResources().getColor(R.color.tabsScrollColor,null));
                    txtJudge.setTextColor(getResources().getColor(R.color.colorText,null));
                    txtCaseTitle.setTextColor(getResources().getColor(R.color.colorText,null));
                }
                else
                {
                    txtType.setTextColor(getResources().getColor(R.color.tabsScrollColor));
                    txtJudge.setTextColor(getResources().getColor(R.color.colorText));
                    txtCaseTitle.setTextColor(getResources().getColor(R.color.colorText));
                }
            }
        });

    }
}
