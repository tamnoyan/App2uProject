package com.example.tamn.app2uproject.Model;

/**
 * Created by Tamn on 24/08/2016.
 */
public class UserDetails {
    private String username;
    private String imageUrl;


    public UserDetails(String username, String imageUrl) {
        this.username = username;
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
