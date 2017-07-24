package edu.rosehulman.yangh4.roommate;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


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
        final TextView groupName = (TextView) view.findViewById(R.id.group_name_input);
        final TextView groupID = (TextView) view.findViewById(R.id.group_id);
        final TextView groupPW = (TextView) view.findViewById(R.id.group_password);
        TextView groupPWC = (TextView) view.findViewById(R.id.group_password_confirm);
        Button button = (Button) view.findViewById(R.id.group_create_confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Group group = new Group();
                group.addMember(MainActivity.user);
                group.setId(groupID.getText().toString());
                group.setGroupname(groupName.getText().toString());
                group.setPassword(groupPW.toString());
                mAdapter.getmGroupList().add(group);
                mAdapter.notifyDataSetChanged();
                mCallback.backtolastlevel();
            }
        });
        return view;
    }


    public interface Callback {
        void backtolastlevel();
    }


}
