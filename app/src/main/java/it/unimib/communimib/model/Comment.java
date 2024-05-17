package it.unimib.communimib.model;

public class Comment {

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
}
