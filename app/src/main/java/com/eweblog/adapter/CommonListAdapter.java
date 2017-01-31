package com.eweblog.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eweblog.R;
import com.eweblog.model.CommonList;

import java.util.List;

/**
 * Created by erginus on 1/30/2017.
 */

public class CommonListAdapter extends ArrayAdapter<CommonList> {
    LayoutInflater flater;
    List<CommonList> list;

    public CommonListAdapter(Activity context, int resouceId, List<CommonList> list){

        super(context,resouceId, list);
        flater = context.getLayoutInflater();
        this.list=list;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        CommonList list = getItem(position);
        View rowview = convertView;
        if (rowview==null) {

            rowview = flater.inflate(R.layout.layout_spinner_dropdown, null, true);

            TextView txtTitle = (TextView) rowview.findViewById(R.id.textView);
            txtTitle.setText(list.getName());
            if (position == 0) {
                txtTitle.setTextColor(Color.GRAY);
            } else {
                txtTitle.setTextColor(Color.BLACK);
            }

        }

        return rowview;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
