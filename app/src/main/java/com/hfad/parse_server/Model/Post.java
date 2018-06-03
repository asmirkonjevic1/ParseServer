package com.hfad.parse_server.Model;

import android.graphics.Bitmap;
import android.widget.TextView;

public class Post {

    private String objectId;
    private Bitmap image;
    private String username;


    public Post(String objectId, Bitmap image, String username) {
        this.objectId = objectId;
        this.image = image;
        this.username = username;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
