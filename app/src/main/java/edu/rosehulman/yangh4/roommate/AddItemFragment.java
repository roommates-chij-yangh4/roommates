package edu.rosehulman.yangh4.roommate;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddItemFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_GROUP = "destination_group_tag";

    // TODO: Rename and change types of parameters
    private ItemListAdapter mAdapter;
    private AddItemFragment.Callback mCallback;


    public AddItemFragment() {
        // Required empty public constructor
    }

    public static AddItemFragment newInstance(ItemListAdapter mAdapter) {
        AddItemFragment fragment = new AddItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_GROUP, mAdapter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAdapter = getArguments().getParcelable(ARG_GROUP);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddItemFragment.Callback) {
            mCallback = (AddItemFragment.Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);
        final TextView itemname = (TextView) view.findViewById(R.id.item_name_input);
        final TextView itemdesc = (TextView) view.findViewById(R.id.description_input);
        final TextView itemprice = (TextView) view.findViewById(R.id.Price_input);
        final TextView itemtag = (TextView) view.findViewById(R.id.Tag_input);
        final TextView itemdate = (TextView) view.findViewById(R.id.input_date);
        Button add = (Button) view.findViewById(R.id.add_item_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item = new Item(itemname.getText(), itemprice.getText(), itemtag.getText(), itemdesc.getText());
                String datestr = itemdate.getText().toString();
                String year = datestr.substring(0, 4);
                String month = datestr.substring(5, 7);
                String day = datestr.substring(8, 10);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
                item.setPurchasedate(calendar.getTime());
                mAdapter.getGroup().addItem(item);
                mAdapter.notifyDataSetChanged();
                mAdapter.getGroup().updateBalance();
                mCallback.backtolastlevel();
            }
        });
        return view;
    }

    public interface Callback {
        void backtolastlevel();
    }
}
