package com.eweblog.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
import com.eweblog.model.ChildUsersList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    List<ChildUsersList> childUsersLists=new ArrayList<>();
    Context context;

    public UserAdapter(Context context,List<ChildUsersList> childUsersLists)
    {
        this.context=context;
        this.childUsersLists=childUsersLists;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        ChildUsersList childUserObject=childUsersLists.get(position);
        holder.txtName.setText(childUserObject.getUserDetail());
        holder.txtNumber.setText(String.valueOf(childUserObject.getCasesCount()));
       // int id=childUserObject.getId();
    }

    @Override
    public int getItemCount() {
        return childUsersLists.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtNumber;

        public UserViewHolder(View itemView) {
            super(itemView);
            txtName=(TextView) itemView.findViewById(R.id.childName);
            txtNumber=(TextView) itemView.findViewById(R.id.childCaseCount);
        }
    }
}
