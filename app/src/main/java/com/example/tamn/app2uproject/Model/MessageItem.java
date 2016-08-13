package com.example.tamn.app2uproject.Model;

/**
 * Created by Tamn on 07/08/2016.
 */
public class MessageItem {
    private String title;
    private String content;
    //TODO: add image - use the url

    public MessageItem() {
        /**
         * Empty Constructor for query
         */
    }

    public MessageItem(String title, String content) {
        this.title = title;
        this.content = content;
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


}
