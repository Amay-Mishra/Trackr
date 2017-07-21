package com.example.root.trackr;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 20/7/17.
 */

public class FriendList implements Parcelable {

    @SerializedName("id")
    private int id;

    public FriendList(int id, String name, int permissionStatus) {
        this.id = id;
        this.name = name;
        this.permissionStatus = permissionStatus;
    }

    @SerializedName("name")

    private String name;
    @SerializedName("permission_status")
    private int permissionStatus;

    public int getId() {
        return id;
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

    public int getPermissionStatus() {
        return permissionStatus;
    }

    public void setPermissionStatus(int permissionStatus) {
        this.permissionStatus = permissionStatus;
    }

    public FriendList() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.permissionStatus);
    }

    protected FriendList(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.permissionStatus = in.readInt();
    }

    public static final Creator<FriendList> CREATOR = new Creator<FriendList>() {
        @Override
        public FriendList createFromParcel(Parcel source) {
            return new FriendList(source);
        }

        @Override
        public FriendList[] newArray(int size) {
            return new FriendList[size];
        }
    };
}
