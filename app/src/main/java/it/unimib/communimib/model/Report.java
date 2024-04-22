package it.unimib.communimib.model;

import com.google.firebase.database.Exclude;

public class Report {

    @Exclude
    private String rid;
    private String title;
    private String description;
    private String building;
    private String category;
    private User author;

    public Report(){

    }

    public Report(String title, String description, String building, String category, User author) {
        this.title = title;
        this.description = description;
        this.building = building;
        this.category = category;
        this.author = author;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
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

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
