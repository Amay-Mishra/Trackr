package com.example.root.trackr;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Debargha Bhattacharjee on 12/7/17.
 */

public class Location implements Parcelable {
    public Location(@Nullable String latitude, @Nullable int userId, @Nullable String longitude) {
        this.latitude = latitude;
        this.userId = userId;
        this.longitude = longitude;
    }

    @SerializedName("latitude")
    private String latitude;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("longitude")
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.latitude);
        dest.writeInt(this.userId);
        dest.writeString(this.longitude);
    }

    public Location() {
    }

    protected Location(Parcel in) {
        this.latitude = in.readString();
        this.userId = in.readInt();
        this.longitude = in.readString();
    }

    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
