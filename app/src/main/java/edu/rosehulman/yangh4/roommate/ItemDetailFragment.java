package edu.rosehulman.yangh4.roommate;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;


public class ItemDetailFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ITEM = "ARG_ITEM";

    private Item mItem;


    public ItemDetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ItemDetailFragment newInstance(Item mItem) {
        ItemDetailFragment fragment = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ITEM, mItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItem = getArguments().getParcelable(ARG_ITEM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_detail, container, false);
        ((TextView) view.findViewById(R.id.item_name)).setText("Item Name: " + mItem.getItemname());
        ((TextView) view.findViewById(R.id.item_price)).setText("Price: " + mItem.getItemprice() + "");
        ((TextView) view.findViewById(R.id.item_purchasedby)).setText("Purchased by: " + mItem.getUsername());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mItem.getPurchasedate());
        ((TextView) view.findViewById(R.id.item_date)).setText(calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH))
        ;
        ((TextView) view.findViewById(R.id.item_description)).setText(mItem.getDescription());
        ((MainActivity) getActivity()).setActionBarTitle(mItem.getItemname());

        return view;
    }

}
