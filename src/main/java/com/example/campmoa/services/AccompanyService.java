package com.example.campmoa.services;

import com.example.campmoa.entities.acconpany.*;
import com.example.campmoa.entities.member.UserEntity;
import com.example.campmoa.enums.CommonResult;
import com.example.campmoa.enums.RequestResult;
import com.example.campmoa.interfaces.IResult;
import com.example.campmoa.mappers.IAccMapper;
import com.example.campmoa.mappers.IMemberMapper;
import com.example.campmoa.vos.accompany.AccArticleSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.Date;

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


    public IResult deleteArticle(int id) {
        return this.accMapper.deleteArticle(id) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public IResult putRequest(UserEntity requester, int articleIndex) {
        AccArticleEntity article = this.accMapper.selectArticleByIndex(articleIndex);
//        requester == ???????????? ??????. session??? ????????????.
        if (requester == null) {
            return RequestResult.NOT_SIGNED;
        }
        if (article == null) {
            return RequestResult.NOT_FOUND;
        }

//        ???????????? ????????? ????????????, ????????? articleIndex ??? ????????? ???????????? ????????????.
        UserEntity respondent = this.memberMapper.selectUserByEmail(UserEntity.build().setEmail(article.getUserEmail()));
        if (requester.equals(respondent)) {
            return RequestResult.YOURSELF;
        }

        RequestEntity request = RequestEntity.build()
                .setRequesterUserEmail(requester.getEmail())
                .setRequesteeUserEmail(respondent.getEmail())
                .setArticleIndex(articleIndex)
                .setCreatedAt(new Date())
                .setGranted(false)
                .setDeclined(false);

        return this.accMapper.insertRequest(request) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public IResult modifyArticle(AccArticleEntity article) {
//        oldArticle ?????? ???????????? ?????? article ????????? ???????????????.
        AccArticleEntity oldArticle = this.accMapper.selectArticleByIndex(article.getIndex());
//        ?????? ????????? ????????? ????????? ?????? ??????
        if (oldArticle == null) {
            return CommonResult.FAILURE;
        }
//        ?????? ????????? ???????????? ?????? ????????? ???????????? ???????????? ????????? ?????? ?????? ??????
        if (!article.getUserEmail().equals(oldArticle.getUserEmail())) {
            return CommonResult.FAILURE;
        }
//        ?????? ?????? ??????????????? ?????? ????????? ???????????? ????????? ?????? ??????????????? ??????????????? ??????.
        if (article.getCoverImage() == null) {
            article.setCoverImage(oldArticle.getCoverImage())
                    .setCoverImageMime(oldArticle.getCoverImageMime());
        }

//        ??? ?????? ????????? ?????? ????????? ?????? ?????????.
        article.setIndex(oldArticle.getIndex())
                .setUserEmail(oldArticle.getUserEmail())
                .setCreatedAt(oldArticle.getCreatedAt());

        return accMapper.updateArticle(article) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }


    public boolean checkRequest(UserEntity requester, int articleIndex) {
        if (requester == null) {
            return false;
        }
        return this.accMapper
                .selectRequestByRequesterArticleIndex(requester.getEmail(), articleIndex) != null;
    }
}
