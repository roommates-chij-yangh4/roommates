package edu.rosehulman.yangh4.roommate;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                mCallback.show_item(mGroupAdapter.getId());
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
        FloatingActionButton fab = (FloatingActionButton) relativeview.findViewById(R.id.member_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("INVITESOMEONE", "INVITE");
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Invite User");
                View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_invite, null, false);
                builder.setView(view);
                final TextView KeyEditText = (TextView) view.findViewById(R.id.dialog_invite_user_key);
                builder.setPositiveButton("Invite", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String id = KeyEditText.getText().toString();
                        if (id == null || id.length() == 0) {
                            onInviteUserError("ID is empty");
                            return;
                        }
                        final String groupid = mGroupAdapter.getId();
                        DatabaseReference mMemberRef = FirebaseDatabase.getInstance().getReference().child("members");
                        mMemberRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot == null) {
                                    onInviteUserError("User Does Not Exist");
                                } else {
                                    DatabaseReference mMessageRef = FirebaseDatabase.getInstance().getReference().child("messages");
                                    mMessageRef.push().setValue(new Message(MainActivity.user.getName(), id, groupid));
                                    Toast.makeText(getContext(), "You successfully invited the user", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });//Check if the user exist
                    }
                }).show();

            }
        });
        if (mGroupAdapter == null) {
            mGroupAdapter = new GroupMembersAdapter(new Group(), mCallback);
        }
        view.setAdapter(mGroupAdapter);
        ((MainActivity) getActivity()).setActionBarTitle(mGroupAdapter.getId() + "'s members");
        return relativeview;
    }

    public void onInviteUserError(String message) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Invite User Error!")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();
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

        void show_item(String groupKey);
    }
}
