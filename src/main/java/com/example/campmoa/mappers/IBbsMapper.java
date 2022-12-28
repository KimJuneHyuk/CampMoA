package com.example.campmoa.mappers;

import com.example.campmoa.entities.bbs.ArticleEntity;
import com.example.campmoa.entities.bbs.ArticleLikeEntity;
import com.example.campmoa.entities.bbs.BoardEntity;
import com.example.campmoa.vos.bbs.ArticleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IBbsMapper {

    int insertArticle(ArticleEntity article);

//    int insertArticleLike(ArticleLikeEntity articleLike);

    BoardEntity[] selectBoardIdByText();

    BoardEntity selectBoardById(@Param(value = "id") String id);

    ArticleVo[] selectArticles();

    //    ========================================read 상세보기 페이지.....
    ArticleVo selectArticleByIndex(@Param(value = "index") int index);
//    {
//        return this.selectArticleByIndex(index);
//    }
//    오버로딩....
//    ArticleVo selectArticleByIndex (@Param(value = "index") int index,
//                                    @Param(value = "email") String email);


    int updateArticle(ArticleEntity article);


}
