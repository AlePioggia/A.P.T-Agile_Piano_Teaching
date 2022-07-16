package com.example.apt_agile_piano_teaching.models;

public class ImageUpload {

    private String mName;
    private String mImageUrl;

    public ImageUpload() {
        //Empty constructor needed
    }

    public ImageUpload(String name, String imageUrl) {
        if (name.trim().equals("")) {
            name = "empty name";
        }

        mName = name;
        mImageUrl = imageUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}
