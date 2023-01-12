package com.example.campmoa.entities.bbs;

import java.util.Date;
import java.util.Objects;

public class CommentLikeEntity {

    public  static final String ATTRIBUTE_NAME = "commentLike";
    public  static final String ATTRIBUTE_NAME_PLURAL = "commentsLike";

    public static CommentLikeEntity build() {
        return new CommentLikeEntity();
    }


    private String userEmail;
    private int commentIndex;
    private Date createdOn;

    public CommentLikeEntity() {
    }

    public CommentLikeEntity(String userEmail, int commentIndex, Date createdOn) {
        this.userEmail = userEmail;
        this.commentIndex = commentIndex;
        this.createdOn = createdOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentLikeEntity that = (CommentLikeEntity) o;
        return commentIndex == that.commentIndex && Objects.equals(userEmail, that.userEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEmail, commentIndex);
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getCommentIndex() {
        return commentIndex;
    }

    public void setCommentIndex(int commentIndex) {
        this.commentIndex = commentIndex;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}
