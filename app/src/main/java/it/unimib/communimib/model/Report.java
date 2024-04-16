package it.unimib.communimib.model;

public class Report {

    private String title;
    private String description;
    private String building;

    private String category;

    private String author;
    public Report(String title, String description, String building, String category, String author) {
        this.title = title;
        this.description = description;
        this.building = building;
        this.category = category;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
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
