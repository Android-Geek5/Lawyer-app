package com.lawyerapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lawyerapp.CaseDetailActivity;
import com.lawyerapp.R;
import com.lawyerapp.model.CaseListModel;

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
        holder.txtCase.setText("Case Title: "+caseList.get(position).getCaseTitle());
        holder.txtNumber.setText("Case Number: "+caseList.get(position).getCaseNumber());
        holder.txtCourt.setText("Court Name: "+caseList.get(position).getCourtName());
        holder.txtCaseType.setText("Case Type: "+caseList.get(position).getCaseType());


        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent=new Intent(context, CaseDetailActivity.class);
                intent.putExtra("cnumber",caseList.get(position).getCaseNumber());
                intent.putExtra("id", caseList.get(position).getCaseId());
                intent.putExtra("ctype",caseList.get(position).getCaseType());
                intent.putExtra("ctitle", caseList.get(position).getCaseTitle());
                intent.putExtra("court",caseList.get(position).getCourtName());
                intent.putExtra("status", caseList.get(position).getCaseStatus());
                intent.putExtra("pdate",caseList.get(position).getCasePrevDate());
                intent.putExtra("ndate", caseList.get(position).getNextDate());
                intent.putExtra("sdate",caseList.get(position).getCaseStartDate());
                intent.putExtra("oname", caseList.get(position).getCounsellorName());
                intent.putExtra("ocontact",caseList.get(position).getCounsellorContact());
                intent.putExtra("rname", caseList.get(position).getRetainName());
                intent.putExtra("rcontact",caseList.get(position).getRetainContact());
                intent.putExtra("comment", caseList.get(position).getComment());
                context.startActivity(intent);
            }
        });
        return rowView;
    }

}