package com.eweblog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eweblog.R;
import com.eweblog.model.ChildUsersList;

import java.util.ArrayList;
import java.util.List;


/**
 * Adapter to show users list for business user
 * It is used to show child name and number of cases assigned.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {
    List<ChildUsersList> childUsersLists=new ArrayList<>();
    Context context;

    public UserListAdapter(Context context, List<ChildUsersList> childUsersLists)
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
