package it.unimib.communimib.model;

import java.util.Objects;

public class Comment {

    private String cid;
    private User commentCreator;
    private String text;
    private long timestamp;

    public Comment(User commentCreator, String text) {
        this.commentCreator = commentCreator;
        this.text = text;
    }

    public User getCommentCreator() {
        return commentCreator;
    }

    public void setCommentCreator(User commentCreator) {
        this.commentCreator = commentCreator;
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
