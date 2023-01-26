package com.example.campmoa.entities.bbs;

import java.util.Date;
import java.util.Objects;

public class CommentEntity {

    public static final String ATTRIBUTE_NAME = "comment";
    public static final String ATTRIBUTE_NAME_PLURAL = "comments";

    public static CommentEntity build() {
        return new CommentEntity();
    }

    private int commentIndex;
    private int commentGroup;
    private int commentSequence;
    private int commentLevel;
    private String userEmail;
    private int articleIndex;
    private String Content;
    private Date writtenOn;

    public CommentEntity() {
    }

    public CommentEntity(int commentIndex, int commentGroup, int commentSequence, int commentLevel, String userEmail, int articleIndex, String content, Date writtenOn) {
        this.commentIndex = commentIndex;
        this.commentGroup = commentGroup;
        this.commentSequence = commentSequence;
        this.commentLevel = commentLevel;
        this.userEmail = userEmail;
        this.articleIndex = articleIndex;
        Content = content;
        this.writtenOn = writtenOn;
    }

    public int getCommentIndex() {
        return commentIndex;
    }

    public CommentEntity setCommentIndex(int commentIndex) {
        this.commentIndex = commentIndex;
        return this;
    }

    public int getCommentGroup() {
        return commentGroup;
    }

    public CommentEntity setCommentGroup(int commentGroup) {
        this.commentGroup = commentGroup;
        return this;
    }

    public int getCommentSequence() {
        return commentSequence;
    }

    public CommentEntity setCommentSequence(int commentSequence) {
        this.commentSequence = commentSequence;
        return this;
    }

    public int getCommentLevel() {
        return commentLevel;
    }

    public CommentEntity setCommentLevel(int commentLevel) {
        this.commentLevel = commentLevel;
        return this;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public CommentEntity setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public int getArticleIndex() {
        return articleIndex;
    }

    public CommentEntity setArticleIndex(int articleIndex) {
        this.articleIndex = articleIndex;
        return this;
    }

    public String getContent() {
        return Content;
    }

    public CommentEntity setContent(String content) {
        Content = content;
        return this;
    }

    public Date getWrittenOn() {
        return writtenOn;
    }

    public CommentEntity setWrittenOn(Date writtenOn) {
        this.writtenOn = writtenOn;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentEntity that = (CommentEntity) o;
        return commentIndex == that.commentIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentIndex);
    }


    @Override
    public String toString() {
        return "CommentEntity{" +
                "commentIndex=" + commentIndex +
                ", commentGroup=" + commentGroup +
                ", commentSequence=" + commentSequence +
                ", commentLevel=" + commentLevel +
                ", userEmail='" + userEmail + '\'' +
                ", articleIndex=" + articleIndex +
                ", Content='" + Content + '\'' +
                ", writtenOn=" + writtenOn +
                '}';
    }
}
