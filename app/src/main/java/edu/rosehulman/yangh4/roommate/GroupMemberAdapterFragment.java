package edu.rosehulman.yangh4.roommate;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class GroupMemberAdapterFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_Adapter = "Group_Member_Adapter";

    private static Callback mCallback;
    private GroupMembersAdapter mGroupAdapter;


    public GroupMemberAdapterFragment() {
        // Required empty public constructor
    }

    public static GroupMemberAdapterFragment newInstance(Group group) {
        GroupMemberAdapterFragment fragment = new GroupMemberAdapterFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_Adapter, new GroupMembersAdapter(group, mCallback));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mGroupAdapter = getArguments().getParcelable(ARG_Adapter);
            mGroupAdapter.setCallBack(mCallback);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.group_member_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_show_item:
                mCallback.show_item(mGroupAdapter.getmGroup());
                break;
            case R.id.action_leave_group:
                mCallback.show_leave_group_dialog(mGroupAdapter);
                break;
//            case R.id.action_feedback:
//                break;
//            case R.id.action_settings:
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout relativeview = (RelativeLayout) inflater.inflate(R.layout.groupmember_view, container, false);
        RecyclerView view = (RecyclerView) relativeview.findViewById(R.id.member_list_view);
        view.setLayoutManager(new LinearLayoutManager(getContext()));
        if (mGroupAdapter == null) {
            mGroupAdapter = new GroupMembersAdapter(new Group(), mCallback);
        }
        view.setAdapter(mGroupAdapter);
        return relativeview;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            mCallback = (Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public interface Callback {
        void showUserInfo(People person);

        void show_leave_group_dialog(GroupMembersAdapter mGroupAdapter);

        void show_item(Group group);
    }
}
