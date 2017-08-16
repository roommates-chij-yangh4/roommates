package edu.rosehulman.yangh4.roommate;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hao Yang on 7/22/2017.
 */

public class Group implements Parcelable {
    private ArrayList<Item> itemlist;
    private String password;
    private String id;
    private HashMap<String, Boolean> memberkeys;
    private String groupname;

    protected Group(Parcel in) {
        itemlist = in.createTypedArrayList(Item.CREATOR);
        password = in.readString();
        id = in.readString();
        groupname = in.readString();
        description = in.readString();
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public HashMap<String, Double> getBalanceMap() {
        return balanceMap;
    }

    public void setBalanceMap(HashMap<String, Double> balanceMap) {
        this.balanceMap = balanceMap;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public HashMap<String, Boolean> getMemberkeys() {
        return memberkeys;
    }

    public void setMemberkeys(HashMap<String, Boolean> memberkeys) {
        this.memberkeys = memberkeys;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Item> getItemlist() {
        return itemlist;
    }

    public void setItemlist(ArrayList<Item> itemlist) {
        this.itemlist = itemlist;
    }

    public void addItem(Item item) {
        itemlist.add(item);
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;


    private HashMap<String, Double> balanceMap = new HashMap<>();


    public Group() {
        this.itemlist = new ArrayList<>();
        this.memberkeys = new HashMap<>();
        this.description = "Description should be here";
    }

    public String getGroupname() {
        return groupname;
    }

    public void updateBalance() {
        for (String key : balanceMap.keySet()) {
            balanceMap.put(key, 0.0);
        }
        for (Item item : itemlist) {
            for (String key : balanceMap.keySet()) {
                if (item.getUserkey().equals(key)) {
                    int size = memberkeys.size();
                    balanceMap.put(key, balanceMap.get(key) + (item.getItemprice()) * ((size - 1 == 0 ? 1 : size - 1)) / size);
                } else {
                    balanceMap.put(key, balanceMap.get(key) - item.getItemprice() / memberkeys.size());
                }
            }
        }
        return;
    }


    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public void addMember(People member) {
        this.memberkeys.put(member.getKey(), true);
    }

    public void remove(Item item) {
        itemlist.remove(item);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(itemlist);
        dest.writeString(password);
        dest.writeString(id);
        dest.writeString(groupname);
        dest.writeString(description);
    }
}
