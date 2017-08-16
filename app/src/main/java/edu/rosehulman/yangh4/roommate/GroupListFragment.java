package edu.rosehulman.yangh4.roommate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_Adapter = "Group_List_Adapter";

    public GroupAdapter getmGroupAdapter() {
        return mGroupAdapter;
    }

    // TODO: Rename and change types of parameters
    private GroupAdapter mGroupAdapter;

    private OnFragmentInteractionListener mListener;
    private static Callback mCallback;
    private String mUid;

    public GroupListFragment() {
        // Required empty public constructor
    }

    public static GroupListFragment newInstance(String uid) {
        GroupListFragment fragment = new GroupListFragment();
        Bundle args = new Bundle();
        args.putString("user id", uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mUid = getArguments().getString("user id");
//            mGroupAdapter = getArguments().getParcelable(ARG_Adapter);
//            mGroupAdapter.setmCallback(mCallback);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.group_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout relativeview = (RelativeLayout) inflater.inflate(R.layout.group_recycler_view, container, false);
        RecyclerView view = (RecyclerView) relativeview.findViewById(R.id.group_list_view);
        view.setLayoutManager(new LinearLayoutManager(getContext()));
        mGroupAdapter = new GroupAdapter(mCallback, mUid);
        view.setAdapter(mGroupAdapter);
        FloatingActionButton fab = (FloatingActionButton) relativeview.findViewById(R.id.group_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("FABCLICK", "ADD ITEM");
                mCallback.joinGroup(mGroupAdapter);
            }
        });
        ((MainActivity) getActivity()).setActionBarTitle("Group List");
        return relativeview;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GroupListFragment.Callback) {
            mCallback = (GroupListFragment.Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_create_group:
                mCallback.createNewGroup(mGroupAdapter);
                break;
//            case R.id.action_feedback:
//                break;
//            case R.id.action_settings:
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public interface Callback {
        void showgroupmember(Group group);

        void createNewGroup(GroupAdapter mGroupAdapter);

        void joinGroup(GroupAdapter mGroupAdapter);

        void showEditGroupDialog(Group group);
    }
}
