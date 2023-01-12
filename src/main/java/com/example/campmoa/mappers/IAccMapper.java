package com.example.campmoa.mappers;

import com.example.campmoa.entities.acconpany.*;
import com.example.campmoa.vos.accompany.AccArticleSearchVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IAccMapper {
//    글 쓰기 삽입 선언부
    int insertArticle(AccArticleEntity article);

    int insertImage(ImageEntity image);










    AccArticleSearchVo[] selectArticlesForSearch(
            @Param(value = "region") RegionEntity region,
            @Param(value = "lastArticleIndex") int lastArticleIndex);

    ContinentEntity[] selectContinents();

    CountryEntity[] selectCountries();

    RegionEntity[] selectRegions();

    ImageEntity selectImageByIndex(@Param(value = "index") int index);

    AccArticleEntity selectArticleByIndex(@Param(value = "index") int index);

}
