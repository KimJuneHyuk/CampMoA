package com.example.campmoa.vos.bbs;

import com.example.campmoa.entities.bbs.ArticleEntity;

public class ArticleVo extends ArticleEntity {
    private String name;
    private boolean articleLiked;

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
