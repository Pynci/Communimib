package it.unimib.communimib.model;

public class Post {

    String pid;
    String title;
    String description;
    String category;
    User author;

    //todo inserire contatto


    public Post(String title, String description, String category, User author) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.author = author;
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
}
