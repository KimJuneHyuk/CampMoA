package com.example.campmoa.entities.bbs;

import java.util.Date;
import java.util.Objects;

public class ArticleLikeEntity {

    private boolean isLike;
    private String userEmail;
    private int articleIndex;
    private Date createdAt;

    public ArticleLikeEntity() {
    }

    public ArticleLikeEntity(boolean isLike, String userEmail, int articleIndex, Date createdAt) {
        this.isLike = isLike;
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

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
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
