package com.example.galleonsup.model;

public class Notification {
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
}
