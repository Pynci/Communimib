package it.unimib.communimib.model;

import java.util.Objects;

public class Comment {

    private String cid;
    private User author;
    private String text;
    private long timestamp;

    public Comment(){

    }

    public Comment(User author, String text, long timestamp) {
        this.author = author;
        this.text = text;
        this.timestamp = timestamp;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(getCid(), comment.getCid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCid());
    }
}
