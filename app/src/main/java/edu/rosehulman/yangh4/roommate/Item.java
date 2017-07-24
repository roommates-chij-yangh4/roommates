package edu.rosehulman.yangh4.roommate;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Hao Yang on 7/21/2017.
 */

public class Item implements Parcelable {
    private String itemname;
    private String Username;
    private double itemprice;
    private Bitmap itempicture;
    private Date purchasedate;
    private String tag;
    private String description;

    public Item() {
    }

    public Item(CharSequence name, CharSequence price, CharSequence tag, CharSequence description) {
        Username = MainActivity.user.getName();
        itemname = name.toString();
        itemprice = Double.parseDouble(price.toString());
        this.tag = tag.toString();
        this.description = description.toString();
        purchasedate = new Date(System.currentTimeMillis());
    }

    protected Item(Parcel in) {
        itemname = in.readString();
        Username = in.readString();
        itemprice = in.readDouble();
        itempicture = in.readParcelable(Bitmap.class.getClassLoader());
        tag = in.readString();
        description = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public double getItemprice() {
        return itemprice;
    }

    public void setItemprice(double itemprice) {
        this.itemprice = itemprice;
    }

    public Bitmap getItempicture() {
        return itempicture;
    }

    public void setItempicture(Bitmap itempicture) {
        this.itempicture = itempicture;
    }

    public Date getPurchasedate() {
        return purchasedate;
    }

    public void setPurchasedate(Date purchasedate) {
        this.purchasedate = purchasedate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemname);
        dest.writeString(Username);
        dest.writeDouble(itemprice);
        dest.writeParcelable(itempicture, flags);
        dest.writeString(tag);
        dest.writeString(description);
    }
}
