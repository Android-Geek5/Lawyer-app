package com.eweblog.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.eweblog.AddCaseActivity;
import com.eweblog.R;
import com.eweblog.ViewUsersActivity;
import com.eweblog.ViewCasesActivity;


/*
*Main screen for Corporate user
 */

public class CorporateUserMainFragment extends Fragment {
   LinearLayout viewUserLayout,viewCaseLayout,addCaseLayout;

    public CorporateUserMainFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_corporate_user_main, container, false);
        viewCaseLayout=(LinearLayout) rootView.findViewById(R.id.view_cases_layout);
        viewUserLayout=(LinearLayout) rootView.findViewById(R.id.view_user_layout);
        addCaseLayout=(LinearLayout) rootView.findViewById(R.id.add_case_layout);
        viewUserLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), ViewUsersActivity.class);
                startActivity(intent);
            }
        });
        viewCaseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), ViewCasesActivity.class);
                startActivity(intent);
            }
        });
        addCaseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), AddCaseActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

}