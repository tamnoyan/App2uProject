package com.example.tamn.app2uproject.Model;

/**
 * Created by Tamn on 07/08/2016.
 */
public class MessageItem {
    private String title;
    private String content;
    private String url;
    //TODO: add image - use the url

    public MessageItem() {
        /**
         * Empty Constructor for query
         */
    }

    public MessageItem(String title, String content, String url) {
        this.title = title;
        this.content = content;
        this.url = url;
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
