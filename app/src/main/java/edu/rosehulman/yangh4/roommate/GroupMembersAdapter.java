package edu.rosehulman.yangh4.roommate;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Hao Yang on 7/21/2017.
 */

public class GroupMembersAdapter extends RecyclerView.Adapter<GroupMembersAdapter.ViewHolder> implements Parcelable {
    protected GroupMembersAdapter(Parcel in) {
        mGroup = in.readParcelable(Group.class.getClassLoader());
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

    public Group getmGroup() {
        return mGroup;
    }

    private Group mGroup;
    private GroupMemberAdapterFragment.Callback mCallback;

    public GroupMembersAdapter(Group group, GroupMemberAdapterFragment.Callback mCallback) {
        this.mGroup = group;
        this.mCallback = mCallback;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.memberinfo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final People person = mGroup.getGroupmembers().get(position);
        holder.mNameView.setText(person.getName());
        holder.mImageView.setImageBitmap(person.getPhoto());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.showUserInfo(person);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mGroup.getGroupmembers().size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mGroup, flags);
    }

    public void setCallBack(GroupMemberAdapterFragment.Callback callBack) {
        this.mCallback = callBack;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mNameView;
        ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.member_photo);
            mNameView = (TextView) itemView.findViewById(R.id.member_name);
        }
    }
}
