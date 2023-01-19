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
//        requester == 로그인한 유저. session에 올려놓음.
        if (requester == null) {
            return RequestResult.NOT_SIGNED;
        }
        if (article == null) {
            return RequestResult.NOT_FOUND;
        }

//        로그인한 유저도 존재하고, 넘어온 articleIndex 로 검색후 게시글도 존재한다.
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
//        oldArticle 객체 만들어서 기존 article 정보를 저장시킨다.
        AccArticleEntity oldArticle = this.accMapper.selectArticleByIndex(article.getIndex());
//        기존 게시글 정보가 없다면 실패 처리
        if (oldArticle == null) {
            return CommonResult.FAILURE;
        }
//        현재 게시글 작성자와 기존 게시글 작성자가 다르다면 게시글 수정 실패 처리
        if (!article.getUserEmail().equals(oldArticle.getUserEmail())) {
            return CommonResult.FAILURE;
        }
//        만약 수정 들어왔는데 현재 게시글 이미지가 없다면 기존 게시글것을 사용하도록 설정.
        if (article.getCoverImage() == null) {
            article.setCoverImage(oldArticle.getCoverImage())
                    .setCoverImageMime(oldArticle.getCoverImageMime());
        }

//        위 모든 경우를 통과 했다면 수정 시킬것.
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
