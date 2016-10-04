package com.erginus.lawyerapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.erginus.lawyerapp.CaseDetailActivity;
import com.erginus.lawyerapp.CaseListModel;
import com.erginus.lawyerapp.R;

import java.util.List;


public class CaseListAdapter  extends BaseAdapter {
    List<CaseListModel> caseList;

    private static LayoutInflater inflater=null;
    Context context;

    public CaseListAdapter(Context contxt, List<CaseListModel> list) {
        // TODO Auto-generated constructor stub

        context=contxt;
        caseList=list;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return caseList.size();
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
        TextView txtCase, txtNumber, txtCourt, txtCaseType, txtLearn;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.case_list_item, null);
        holder.txtCase=(TextView) rowView.findViewById(R.id.textView_title);
        holder.txtNumber=(TextView) rowView.findViewById(R.id.textView_nmber);
        holder.txtCourt=(TextView) rowView.findViewById(R.id.textView_name);
        holder.txtCaseType=(TextView) rowView.findViewById(R.id.textView_type);
        holder.txtLearn=(TextView) rowView.findViewById(R.id.textView_learnmore);
      /*  holder.txtUniversity.setText(universities[position]);
        holder.txtCity.setText(cities[position]);
        holder.txtDueDate.setText(duedates[position]);

       */

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent=new Intent(context, CaseDetailActivity.class);

                context.startActivity(intent);
            }
        });
        return rowView;
    }

}