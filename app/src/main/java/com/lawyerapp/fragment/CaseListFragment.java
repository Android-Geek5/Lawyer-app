package com.lawyerapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.lawyerapp.AddCaseActivity;
import com.lawyerapp.R;
import com.lawyerapp.SelectDateActivity;
import com.lawyerapp.adapter.CaseListAdapter;
import com.lawyerapp.common.MapAppConstant;
import com.lawyerapp.common.Prefshelper;
import com.lawyerapp.common.VolleySingleton;
import com.lawyerapp.model.CaseListModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaseListFragment extends Fragment {

    List<CaseListModel> caseList;
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
