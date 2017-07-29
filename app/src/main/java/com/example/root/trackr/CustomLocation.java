package com.example.root.trackr;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Debargha Bhattacharjee on 12/7/17.
 */

public class CustomLocation implements Parcelable {
    public CustomLocation(@Nullable String latitude, @Nullable int userId, @Nullable String longitude) {
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

    public CustomLocation() {
    }

    protected CustomLocation(Parcel in) {
        this.latitude = in.readString();
        this.userId = in.readInt();
        this.longitude = in.readString();
    }

    public static final Parcelable.Creator<CustomLocation> CREATOR = new Parcelable.Creator<CustomLocation>() {
        @Override
        public CustomLocation createFromParcel(Parcel source) {
            return new CustomLocation(source);
        }

        @Override
        public CustomLocation[] newArray(int size) {
            return new CustomLocation[size];
        }
    };
}
