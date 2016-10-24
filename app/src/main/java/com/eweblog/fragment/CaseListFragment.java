package com.eweblog.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.eweblog.AddCaseActivity;
import com.eweblog.R;
import com.eweblog.SelectDateActivity;
import com.eweblog.adapter.CaseListAdapter;
import com.eweblog.common.Prefshelper;
import com.eweblog.model.CaseListModel;

import java.util.List;

public class CaseListFragment extends Fragment {

    List<CaseListModel> caseList, caseListArray;
    ListView listView;
    FloatingActionButton fab;
    Prefshelper prefshelper;
    View rootview;


    public CaseListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        prefshelper=new Prefshelper(getActivity());
        if(prefshelper.getMesage().equalsIgnoreCase("No Cases available"))
        {
           rootview= inflater.inflate(R.layout.activity_no_case, container, false);
            fab = (FloatingActionButton)rootview.findViewById(R.id.fab);

            fab.setBackgroundTintList(ColorStateList.valueOf(Color
                    .parseColor("#00bcd5")));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getActivity(), AddCaseActivity.class);
                    startActivity(intent);

                }
            });
        }
        else
        {
            rootview = inflater.inflate(R.layout.fragment_case_list, container, false);
            listView = (ListView) rootview.findViewById(R.id.listView);
            caseList= (List<CaseListModel>) getArguments().getSerializable("list");

            listView.setAdapter(new CaseListAdapter(getActivity(), caseList));


            fab = (FloatingActionButton)rootview.findViewById(R.id.fab);

            fab.setBackgroundTintList(ColorStateList.valueOf(Color
                    .parseColor("#00bcd5")));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getActivity(), AddCaseActivity.class);
                    startActivity(intent);

                }
            });
        }

        SelectDateActivity.txtTitle.setText("Case List");


        return rootview;
    }

    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    if(getFragmentManager().getBackStackEntryCount() > 0) {

                        getFragmentManager().popBackStack();
                        SelectDateActivity.txtTitle.setText("Home");

                    }

                    return true;

                }

                return false;
            }
        });
    }

}
