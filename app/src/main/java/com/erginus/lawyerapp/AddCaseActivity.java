package com.erginus.lawyerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class AddCaseActivity extends AppCompatActivity {
LinearLayout linearLayout;
    Spinner sprDay, sprMonth, sprYear, sprNDay, sprNMonth, sprNYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_case);
        sprDay=(Spinner)findViewById(R.id.spinner_day);
        sprMonth=(Spinner)findViewById(R.id.spinner_month);
        sprYear=(Spinner)findViewById(R.id.spinner_year);
        sprNDay=(Spinner)findViewById(R.id.spinner_nday);
        sprNMonth=(Spinner)findViewById(R.id.spinner_nmonth);
        sprNYear=(Spinner)findViewById(R.id.spinner_nyear);
        linearLayout=(LinearLayout)findViewById(R.id.ll_navi);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ArrayAdapter<String> spinnerArrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.day)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sprDay.setAdapter(spinnerArrayAdapter);
        sprNDay.setAdapter(spinnerArrayAdapter);
        ArrayAdapter<String> spinnerArrayAdapter1 =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.month)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sprMonth.setAdapter(spinnerArrayAdapter1);
        sprNMonth.setAdapter(spinnerArrayAdapter1);
        ArrayAdapter<String> spinnerArrayAdapter2 =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.year)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sprYear.setAdapter(spinnerArrayAdapter2);
        sprNYear.setAdapter(spinnerArrayAdapter2);
    }
    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();

    }
}
