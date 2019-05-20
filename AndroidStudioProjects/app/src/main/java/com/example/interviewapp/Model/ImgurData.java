package com.example.interviewapp.Model;

public class ImgurData {

    //This is the data that we are going to store, they are private because we are going to access it by the Getters/Setters
    private String title;
    private String image;

    //Default constructor. Is a very good practice to do this
    public ImgurData() {
    }

    //Constructor with parameters
    public ImgurData(String title, String image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}