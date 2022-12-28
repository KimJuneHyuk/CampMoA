package com.example.campmoa.entities.bbs;

import java.util.Date;
import java.util.Objects;

public class ArticleLikeEntity {

    public  static final String ATTRIBUTE_NAME = "articleLike";
    public  static final String ATTRIBUTE_NAME_PLURAL = "articleLikes";

    public static ArticleLikeEntity build() {
        return new ArticleLikeEntity();
    }

    private String userEmail;
    private int articleIndex;
    private Date createdAt;

    public ArticleLikeEntity() {
    }

    public ArticleLikeEntity(String userEmail, int articleIndex, Date createdAt) {
        this.userEmail = userEmail;
        this.articleIndex = articleIndex;
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleLikeEntity that = (ArticleLikeEntity) o;
        return articleIndex == that.articleIndex && Objects.equals(userEmail, that.userEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEmail, articleIndex);
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
