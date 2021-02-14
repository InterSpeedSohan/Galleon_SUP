package com.example.galleonsup.model;

import android.os.Parcel;
import android.os.Parcelable;



public class Notification implements Parcelable {
    String id, header, body, url, type, isSeen;
    public Notification(String id, String header, String body, String url, String type, String isSeen)
    {
        this.id = id;
        this.header = header;
        this.body = body;
        this.url = url;
        this.type = type;
        this.isSeen = isSeen;
    }

    protected Notification(Parcel in) {
        id = in.readString();
        header = in.readString();
        body = in.readString();
        url = in.readString();
        type = in.readString();
        isSeen = in.readString();
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public String getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(String isSeen) {
        this.isSeen = isSeen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(header);
        parcel.writeString(body);
        parcel.writeString(url);
        parcel.writeString(type);
        parcel.writeString(isSeen);
    }
}
