package edu.rosehulman.yangh4.roommate;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hao Yang on 7/22/2017.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> implements Parcelable {
    private ArrayList<Group> mGroupList;
    private GroupListFragment.Callback mCallback;

    protected GroupAdapter(Parcel in) {
        mGroupList = in.createTypedArrayList(Group.CREATOR);
    }

    public static final Creator<GroupAdapter> CREATOR = new Creator<GroupAdapter>() {
        @Override
        public GroupAdapter createFromParcel(Parcel in) {
            return new GroupAdapter(in);
        }

        @Override
        public GroupAdapter[] newArray(int size) {
            return new GroupAdapter[size];
        }
    };

    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.groupinfo, parent, false);
        return new GroupAdapter.ViewHolder(view);
    }

    public void setmCallback(GroupListFragment.Callback mCallback) {
        this.mCallback = mCallback;
    }

    public GroupAdapter(ArrayList<Group> mGroupList, GroupListFragment.Callback mCallback) {
        this.mGroupList = mGroupList;
        this.mCallback = mCallback;
        Log.d("GROUPADAPTER", (mCallback == null) + "");
    }

    public GroupAdapter() {
    }

    @Override
    public void onBindViewHolder(GroupAdapter.ViewHolder holder, int position) {
        final Group group = mGroupList.get(position);
        holder.mgroupname.setText(group.getGroupname());
        holder.mDescription.setText(group.getDescription());
        holder.mBalance.setText("Balance: " + group.getBalance());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.showgroupmember(group);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGroupList.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public ArrayList<Group> getmGroupList() {
        return mGroupList;
    }

    @Override

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mGroupList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mgroupname;
        private TextView mDescription;
        private TextView mBalance;


        public ViewHolder(View itemView) {
            super(itemView);
            mgroupname = (TextView) itemView.findViewById(R.id.item_name);
            mDescription = (TextView) itemView.findViewById(R.id.description_view);
            mBalance = (TextView) itemView.findViewById(R.id.balance_text);
        }
    }
}
