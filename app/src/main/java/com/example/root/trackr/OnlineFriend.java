package com.example.root.trackr;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 20/7/17.
 */

public class OnlineFriend implements Parcelable {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    public int getId() {
        return id;
    }

    public OnlineFriend(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public OnlineFriend() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    protected OnlineFriend(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Creator<OnlineFriend> CREATOR = new Creator<OnlineFriend>() {
        @Override
        public OnlineFriend createFromParcel(Parcel source) {
            return new OnlineFriend(source);
        }

        @Override
        public OnlineFriend[] newArray(int size) {
            return new OnlineFriend[size];
        }
    };
}
