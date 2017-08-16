package edu.rosehulman.yangh4.roommate;

import android.content.Context;
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

public class ItemListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_Adapter = "ItemListAdapter";
    private static ItemListFragment.Callback mCallback;

    // TODO: Rename and change types of parameters
    private ItemListAdapter mAdapter;

    private Callback mListener;

    public ItemListFragment() {
        // Required empty public constructor
    }

    public static ItemListFragment newInstance(String groupKey) {
        ItemListFragment fragment = new ItemListFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_Adapter, new ItemListAdapter(groupKey, mCallback));
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mAdapter = getArguments().getParcelable(ARG_Adapter);
            mAdapter.setCallBack(mCallback);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.item_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sort_name:
                mAdapter.sortElement("NAME");
                break;
            case R.id.action_sort_date:
                mAdapter.sortElement("DATE");
                break;
            case R.id.action_sort_price:
                mAdapter.sortElement("PRICE");
                break;
            case R.id.action_clear_all:
                mAdapter.clear();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout relativeview = (RelativeLayout) inflater.inflate(R.layout.item_recycler_view, container, false);
        RecyclerView view = (RecyclerView) relativeview.findViewById(R.id.item_list_view);
        view.setLayoutManager(new LinearLayoutManager(getContext()));
        FloatingActionButton fab = (FloatingActionButton) relativeview.findViewById(R.id.item_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("FABCLICK", "ADD ITEM");
                mCallback.addItem(mAdapter);
            }
        });
        if (mAdapter == null) {
            mAdapter = new ItemListAdapter("", mCallback);
        }
        view.setAdapter(mAdapter);
        ((MainActivity) getActivity()).setActionBarTitle(mAdapter.getGroupKey() + "'s Items");
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
        void showItemDetail(Item item);

        void addItem(ItemListAdapter mAdapter);

        void showEditDialog(ItemListAdapter mAdapter, Item item);
    }
}
