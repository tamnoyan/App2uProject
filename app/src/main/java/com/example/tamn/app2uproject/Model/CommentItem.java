package com.example.tamn.app2uproject.Model;

/**
 * Created by Tamn on 08/08/2016.
 */
public class CommentItem {
    //private String title;
    private String comment;
    private String email;



    public CommentItem() {
        /*must have an empty constructor*/
    }

    public CommentItem(String comment, String email) {
        this.comment = comment;
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}