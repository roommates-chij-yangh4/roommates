package edu.rosehulman.yangh4.roommate;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Hao Yang on 7/22/2017.
 */

public class Group implements Parcelable {
    private ArrayList<Item> itemlist;
    private String password;
    private String id;
    private ArrayList<People> groupmembers;
    private String groupname;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    protected Group(Parcel in) {
        groupname = in.readString();

        description = in.readString();
        balance = in.readDouble();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    private double balance = 0;


    public Group() {
        this.itemlist = new ArrayList<>();
        this.groupmembers = new ArrayList<>();
        this.description = "Description should be here";
    }

    public ArrayList<People> getGroupmembers() {
        return groupmembers;
    }

    public String getGroupname() {
        return groupname;
    }

    public void updateBalance() {
        double price = 0;
        for (Item items : itemlist) {
            price += items.getItemprice();
        }
        this.balance = price / groupmembers.size();
    }


    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public void addMember(People member) {
        this.groupmembers.add(member);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(groupname);
        dest.writeString(description);
        dest.writeDouble(balance);
    }
}
