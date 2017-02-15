package com.eweblog.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.eweblog.MainAcitivity;
import com.eweblog.R;
import com.eweblog.CorporateUserMainActivity;
import com.eweblog.adapter.UserAdapter;
import com.eweblog.model.CaseListModel;
import com.eweblog.model.ChildUsersList;

import java.util.ArrayList;
import java.util.List;


public class InactiveUsersFragement extends Fragment {
    List<ChildUsersList> childUsersLists=new ArrayList<>();
    UserAdapter userAdapter;
    RecyclerView recyclerView;

    public InactiveUsersFragement() {
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
        recyclerView=(RecyclerView)rootview.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        userAdapter=new UserAdapter(getActivity(),childUsersLists);
        recyclerView.setAdapter(userAdapter);

        return  rootview;
    }

    @TargetApi(Build.VERSION_CODES.M)

    @Override
    public void onResume() {

        super.onResume();
   //     getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    if(getFragmentManager().getBackStackEntryCount() > 0) {

                        getFragmentManager().popBackStack();
                        //CorporateUserMainActivity.txtTitle.setText("Home");
                        MainAcitivity.txtTitle.setText("Home");
                    }

                    return true;

                }

                return false;
            }
        });
    }
    public void inflateList(List<ChildUsersList> childUsers)
    {
        childUsersLists.clear();
        childUsersLists.addAll(childUsers);
        userAdapter.notifyDataSetChanged();

    }
}
