package com.eweblog.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.eweblog.R;
import com.eweblog.CorporateUserMainActivity;
import com.eweblog.common.Prefshelper;
import com.eweblog.model.CaseListModel;

import java.util.List;

public class WeeklyCaseListFragment extends Fragment {

    List<CaseListModel> caseList;
    ListView listView;
    FloatingActionButton fab;
    Prefshelper prefshelper;
    View rootview;


    public WeeklyCaseListFragment() {
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

            rootview = inflater.inflate(R.layout.fragment_weekly_list, container, false);
            listView = (ListView) rootview.findViewById(R.id.listView);
          /*  caseList= (List<CaseListModel>) getArguments().getSerializable("list");

            Log.e("listtttttte", caseList+"");
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
            });*/





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

                        Intent intent=new Intent(getActivity(), CorporateUserMainActivity.class);
                        startActivity(intent);

                    }

                    return true;

                }

                return false;
            }
        });
    }

}
