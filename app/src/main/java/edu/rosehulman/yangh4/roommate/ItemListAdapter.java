package edu.rosehulman.yangh4.roommate;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Hao Yang on 7/23/2017.
 */

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> implements Parcelable {
    private Group mGroup;
    private ItemListFragment.Callback mCallback;

    public ItemListAdapter(Group group, ItemListFragment.Callback callback) {
        mGroup = group;
        mCallback = callback;
    }

    protected ItemListAdapter(Parcel in) {
        mGroup = in.readParcelable(Group.class.getClassLoader());
    }

    public static final Creator<ItemListAdapter> CREATOR = new Creator<ItemListAdapter>() {
        @Override
        public ItemListAdapter createFromParcel(Parcel in) {
            return new ItemListAdapter(in);
        }

        @Override
        public ItemListAdapter[] newArray(int size) {
            return new ItemListAdapter[size];
        }
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iteminfo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Item item = mGroup.getItemlist().get(position);
        holder.mDate.setText(item.getPurchasedate().toString());
        holder.mPrice.setText(item.getItemprice() + "");
        holder.mItemName.setText(item.getItemname());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.showItemDetail(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGroup.getItemlist().size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mGroup, flags);
    }

    public void setCallBack(ItemListFragment.Callback callBack) {
        mCallback = callBack;
    }

    public void sortElement(String option) {
        switch (option) {
            case "DATE":
                Collections.sort(mGroup.getItemlist(), (new Comparator<Item>() {
                    @Override
                    public int compare(Item o1, Item o2) {
                        return o1.getPurchasedate().compareTo(o2.getPurchasedate());
                    }
                }));
                notifyDataSetChanged();
                return;
            case "NAME":
                Collections.sort(mGroup.getItemlist(), (new Comparator<Item>() {
                    @Override
                    public int compare(Item o1, Item o2) {
                        return o1.getItemname().compareTo(o2.getItemname());
                    }
                }));
                notifyDataSetChanged();
                return;
            case "PRICE":
                Collections.sort(mGroup.getItemlist(), (new Comparator<Item>() {
                    @Override
                    public int compare(Item item1, Item item2) {
                        double o1 = item1.getItemprice();
                        double o2 = item2.getItemprice();
                        return o1 < o2 ? -1 : (o1 == o2 ? 0 : 1);
                    }
                }));
                notifyDataSetChanged();
                return;
        }
    }

    public Group getGroup() {
        return mGroup;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mItemName;
        TextView mPrice;
        TextView mDate;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemName = (TextView) itemView.findViewById(R.id.item_name);
            mPrice = (TextView) itemView.findViewById(R.id.price_text);
            mDate = (TextView) itemView.findViewById(R.id.date_text);
        }
    }
}
