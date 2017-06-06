package com.eweblog.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eweblog.CaseDetailActivity;
import com.eweblog.R;
import com.eweblog.model.CaseList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erginus on 2/8/2017.
 */

/**
 * Adapter to show case list on search,advance search and view cases.
 * It is used to show case title and date
 */

public class CaseListAdapter extends RecyclerView.Adapter<CaseListAdapter.SearchCaseListViewHolder> {
    List<CaseList> caseList=new ArrayList<>();
    Context context;

    public CaseListAdapter(Context context, List<CaseList> caseList)
    {
        this.context=context;
        this.caseList=caseList;
    }

    @Override
    public SearchCaseListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.case_list_item, parent, false);

        return new SearchCaseListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchCaseListViewHolder holder, int position) {
        CaseList caseListObject =caseList.get(position);
        holder.txtTitle.setText(caseListObject.getCase_title());
        holder.txtDate.setText(caseListObject.getCase_date());
        final int id= caseListObject.getCase_id();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, CaseDetailActivity.class);
                intent.putExtra("id",String.valueOf(id));
                context.startActivity(intent);
            }
        });
    }
    public void addArrayList(List<CaseList> caseLists)
    {
        caseList.clear();
        caseList.addAll(caseLists);
    }
    @Override
    public int getItemCount() {
        return caseList.size();
    }

    public class SearchCaseListViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDate;

        public SearchCaseListViewHolder(View itemView) {
            super(itemView);
            txtTitle=(TextView) itemView.findViewById(R.id.textView_title);
            txtDate=(TextView) itemView.findViewById(R.id.textView_date);
        }
    }
}
