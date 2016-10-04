package com.erginus.lawyerapp.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;

import com.erginus.lawyerapp.AddCaseActivity;
import com.erginus.lawyerapp.CaseListModel;
import com.erginus.lawyerapp.R;
import com.erginus.lawyerapp.SelectDateActivity;
import com.erginus.lawyerapp.adapter.CaseListAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    List<CaseListModel> caseList;
    ListView listView;
    FloatingActionButton fab;

    public HomeFragment() {
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
        View rootview = inflater.inflate(R.layout.activity_home, container, false);

        listView = (ListView) rootview.findViewById(R.id.listView);

        CaseListModel caseListModel = new CaseListModel();
        caseList = new ArrayList<>();
        caseList.add(caseListModel);
        caseList.add(caseListModel);
        caseList.add(caseListModel);
        caseList.add(caseListModel);
        caseList.add(caseListModel);
        caseList.add(caseListModel);
        caseList.add(caseListModel);
        caseList.add(caseListModel);

        listView.setAdapter(new CaseListAdapter(getActivity(), caseList));
        fab = (FloatingActionButton)rootview.findViewById(R.id.fab);

        fab.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor("#00bcd5")));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), AddCaseActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
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

                        SelectDateActivity.txtTitle.setText("Add Date");
                        getFragmentManager().popBackStack();

                    }

                    return true;

                }

                return false;
            }
        });
    }

}
