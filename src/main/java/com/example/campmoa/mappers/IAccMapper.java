package com.example.campmoa.mappers;

import com.example.campmoa.entities.acconpany.AccArticleEntity;
import com.example.campmoa.entities.acconpany.CountryEntity;
import com.example.campmoa.entities.acconpany.ContinentEntity;
import com.example.campmoa.vos.accompany.AccArticleSearchVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IAccMapper {
    AccArticleSearchVo[] selectArticlesForSearch(
            @Param(value = "city") CountryEntity city,
            @Param(value = "lastArticleIndex") int lastArticleIndex);

    ContinentEntity[] selectContinents();

    CountryEntity[] selectCities();

    AccArticleEntity selectArticleByIndex(@Param(value = "index") int index);

}
