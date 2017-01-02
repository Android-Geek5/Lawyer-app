package com.eweblog.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.eweblog.R;
import com.eweblog.SelectDateActivity;
import com.eweblog.adapter.UserAdapter;
import com.eweblog.model.CaseListModel;

import java.util.ArrayList;
import java.util.List;


public class ActiveUsersFragement extends Fragment {
    ListView listView;
    FloatingActionButton fab;
    List<CaseListModel> list;
    public ActiveUsersFragement() {
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
       View rootview= inflater.inflate(R.layout.fragment_case_list, container, false);

        listView = (ListView) rootview.findViewById(R.id.listView);
        fab = (FloatingActionButton)rootview.findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        CaseListModel model=new CaseListModel();
        list=new ArrayList<>();
        list.add(model);
        UserAdapter userAdapter=new UserAdapter(getActivity(), list);
        listView.setAdapter(userAdapter);

        return  rootview;
    }

    @TargetApi(Build.VERSION_CODES.M)

    @Override
    public void onResume() {

        super.onResume();
       // getView().setFocusableInTouchMode(true);
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
