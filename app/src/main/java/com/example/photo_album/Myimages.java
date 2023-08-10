package com.example.photo_album;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "my_images")
public class Myimages {

    @PrimaryKey(autoGenerate = true)
    public int image_id;
    public String image_title;
    public String image_description;
    public byte[] image;

    public Myimages(int image_id, String image_title, String image_description, byte[] image) {
        this.image_id = image_id;
        this.image_title = image_title;
        this.image_description = image_description;
        this.image = image;
    }

    public Myimages(String title, String description, byte[] image) {
        this.image_title = title;
        this.image_description = description;
        this.image = image;
    }

    public int getImage_id() {
        return image_id;
    }

    public String getImage_title() {
        return image_title;
    }

    public String getImage_description() {
        return image_description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }
}
