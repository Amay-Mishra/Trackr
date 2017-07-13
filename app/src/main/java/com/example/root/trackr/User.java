package com.example.root.trackr;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 13/7/17.
 */

public class User implements Parcelable {
    public User(@Nullable String phone, @Nullable  String password, @Nullable int id, @Nullable String fname, @Nullable String lname, @Nullable String authToken) {
        this.phone = phone;
        this.password = password;
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.authToken = authToken;
    }

    @SerializedName("phone")
    private String phone;
    @SerializedName("password")
    private String password;
    @SerializedName("id")
    private int id;
    @SerializedName("fname")
    private String fname;
    @SerializedName("lname")
    private String lname;
    @SerializedName("auth_token")
    private String authToken;

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

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
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
        dest.writeString(this.fname);
        dest.writeString(this.lname);
        dest.writeString(this.authToken);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.phone = in.readString();
        this.password = in.readString();
        this.id = in.readInt();
        this.fname = in.readString();
        this.lname = in.readString();
        this.authToken = in.readString();
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
