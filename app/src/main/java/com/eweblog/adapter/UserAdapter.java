package com.eweblog.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eweblog.CaseDetailActivity;
import com.eweblog.R;
import com.eweblog.common.ConnectionDetector;
import com.eweblog.common.Prefshelper;
import com.eweblog.model.CaseListModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class UserAdapter extends BaseAdapter {
    List<CaseListModel> newList;
    ConnectionDetector cd;
    private static LayoutInflater inflater=null;
    Context context;
    Prefshelper prefshelper;

    public UserAdapter(Context contxt, List<CaseListModel> list) {
        // TODO Auto-generated constructor stub

        context=contxt;
        newList=list;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return newList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView txtCase, txtNumber;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.user_list_item, null);

        return rowView;
    }

}