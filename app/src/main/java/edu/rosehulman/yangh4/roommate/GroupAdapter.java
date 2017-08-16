package edu.rosehulman.yangh4.roommate;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

/**
 * Created by Hao Yang on 7/22/2017.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> implements Parcelable {
    private ArrayList<Group> mGroupList;
    private GroupListFragment.Callback mCallback;
    private String mUid;
    private DatabaseReference mGroupRef;
//    private DatabaseReference mMemberRef;

    protected GroupAdapter(Parcel in) {
        mGroupList = in.createTypedArrayList(Group.CREATOR);
        mUid = in.readString();
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


    public GroupAdapter(GroupListFragment.Callback mCallback, String mUid) {
        this.mCallback = mCallback;
        this.mUid = mUid;
        mGroupList = new ArrayList<>();
        mGroupRef = FirebaseDatabase.getInstance().getReference().child("groups");
        Query query = mGroupRef.orderByChild("memberkeys/" + mUid).equalTo(true);
        query.addChildEventListener(new GroupsChildEventListener());
//        mMemberRef = FirebaseDatabase.getInstance().getReference().child("members").child(mUid);
    }

    @Override
    public void onBindViewHolder(GroupAdapter.ViewHolder holder, int position) {
        final Group group = mGroupList.get(position);
        holder.mgroupname.setText(group.getGroupname());
        holder.mDescription.setText(group.getDescription());
        Double balance = group.getBalanceMap().get(MainActivity.user.getKey()) == null ? 0 : group.getBalanceMap().get(MainActivity.user.getKey());
        holder.mBalance.setText("Balance: " + balance);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mCallback.showEditGroupDialog(group);
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                mCallback.showgroupmember(group);
            }
        });
    }

    public void add(Group group) {
        mGroupRef.child(group.getId()).setValue(group);
        mGroupRef.child(group.getId() + "/memberkeys/" + MainActivity.user.getKey()).setValue(true);
        mGroupRef.child(group.getId() + "/balanceMap/" + MainActivity.user.getKey()).setValue(0);
//        DatabaseReference temp = mGroupRef.push();
//        temp.setValue(group);
//        Map<String, Object> map = new HashMap<>();
//        map.put(temp.getKey(), true);
//        mMemberRef.child("Groups").updateChildren(map);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mGroupList);
        dest.writeString(mUid);
    }

    class GroupsChildEventListener implements ChildEventListener {

        private void add(DataSnapshot dataSnapshot) {
            Group group = dataSnapshot.getValue(Group.class);
            mGroupList.add(group);
        }

        private int remove(String key) {
            for (Group groups : mGroupList) {
                if (groups.getId().equals(key)) {
                    int pos = mGroupList.indexOf(groups);
                    mGroupList.remove(groups);
                    return pos;
                }
            }
            return -1;
        }


        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            remove(dataSnapshot.getKey());
            add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            int position = remove(dataSnapshot.getKey());
            if (position >= 0) {
                notifyItemRemoved(position);
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            // empty
        }

        @Override
        public void onCancelled(DatabaseError firebaseError) {
            Log.e("TAG", "onCancelled. Error: " + firebaseError.getMessage());

        }
    }


    @Override
    public int getItemCount() {
        return mGroupList.size();
    }

    public ArrayList<Group> getmGroupList() {
        return mGroupList;
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
