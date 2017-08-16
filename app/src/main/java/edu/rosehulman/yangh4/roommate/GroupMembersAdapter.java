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
 * Created by Hao Yang on 7/21/2017.
 */

public class GroupMembersAdapter extends RecyclerView.Adapter<GroupMembersAdapter.ViewHolder> implements Parcelable {

    private GroupMemberAdapterFragment.Callback mCallback;
    private String mGroupKey;
    private DatabaseReference mPeopleRef;
    private ArrayList<People> mMembers = new ArrayList<>();


    public GroupMembersAdapter(Group group, GroupMemberAdapterFragment.Callback mCallback) {
        this.mCallback = mCallback;
        mGroupKey = group.getId();
        mPeopleRef = FirebaseDatabase.getInstance().getReference().child("members");
        Query query = mPeopleRef.orderByChild("groupkeys/" + mGroupKey).equalTo(true);
        query.addChildEventListener(new MembersChildEventListener());
    }


    protected GroupMembersAdapter(Parcel in) {
        mGroupKey = in.readString();
        mMembers = in.createTypedArrayList(People.CREATOR);
    }

    public static final Creator<GroupMembersAdapter> CREATOR = new Creator<GroupMembersAdapter>() {
        @Override
        public GroupMembersAdapter createFromParcel(Parcel in) {
            return new GroupMembersAdapter(in);
        }

        @Override
        public GroupMembersAdapter[] newArray(int size) {
            return new GroupMembersAdapter[size];
        }
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.memberinfo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final People person = mMembers.get(position);
        holder.mNameView.setText(person.getName());
        holder.mPhoneView.setText(person.getPhone());
//        holder.mImageView.setImageBitmap(person.getPhoto());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.showUserInfo(person);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMembers.size();
    }

    public void setCallBack(GroupMemberAdapterFragment.Callback callBack) {
        this.mCallback = callBack;
    }


    public void add(People member) {
        mPeopleRef.child(member.getKey()).setValue(member);
    }


    public String getId() {
        return mGroupKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mGroupKey);
        dest.writeTypedList(mMembers);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mNameView;
        TextView mPhoneView;

        public ViewHolder(View itemView) {
            super(itemView);
            mPhoneView = (TextView) itemView.findViewById(R.id.member_phone);
            mNameView = (TextView) itemView.findViewById(R.id.member_name);
        }
    }

    private class MembersChildEventListener implements ChildEventListener {
        private void add(DataSnapshot dataSnapshot) {
            People member = dataSnapshot.getValue(People.class);
            mMembers.add(member);
        }

        private int remove(String key) {
            for (People member : mMembers) {
                if (member.getKey().equals(key)) {
                    int pos = mMembers.indexOf(member);
                    mMembers.remove(member);
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
}
