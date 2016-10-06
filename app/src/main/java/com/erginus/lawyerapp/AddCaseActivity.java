package com.erginus.lawyerapp;

import android.content.Context;
import android.graphics.Color;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;



public class AddCaseActivity extends AppCompatActivity implements SpellCheckerSession.SpellCheckerSessionListener {
    LinearLayout linearLayout;
    Spinner sprDay, sprMonth, sprYear, sprNDay, sprNMonth, sprNYear, sprCaseType;
    private SpellCheckerSession mScs;
    ArrayAdapter<String> stringArrayAdapter;
    Button btnAdd;
    AutoCompleteTextView txtCourtName, txtCaseNumber, txtCaseTitle, txtCaseType, txtStatus, txtRetainName, txtRetainMobile,
            txtOppositeName,txtOppositeNumber, txtComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_case);
        sprDay = (Spinner) findViewById(R.id.spinner_day);
        sprMonth = (Spinner) findViewById(R.id.spinner_month);
        sprYear = (Spinner) findViewById(R.id.spinner_year);
        sprNDay = (Spinner) findViewById(R.id.spinner_nday);
        sprNMonth = (Spinner) findViewById(R.id.spinner_nmonth);
        sprNYear = (Spinner) findViewById(R.id.spinner_nyear);
        sprCaseType = (Spinner) findViewById(R.id.spinner_type);
        btnAdd=(Button)findViewById(R.id.add);
        linearLayout = (LinearLayout) findViewById(R.id.ll_navi);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        txtCourtName=(AutoCompleteTextView) findViewById(R.id.textView_name);
        txtCaseNumber=(AutoCompleteTextView) findViewById(R.id.textView_number);
        txtCaseTitle=(AutoCompleteTextView) findViewById(R.id.textView_title);
        txtCaseType=(AutoCompleteTextView) findViewById(R.id.textView_type);
        txtStatus=(AutoCompleteTextView) findViewById(R.id.textView_position);
        txtRetainName=(AutoCompleteTextView) findViewById(R.id.textView_nm);
        txtRetainMobile=(AutoCompleteTextView) findViewById(R.id.textView_mobile);
        txtOppositeName=(AutoCompleteTextView) findViewById(R.id.textView_cname);
        txtOppositeNumber=(AutoCompleteTextView) findViewById(R.id.textView_cmobile);
        txtComments=(AutoCompleteTextView) findViewById(R.id.textView_comments);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mScs.getSuggestions(new TextInfo(txtCourtName.getText().toString()), 3 );
            }
        });
        if (sprCaseType.getAdapter() == null) {
            stringArrayAdapter = new ArrayAdapter<String>(AddCaseActivity.this, R.layout.layout_spinner, getResources().getStringArray(R.array.case_type)) {

                @Override
                public boolean isEnabled(int position) {

                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }

                    return view;
                }
            };
            stringArrayAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
            sprCaseType.setAdapter(stringArrayAdapter);
        }
        if (sprDay.getAdapter() == null) {
           stringArrayAdapter = new ArrayAdapter<String>(AddCaseActivity.this, R.layout.layout_spinner, getResources().getStringArray(R.array.day)) {

                @Override
                public boolean isEnabled(int position) {

                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            stringArrayAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
            sprDay.setAdapter(stringArrayAdapter);
        }
        if (sprMonth.getAdapter() == null) {
           stringArrayAdapter = new ArrayAdapter<String>(AddCaseActivity.this, R.layout.layout_spinner, getResources().getStringArray(R.array.month)) {

                @Override
                public boolean isEnabled(int position) {

                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            stringArrayAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
            sprMonth.setAdapter(stringArrayAdapter);
        }
        if (sprYear.getAdapter() == null) {
           stringArrayAdapter = new ArrayAdapter<String>(AddCaseActivity.this, R.layout.layout_spinner, getResources().getStringArray(R.array.year)) {

                @Override
                public boolean isEnabled(int position) {

                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            stringArrayAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
            sprYear.setAdapter(stringArrayAdapter);
        }
        if (sprNDay.getAdapter() == null) {
           stringArrayAdapter = new ArrayAdapter<String>(AddCaseActivity.this, R.layout.layout_spinner, getResources().getStringArray(R.array.day)) {

                @Override
                public boolean isEnabled(int position) {

                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            stringArrayAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
            sprNDay.setAdapter(stringArrayAdapter);
        }
        if (sprNMonth.getAdapter() == null) {
           stringArrayAdapter = new ArrayAdapter<String>(AddCaseActivity.this, R.layout.layout_spinner, getResources().getStringArray(R.array.month)) {

                @Override
                public boolean isEnabled(int position) {

                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            stringArrayAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
            sprNMonth.setAdapter(stringArrayAdapter);
        }
        if (sprNYear.getAdapter() == null) {
            stringArrayAdapter = new ArrayAdapter<String>(AddCaseActivity.this, R.layout.layout_spinner, getResources().getStringArray(R.array.year)) {

                @Override
                public boolean isEnabled(int position) {

                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            stringArrayAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
            sprNYear.setAdapter(stringArrayAdapter);
        }
        sprCaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==3)
                {
                    txtCaseType.setVisibility(View.VISIBLE);
                    txtCaseType.requestFocus();
                }
                else
                {
                    txtCaseType.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sprYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0)
                {

                }
                else {
                    txtCourtName.requestFocus();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sprNYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0)
                {

                }
                else {
                    txtRetainName.requestFocus();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    public void onResume() {
        super.onResume();
        final TextServicesManager tsm = (TextServicesManager) getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE);
        mScs = tsm.newSpellCheckerSession(null, null, this, true);
    }

    public void onPause() {
        super.onPause();
        if (mScs != null) {
            mScs.close();
        }
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }


    @Override
    public void onGetSuggestions(SuggestionsInfo[] arg0) {

        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < arg0.length; ++i) {
            // Returned suggestions are contained in SuggestionsInfo
            final int len = arg0[i].getSuggestionsCount();
            sb.append('\n');

            for (int j = 0; j < len; ++j) {
                sb.append("," + arg0[i].getSuggestionAt(j));
            }

            sb.append(" (" + len + ")");
        }

    }

    @Override
    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] sentenceSuggestionsInfos) {

    }
}
