package com.eweblog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eweblog.common.Prefshelper;
import com.eweblog.model.SearchCaseList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erginus on 2/8/2017.
 */

public class SearchCaseListAdapter extends RecyclerView.Adapter<SearchCaseListAdapter.SearchCaseListViewHolder> {
    List<SearchCaseList> searchCaseList=new ArrayList<>();
    Context context;

    public SearchCaseListAdapter(Context context,List<SearchCaseList> searchCaseList)
    {
        this.context=context;
        this.searchCaseList=searchCaseList;
    }

    @Override
    public SearchCaseListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.case_list_item, parent, false);

        return new SearchCaseListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchCaseListViewHolder holder, int position) {
        SearchCaseList searchCaseListObject=searchCaseList.get(position);
        holder.txtCase.setText(searchCaseListObject.getCase_title());
        holder.txtNumber.setText(searchCaseListObject.getCase_date());
        int id=searchCaseListObject.getCase_id();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return searchCaseList.size();
    }

    public class SearchCaseListViewHolder extends RecyclerView.ViewHolder {
        TextView txtCase, txtNumber;

        public SearchCaseListViewHolder(View itemView) {
            super(itemView);
            txtCase=(TextView) itemView.findViewById(R.id.textView_title);
            txtNumber=(TextView) itemView.findViewById(R.id.textView_nmber);
        }
    }
}
