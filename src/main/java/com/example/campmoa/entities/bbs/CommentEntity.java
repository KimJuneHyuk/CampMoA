package com.example.campmoa.entities.bbs;

import java.util.Date;
import java.util.Objects;

public class CommentEntity {

    public  static final String ATTRIBUTE_NAME = "comment";
    public  static final String ATTRIBUTE_NAME_PLURAL = "comments";

    public static CommentEntity build() {
        return new CommentEntity();
    }
    private int index;
    private int commentIndex;
    private String userEmail;
    private int articleIndex;
    private String content;
    private Date writtenOn;

    public CommentEntity() {
    }

    public CommentEntity(int index, int commentIndex, String userEmail, int articleIndex, String content, Date writtenOn) {
        this.index = index;
        this.commentIndex = commentIndex;
        this.userEmail = userEmail;
        this.articleIndex = articleIndex;
        this.content = content;
        this.writtenOn = writtenOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentEntity that = (CommentEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getCommentIndex() {
        return commentIndex;
    }

    public void setCommentIndex(int commentIndex) {
        this.commentIndex = commentIndex;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getArticleIndex() {
        return articleIndex;
    }

    public void setArticleIndex(int articleIndex) {
        this.articleIndex = articleIndex;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getWrittenOn() {
        return writtenOn;
    }

    public void setWrittenOn(Date writtenOn) {
        this.writtenOn = writtenOn;
    }
}
