package com.example.root.trackr;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Debargha Bhattacharjee on 12/7/17.
 */

public class UserHasFriend implements Parcelable {

    public UserHasFriend(@Nullable int friendId, @Nullable int permissionStatus, @Nullable int userId) {
        this.friendId = friendId;
        this.permissionStatus = permissionStatus;
        this.userId = userId;
    }

    @SerializedName("friend_id")
    private int friendId;
    @SerializedName("permission_status")
    private int permissionStatus;
    @SerializedName("user_id")
    private int userId;

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public int getPermissionStatus() {
        return permissionStatus;
    }

    public void setPermissionStatus(int permissionStatus) {
        this.permissionStatus = permissionStatus;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.friendId);
        dest.writeInt(this.permissionStatus);
        dest.writeInt(this.userId);
    }

    public UserHasFriend() {
    }

    protected UserHasFriend(Parcel in) {
        this.friendId = in.readInt();
        this.permissionStatus = in.readInt();
        this.userId = in.readInt();
    }

    public static final Parcelable.Creator<UserHasFriend> CREATOR = new Parcelable.Creator<UserHasFriend>() {
        @Override
        public UserHasFriend createFromParcel(Parcel source) {
            return new UserHasFriend(source);
        }

        @Override
        public UserHasFriend[] newArray(int size) {
            return new UserHasFriend[size];
        }
    };
}
