package edu.rosehulman.yangh4.roommate;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateGroupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_GROUP_ADAPTER = "group_adapter";

    // TODO: Rename and change types of parameters
    private GroupAdapter mAdapter;
    private CreateGroupFragment.Callback mCallback;


    public CreateGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateGroupFragment.Callback) {
            mCallback = (CreateGroupFragment.Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    // TODO: Rename and change types and number of parameters
    public static CreateGroupFragment newInstance(GroupAdapter groupAdapter) {
        CreateGroupFragment fragment = new CreateGroupFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_GROUP_ADAPTER, groupAdapter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAdapter = getArguments().getParcelable(ARG_GROUP_ADAPTER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_group, container, false);
        ((MainActivity) getActivity()).setActionBarTitle("Create Group");
        final TextView groupName = (TextView) view.findViewById(R.id.group_name_input);
        final TextView groupID = (TextView) view.findViewById(R.id.group_id);
        final TextView groupPW = (TextView) view.findViewById(R.id.group_password);
        final TextView groupPWC = (TextView) view.findViewById(R.id.group_password_confirm);
        Button button = (Button) view.findViewById(R.id.group_create_confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cancel = false;
                View focusView = null;

                if (!groupPW.getText().toString().equals(groupPWC.getText().toString())) {
                    groupPW.setError("Password does not match");
                    focusView = groupPWC;
                    cancel = true;
                }

                if (TextUtils.isEmpty(groupName.getText())) {
                    groupName.setError(getString(R.string.field_required));
                    focusView = groupName;
                    cancel = true;
                }
                if (TextUtils.isEmpty(groupPW.getText())) {
                    groupPW.setError(getString(R.string.field_required));
                    focusView = groupPW;
                    cancel = true;
                }
                if (TextUtils.isEmpty(groupPWC.getText())) {
                    groupPWC.setError(getString(R.string.field_required));
                    focusView = groupPWC;
                    cancel = true;
                }
                if (TextUtils.isEmpty(groupID.getText())) {
                    groupID.setError(getString(R.string.field_required));
                    focusView = groupID;
                    cancel = true;
                } else {
                    DatabaseReference mGroupRef = FirebaseDatabase.getInstance().getReference().child("groups/" + groupID.getText().toString());
                    mGroupRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue(Group.class) != null) {
                                onCreateGroupError("Group with same ID already exist!");
                            } else {
                                Group group = new Group();
                                group.setId(groupID.getText().toString());
                                group.setGroupname(groupName.getText().toString());
                                group.setPassword(groupPW.getText().toString());
                                DatabaseReference mPeopleRef = FirebaseDatabase.getInstance().getReference().child("members/" + MainActivity.user.getKey());
                                mPeopleRef.child("groupkeys/" + group.getId()).setValue(true);
                                mAdapter.add(group);
                                mAdapter.notifyDataSetChanged();
                                mCallback.backtolastlevel();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    if (cancel) {
                        focusView.requestFocus();
                        return;
                    }
                }
            }
        });
        return view;
    }

    public void onCreateGroupError(String message) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Create Group Error!")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();
    }

    public interface Callback {
        void backtolastlevel();
    }


}
