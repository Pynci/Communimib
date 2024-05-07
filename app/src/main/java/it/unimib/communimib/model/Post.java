package it.unimib.communimib.model;

import java.util.ArrayList;
import java.util.List;

public class Post {

    private String pid;
    private String title;
    private String description;
    private String category;
    private User author;
    private String email;
    private String link;
    private long timestamp;
    private List<String> pictures;


    public Post(){
        pictures = new ArrayList<>();
    }

    public Post(String title, String description, String category, User author, String email, String link, long timestamp) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.author = author;
        this.email = email;
        this.link = link;
        this.timestamp = timestamp;
        pictures = new ArrayList<>();
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }
}
