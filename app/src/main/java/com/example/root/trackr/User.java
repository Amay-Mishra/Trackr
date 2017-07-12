package com.example.root.trackr;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

/**
 * Created by Debargha Bhattacharjee on 12/7/17.
 */

public class User implements Parcelable {

    public User(@Nullable String phone, @Nullable String password, @Nullable int id, @Nullable String lname, @Nullable String fname) {
        this.phone = phone;
        this.password = password;
        this.id = id;
        this.lname = lname;
        this.fname = fname;
    }

    @com.google.gson.annotations.SerializedName("phone")
    private String phone;
    @com.google.gson.annotations.SerializedName("password")
    private String password;
    @com.google.gson.annotations.SerializedName("id")
    private int id;
    @com.google.gson.annotations.SerializedName("lname")
    private String lname;
    @com.google.gson.annotations.SerializedName("fname")
    private String fname;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.phone);
        dest.writeString(this.password);
        dest.writeInt(this.id);
        dest.writeString(this.lname);
        dest.writeString(this.fname);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.phone = in.readString();
        this.password = in.readString();
        this.id = in.readInt();
        this.lname = in.readString();
        this.fname = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
