package com.example.campmoa.entities.bbs;

import java.util.Date;
import java.util.Objects;

public class ArticleEntity {
    public  static final String ATTRIBUTE_NAME = "bbsArticle";
    public  static final String ATTRIBUTE_NAME_PLURAL = "bbsArticles";

    public static ArticleEntity build() {
        return new ArticleEntity();
    }

    private int index;
    private String boardValue;
    private String userEmail;
    private String title;
    private String content;
    private int view;
    private Date createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleEntity that = (ArticleEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    public ArticleEntity() {
    }

    public ArticleEntity(int index, String boardValue, String userEmail, String title, String content, int view, Date createdAt) {
        this.index = index;
        this.boardValue = boardValue;
        this.userEmail = userEmail;
        this.title = title;
        this.content = content;
        this.view = view;
        this.createdAt = createdAt;
    }

    public int getIndex() {
        return index;
    }

    public ArticleEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getBoardValue() {
        return boardValue;
    }

    public ArticleEntity setBoardValue(String boardValue) {
        this.boardValue = boardValue;
        return this;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public ArticleEntity setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ArticleEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ArticleEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public int getView() {
        return view;
    }

    public ArticleEntity setView(int view) {
        this.view = view;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public ArticleEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
