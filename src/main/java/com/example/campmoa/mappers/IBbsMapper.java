package com.example.campmoa.mappers;

import com.example.campmoa.entities.bbs.ArticleEntity;
import com.example.campmoa.entities.bbs.ArticleLikeEntity;
import com.example.campmoa.entities.bbs.BoardEntity;
import com.example.campmoa.vos.bbs.ArticleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import javax.xml.validation.Validator;
import java.util.List;

@Mapper
public interface IBbsMapper {

    int insertArticle(ArticleEntity article);

    BoardEntity[] selectBoardIdByText();

    BoardEntity selectBoardById(@Param(value = "id") String id);

    ArticleVo[] selectArticles();


    //    ========================================read 상세보기 페이지.....
    ArticleVo selectArticleByIndex(@Param(value = "index") int index);

    int updateArticle(ArticleEntity article);

//    =========================================== 좋아요 구현부.

    ArticleLikeEntity[] selectArticleLike(@Param(value = "index") int index);

    boolean insertArticleLike(@Param(value = "index") int index,
                              @Param(value = "userEmail") String userEmail);

    boolean deleteArticleLike(@Param(value = "index") int index,
                              @Param(value = "userEmail") String userEmail);

//    int selectLikeInfo(@Param("aid") int aid);


    //    ========================================================
    ArticleVo[] searchArticles(
            @Param(value = "boardId") String boardId,
            @Param(value = "limit") int limit,
            @Param(value = "offset") int offset,
            @Param(value = "criterion") String criterion,
            @Param(value = "keyword") String keyword);

//    int selectArticleByCount(@Param(value = "boardId") String boardId,
//                             @Param(value = "criterion") String criterion,
//                             @Param(value = "keyword") String keyword);
}
