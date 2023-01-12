package com.example.campmoa.vos.accompany;

import com.example.campmoa.entities.acconpany.AccArticleEntity;

public class AccArticleSearchVo extends AccArticleEntity {
    private String userName;

    public String getUserName() {
        return userName;
    }

    public AccArticleSearchVo setUserName(String userName) {
        this.userName = userName;
        return this;
    }
}
