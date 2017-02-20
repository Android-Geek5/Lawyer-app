package com.eweblog;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eweblog.R;
import com.eweblog.fragment.SelectDateFragment;

public class ViewCasesActivity extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout llBack;
    LinearLayout imageBack;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cases);
        toolbar= (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        llBack=(LinearLayout) toolbar.findViewById(R.id.ll_navi);
        imageBack=(LinearLayout) toolbar.findViewById(R.id.imageView_back);
        imageBack.setVisibility(View.VISIBLE);
        title=(TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText("");
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new SelectDateFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ViewCasesActivity.this.finish();
    }
}
