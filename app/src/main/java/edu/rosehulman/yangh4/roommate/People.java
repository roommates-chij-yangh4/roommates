package edu.rosehulman.yangh4.roommate;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by Hao Yang on 7/21/2017.
 */

public class People implements Parcelable {
    private String first_name;
    private String last_name;
    private String extracontactinfo;
    private HashMap<String, Boolean> groupkeys = new HashMap<>();
    private HashMap<String, Boolean> itemkeys = new HashMap<>();

    protected People(Parcel in) {
        first_name = in.readString();
        last_name = in.readString();
        extracontactinfo = in.readString();
        key = in.readString();
        email = in.readString();
        phone = in.readString();
    }

    public static final Creator<People> CREATOR = new Creator<People>() {
        @Override
        public People createFromParcel(Parcel in) {
            return new People(in);
        }

        @Override
        public People[] newArray(int size) {
            return new People[size];
        }
    };

    public void setKey(String key) {
        this.key = key;
    }

    private String key;

    public String getExtracontactinfo() {
        return extracontactinfo;
    }

    public void setExtracontactinfo(String extracontactinfo) {
        this.extracontactinfo = extracontactinfo;
    }

    public People(String first_name, String last_name, String email, String phone) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        this.extracontactinfo = "The extra contact info of the user will be shown here.";

    }

    public People() {

    }


    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
//
//    public Bitmap getPhoto() {
//        return photo;
//    }
//
//    public void setPhoto(Bitmap photo) {
//        this.photo = photo;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String email;
    private String phone;

    public String getName() {
        return this.first_name + " " + this.last_name;
    }

    public String getKey() {
        return key;
    }

    public HashMap<String, Boolean> getGroupkeys() {
        return groupkeys;
    }

    public void setGroupkeys(HashMap<String, Boolean> groupkeys) {
        this.groupkeys = groupkeys;
    }

    public HashMap<String, Boolean> getItemkeys() {
        return itemkeys;
    }

    public void setItemkeys(HashMap<String, Boolean> itemkeys) {
        this.itemkeys = itemkeys;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(extracontactinfo);
        dest.writeString(key);
        dest.writeString(email);
        dest.writeString(phone);
    }
}
