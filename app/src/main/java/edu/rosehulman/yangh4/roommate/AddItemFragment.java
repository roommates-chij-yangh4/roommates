package edu.rosehulman.yangh4.roommate;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
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
    private static final String ARG_CREATE = "CREATE_BOOLEAN";
    private static final String ARG_ITEM = "ITEM_TAG";

    // TODO: Rename and change types of parameters
    private ItemListAdapter mAdapter;
    private AddItemFragment.Callback mCallback;
    private Boolean create;
    private Item mItem;

    public AddItemFragment() {
        // Required empty public constructor
    }

    public static AddItemFragment newInstance(ItemListAdapter mAdapter, boolean b) {
        AddItemFragment fragment = new AddItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_GROUP, mAdapter);
        args.putBoolean(ARG_CREATE, b);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAdapter = getArguments().getParcelable(ARG_GROUP);
            create = getArguments().getBoolean(ARG_CREATE);
            if (!create) {
                mItem = getArguments().getParcelable(ARG_ITEM);
            }
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
        final EditText itemname = (EditText) view.findViewById(R.id.item_name_input);
        final EditText itemdesc = (EditText) view.findViewById(R.id.description_input);
        final EditText itemprice = (EditText) view.findViewById(R.id.Price_input);
        final EditText itemdate = (EditText) view.findViewById(R.id.input_date);
        Button add = (Button) view.findViewById(R.id.add_item_button);
        if (!create) {
            ((MainActivity) getActivity()).setActionBarTitle("Edit Item");
            itemname.setText(mItem.getItemname());
            itemdesc.setText(mItem.getDescription());
            itemprice.setText(mItem.getItemprice() + "");
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            itemdate.setText(df.format(mItem.getPurchasedate()));
            add.setText("Confirm");
            itemprice.setVisibility(View.GONE);
        } else {
            ((MainActivity) getActivity()).setActionBarTitle("Add Item");
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item;
                boolean cancel = false;
                View focusView = null;

                if (TextUtils.isEmpty(itemname.getText().toString())) {
                    itemname.setError(getString(R.string.field_required));
                    focusView = itemname;
                    cancel = true;
                }
                if (TextUtils.isEmpty(itemdesc.getText().toString())) {
                    itemdesc.setError(getString(R.string.field_required));
                    focusView = itemdesc;
                    cancel = true;
                }
                if (TextUtils.isEmpty(itemprice.getText().toString())) {
                    itemprice.setError(getString(R.string.field_required));
                    focusView = itemprice;
                    cancel = true;
                }
                if (TextUtils.isEmpty(itemdate.getText().toString())) {
                    itemdate.setError(getString(R.string.field_required));
                    focusView = itemdate;
                    cancel = true;
                } else {
                    try {
                        parsedate(itemdate.getText().toString());
                    } catch (Exception e) {
                        itemdate.setError("Date Invalid. Enter as YYYY/MM/DD");
                        focusView = itemdate;
                        cancel = true;
                    }
                }

                if (cancel) {
                    focusView.requestFocus();
                    return;
                }

                if (create) {
                    item = new Item(itemname.getText(), itemprice.getText(), itemdesc.getText());
                    item.setUsername(MainActivity.user.getName());
                } else {
                    item = mItem;
                    item.setUsername(MainActivity.user.getName());
                    item.setItemname(itemname.getText().toString());
                    item.setDescription(itemdesc.getText().toString());
                    item.setItemprice(Double.parseDouble(itemprice.getText().toString()));
                }
                item.setGroupkey(mAdapter.getGroupKey());
                item.setPurchasedate(parsedate(itemdate.getText().toString()).getTime());
                if (create) {
                    mAdapter.addItem(item);
                } else {
                    mAdapter.updateItem(item);
                }
                mAdapter.notifyDataSetChanged();
                mCallback.backtolastlevel();
            }
        });
        return view;
    }

    public Calendar parsedate(String date) {
        String datestr = date;
        String year = datestr.substring(0, 4);
        String month = datestr.substring(5, 7);
        String day = datestr.substring(8, 10);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
        return calendar;
    }

    public static Fragment newInstance(ItemListAdapter mAdapter, Item item, boolean b) {
        AddItemFragment fragment = new AddItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_GROUP, mAdapter);
        args.putParcelable(ARG_ITEM, item);
        args.putBoolean(ARG_CREATE, b);
        fragment.setArguments(args);
        return fragment;
    }

    public interface Callback {
        void backtolastlevel();
    }
}
