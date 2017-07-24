package edu.rosehulman.yangh4.roommate;

import android.content.Context;
import android.net.Uri;
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

import java.util.ArrayList;


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

    public GroupListFragment() {
        // Required empty public constructor
    }

    public static GroupListFragment newInstance(ArrayList<Group> grouplist) {
        GroupListFragment fragment = new GroupListFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_Adapter, new GroupAdapter(grouplist, mCallback));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mGroupAdapter = getArguments().getParcelable(ARG_Adapter);
            mGroupAdapter.setmCallback(mCallback);
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
        if (mGroupAdapter == null) {
            mGroupAdapter = new GroupAdapter(new ArrayList<Group>(), mCallback);
        }
        view.setAdapter(mGroupAdapter);
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
    }
}
