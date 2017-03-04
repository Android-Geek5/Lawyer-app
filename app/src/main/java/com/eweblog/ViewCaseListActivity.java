package com.eweblog;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eweblog.adapter.CaseListAdapter;
import com.eweblog.common.UserInfo;
import com.eweblog.model.CaseList;

import java.util.ArrayList;
import java.util.List;

public class ViewCaseListActivity extends AppCompatActivity {
    LinearLayout llBack;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    CaseListAdapter searchCaseListAdapter;
    List<CaseList> caseList =new ArrayList<>();
    TextView title;
    DividerItemDecoration mDividerItemDecoration;
    LinearLayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_case_list);
        inflateToolbar();
        inflateLayout();
        getIntentInformation();
    }

    public void getIntentInformation() {
        // Get parceable list of cases for particular date
        caseList =getIntent().getParcelableArrayListExtra(UserInfo.SEARCHED_CASE_LIST);
        searchCaseListAdapter.addArrayList(caseList);
        searchCaseListAdapter.notifyDataSetChanged();
    }

    public void inflateToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        title=(TextView)toolbar.findViewById(R.id.toolbar_title);

        llBack=(LinearLayout)toolbar.findViewById(R.id.imageView_back);
        llBack.setVisibility(View.VISIBLE);
        title.setText("Case List");
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });

    }
     public void inflateLayout()
     {
         recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
         searchCaseListAdapter=new CaseListAdapter(ViewCaseListActivity.this, caseList);
         mLayoutManager=new LinearLayoutManager(getApplicationContext());
         recyclerView.setLayoutManager(mLayoutManager);
         mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                 mLayoutManager.getOrientation());
         recyclerView.addItemDecoration(mDividerItemDecoration);
         recyclerView.setAdapter(searchCaseListAdapter);
         fab = (FloatingActionButton)findViewById(R.id.fab);
         /** Show add case floating button only if It is not a child user **/
         if(Utils.getUserPreferencesBoolean(ViewCaseListActivity.this, UserInfo.CHILD_USER_OR_NOT))
         {
             fab.setVisibility(View.GONE);
         }
         else {
             fab.setBackgroundTintList(ColorStateList.valueOf(Color
                     .parseColor("#00bcd5")));
             fab.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {

                     Intent intent = new Intent(ViewCaseListActivity.this, AddCaseActivity.class);
                     startActivity(intent);

                 }
             });
         }
     }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ViewCaseListActivity.this.finish();
        Intent intent = new Intent(ViewCaseListActivity.this, MainAcitivity.class);
        startActivity(intent);
    }
}
