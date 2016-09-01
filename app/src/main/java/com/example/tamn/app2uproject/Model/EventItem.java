package com.example.tamn.app2uproject.Model;

/**
 * Created by Tamn on 07/08/2016.
 */
public class EventItem {
    private String title;
    private String content;
    private String url;
    private String eventUploadTime;

    public EventItem() {
        /**
         * Empty Constructor for query
         */
    }

    public EventItem(String title, String content, String url, String eventUploadTime) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.eventUploadTime = eventUploadTime;
    }

    public String getEventUploadTime() {
        return eventUploadTime;
    }

    public void setEventUploadTime(String eventUploadTime) {
        this.eventUploadTime = eventUploadTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
