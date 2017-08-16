package edu.rosehulman.yangh4.roommate;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Hao Yang on 7/23/2017.
 */

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> implements Parcelable {
    private ArrayList<Item> itemList;

    protected ItemListAdapter(Parcel in) {
        itemList = in.createTypedArrayList(Item.CREATOR);
        GroupKey = in.readString();
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

    public String getGroupKey() {
        return GroupKey;
    }

    private String GroupKey;
    private ItemListFragment.Callback mCallback;
    private DatabaseReference mItemRef;

    public ItemListAdapter(String groupKey, ItemListFragment.Callback callback) {
        GroupKey = groupKey;
        mCallback = callback;
        itemList = new ArrayList<>();
        mItemRef = FirebaseDatabase.getInstance().getReference().child("groups/" + GroupKey + "/items");
        mItemRef.addChildEventListener(new ItemsChildEventListener());
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iteminfo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Item item = itemList.get(position);
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        holder.mDate.setText(df.format(item.getPurchasedate()));
        holder.mPrice.setText(item.getItemprice() + "");
        holder.mItemName.setText(item.getItemname());
        final ItemListAdapter adapter = this;
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mCallback.showEditDialog(adapter, item);
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.showItemDetail(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setCallBack(ItemListFragment.Callback callBack) {
        mCallback = callBack;
    }

    public void sortElement(String option) {
        switch (option) {
            case "DATE":
                Collections.sort(itemList, (new Comparator<Item>() {
                    @Override
                    public int compare(Item o1, Item o2) {
                        return o1.getPurchasedate().compareTo(o2.getPurchasedate());
                    }
                }));
                notifyDataSetChanged();
                return;
            case "NAME":
                Collections.sort(itemList, (new Comparator<Item>() {
                    @Override
                    public int compare(Item o1, Item o2) {
                        return o1.getItemname().compareTo(o2.getItemname());
                    }
                }));
                notifyDataSetChanged();
                return;
            case "PRICE":
                Collections.sort(itemList, (new Comparator<Item>() {
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

    public void addItem(Item item) {
        DatabaseReference temp = mItemRef.push();
        item.setKey(temp.getKey());
        temp.setValue(item);
        FirebaseDatabase.getInstance().getReference().child("members/" + MainActivity.user.getKey() + "/itemkeys/" + item.getKey()).setValue(true);
    }

    public void updateBalance() {
        final DatabaseReference mGroupRef = FirebaseDatabase.getInstance().getReference().child("groups/" + GroupKey);
        mGroupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Group group = dataSnapshot.getValue(Group.class);
                group.setItemlist(itemList);
                group.updateBalance();
                mGroupRef.child("balanceMap").setValue(group.getBalanceMap());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(itemList);
        dest.writeString(GroupKey);
    }

    public void updateItem(Item item) {
        String key = item.getKey();
        mItemRef.child(key).setValue(item);
    }

    public void clear() {
        mItemRef.removeValue();
        DatabaseReference mGroupRef = FirebaseDatabase.getInstance().getReference().child("groups/" + GroupKey);
        mGroupRef.child("balanceMap").removeValue();
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

    public class ItemsChildEventListener implements ChildEventListener {
        private void add(DataSnapshot dataSnapshot) {
            Item item = dataSnapshot.getValue(Item.class);
            item.setKey(dataSnapshot.getKey());
            for (Item check : itemList) {
                if (check.getKey().equals(item.getKey())) {
                    return;
                }
            }
            itemList.add(item);
        }

        private int remove(String key) {
            for (Item item : itemList) {
                if (item.getKey().equals(key)) {
                    int pos = itemList.indexOf(item);
                    itemList.remove(item);
                    return pos;
                }
            }
            return -1;
        }


        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            add(dataSnapshot);
            notifyDataSetChanged();
            updateBalance();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            remove(dataSnapshot.getKey());
            add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            remove(dataSnapshot.getKey());
            notifyDataSetChanged();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
