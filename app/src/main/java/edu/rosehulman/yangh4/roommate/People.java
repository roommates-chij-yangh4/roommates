package edu.rosehulman.yangh4.roommate;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hao Yang on 7/21/2017.
 */

public class People implements Parcelable {
    private String first_name;
    private String last_name;
    private String extracontactinfo;
    private Bitmap photo;

    public String getExtracontactinfo() {
        return extracontactinfo;
    }

    public void setExtracontactinfo(String extracontactinfo) {
        this.extracontactinfo = extracontactinfo;
    }

    public People(String first_name, String last_name, Bitmap photo, String email, String phone) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.photo = photo;
        this.email = email;
        this.phone = phone;
        this.extracontactinfo = "The extra contact info of the user will be shown here. The space above is for the user's photo";

    }

    public People() {

    }

    protected People(Parcel in) {
        first_name = in.readString();
        last_name = in.readString();
        extracontactinfo = in.readString();
        photo = in.readParcelable(Bitmap.class.getClassLoader());
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

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(extracontactinfo);
        dest.writeParcelable(photo, flags);
        dest.writeString(email);
        dest.writeString(phone);
    }

    public String getName() {
        return this.first_name + " " + this.last_name;
    }
}
