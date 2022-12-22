package com.example.campmoa.mappers;

import com.example.campmoa.entities.bss.ArticleEntity;
import com.example.campmoa.entities.bss.BoardEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IBbsMapper {

    int insertArticle(ArticleEntity article);

    BoardEntity[] selectBoardIdByText();

    BoardEntity selectBoardById(@Param(value = "id") String id);



}
