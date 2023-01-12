package com.example.campmoa.services;

import com.example.campmoa.entities.acconpany.*;
import com.example.campmoa.enums.CommonResult;
import com.example.campmoa.interfaces.IResult;
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
    public CountryEntity[] getCountries() {
        return this.accMapper.selectCountries();
    }

    public RegionEntity[] getRegions() {
        return this.accMapper.selectRegions();
    }

    public ImageEntity getImage(int index) {
        return this.accMapper.selectImageByIndex(index);
    }

    public IResult uploadImage(ImageEntity image) {
        return this.accMapper.insertImage(image) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public AccArticleEntity getArticle(int index) {
        return this.accMapper.selectArticleByIndex(index);
    }



    public AccArticleSearchVo[] searchArticles(RegionEntity region, int lastArticleIndex) {
        return this.accMapper.selectArticlesForSearch(region, lastArticleIndex);
    }

    public IResult putArticle(AccArticleEntity article) {
        return this.accMapper.insertArticle(article) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }


}
