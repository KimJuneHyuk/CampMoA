package com.example.campmoa.services;

import com.example.campmoa.entities.acconpany.AccArticleEntity;
import com.example.campmoa.entities.acconpany.CountryEntity;
import com.example.campmoa.entities.acconpany.ContinentEntity;
import com.example.campmoa.mappers.IAccMapper;
import com.example.campmoa.mappers.IMemberMapper;
import com.example.campmoa.vos.accompany.AccArticleSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "com.example.campmoa.services.AccompanyService")
public class AccompanyService {

    private final IAccMapper accMapper;
    private final IMemberMapper memberMapper;
    @Autowired
    public AccompanyService(IAccMapper accMapper, IMemberMapper memberMapper) {
        this.accMapper = accMapper;
        this.memberMapper = memberMapper;
    }

    public ContinentEntity[] getContinents() {
        return this.accMapper.selectContinents();
    }
    public CountryEntity[] getCities() {
        return this.accMapper.selectCities();
    }

    public AccArticleEntity getArticle(int index) {
        return this.accMapper.selectArticleByIndex(index);
    }



    public AccArticleSearchVo[] searchArticles(CountryEntity city, int lastArticleIndex) {
        return this.accMapper.selectArticlesForSearch(city, lastArticleIndex);
    }


}
