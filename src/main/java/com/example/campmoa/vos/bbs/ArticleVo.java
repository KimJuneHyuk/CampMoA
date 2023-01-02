package com.example.campmoa.vos.bbs;

import com.example.campmoa.entities.bbs.ArticleEntity;

public class ArticleVo extends ArticleEntity {
    private String name;
    private boolean articleLiked;
    private int articleLikedCount;






    public int getArticleLikedCount() {
        return articleLikedCount;
    }

    public ArticleVo setArticleLikedCount(int articleLikedCount) {
        this.articleLikedCount = articleLikedCount;
        return this;
    }

    public boolean isArticleLiked() {
        return articleLiked;
    }

    public ArticleVo setArticleLiked(boolean articleLiked) {
        this.articleLiked = articleLiked;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
