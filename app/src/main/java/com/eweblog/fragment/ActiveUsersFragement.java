package com.eweblog.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eweblog.MainAcitivity;
import com.eweblog.R;
import com.eweblog.adapter.UserListAdapter;
import com.eweblog.model.ChildUsersList;

import java.util.ArrayList;
import java.util.List;



public class ActiveUsersFragement extends Fragment {
    List<ChildUsersList> childUsersLists=new ArrayList<>();
    UserListAdapter userListAdapter;
    RecyclerView recyclerView;
    DividerItemDecoration mDividerItemDecoration;
    LinearLayoutManager mLayoutManager;
    TextView noUser;

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

        recyclerView=(RecyclerView)rootview.findViewById(R.id.recyclerView);
        noUser=(TextView) rootview.findViewById(R.id.no_user);
        mLayoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        userListAdapter =new UserListAdapter(getActivity(),childUsersLists);
        mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
        recyclerView.setAdapter(userListAdapter);
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
                        //CorporateUserMainActivity.txtTitle.setText("Home");
                        MainAcitivity.txtTitle.setText("Home");
                    }

                    return true;

                }

                return false;
            }
        });

    }

    // This method is being called from main activity and then list is shown
public void inflateList(List<ChildUsersList> childUsers)
{
    noUser.setVisibility(View.GONE);
    recyclerView.setVisibility(View.VISIBLE);
    childUsersLists.clear();
    childUsersLists.addAll(childUsers);
    userListAdapter.notifyDataSetChanged();

}
    // This method is being called from main activity and there are no users
    public void noUsers()
    {
        recyclerView.setVisibility(View.GONE);
        noUser.setVisibility(View.VISIBLE);
        noUser.setText("No active users");
    }
}
