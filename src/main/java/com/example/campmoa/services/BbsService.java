package com.example.campmoa.services;

import com.example.campmoa.entities.bbs.ArticleEntity;
import com.example.campmoa.entities.bbs.ArticleLikeEntity;
import com.example.campmoa.entities.bbs.BoardEntity;
import com.example.campmoa.entities.member.UserEntity;
import com.example.campmoa.enums.CommonResult;
import com.example.campmoa.enums.bbs.ModifyArticleResult;
import com.example.campmoa.enums.bbs.WriteResult;
import com.example.campmoa.interfaces.IResult;
import com.example.campmoa.mappers.IBbsMapper;
import com.example.campmoa.models.PagingModel;
import com.example.campmoa.vos.bbs.ArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.Date;

@Service(value = "com.example.campmoa.services.BbsService")
public class BbsService {
    private final IBbsMapper bbsMapper;

    @Autowired
    public BbsService(IBbsMapper bbsMapper) {
        this.bbsMapper = bbsMapper;
    }


    public BoardEntity[] getBoard() {
//        BoardEntity[] 존재하는 값은 id , text 2개 이다.
//        레코드를 존재하는 래코드 한줄 을 하나의 배열 값으로 읽어 오기 위해서 배열을 썻다.
        return this.bbsMapper.selectBoardIdByText();
    }
//    public BoardEntity getBoard(String id) {
//        BoardEntity boardEntity = this.bbsMapper.selectBoardById(id);
//        return boardEntity;
//    }







    public Enum<? extends IResult> RegisterBoard(
            ArticleEntity article
    ) {
        BoardEntity board = this.bbsMapper.selectBoardById(article.getBoardValue());
        if (board == null) {
            return WriteResult.NO_SUCH_BOARD;
        }
        return this.bbsMapper.insertArticle(article) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }





    //    ================================================= 페이징 처리


    public ArticleVo[] getSearchArticle(String bid,PagingModel paging, String criterion, String keyword) {
        return bbsMapper.searchArticles(
                bid,
                paging.countPerPage,
//                limit  = 10
                (paging.requestPage-1) * paging.countPerPage,
//                2 - 1 = 1 * 10 = 10
                criterion,
                keyword);
//        return this.bbsMapper.selectArticles();
    }

    public ArticleVo[] getArticles() {
        return this.bbsMapper.selectArticles();
    }







//    ==========================================================================
//    상세 보기. read

    public ArticleVo readArticle(int index) {
        ArticleVo existingArticleReadVo = this.bbsMapper.selectArticleByIndex(index);
        if (existingArticleReadVo != null) {
            existingArticleReadVo.setView(existingArticleReadVo.getView() + 1);
            this.bbsMapper.updateArticle(existingArticleReadVo);
        }
        return existingArticleReadVo;
    }








//    =================== read 상세보기 수정 Modify

    public Enum<? extends IResult> prepareModifyArticle(ArticleEntity article, UserEntity user) {
        if (user == null) {
            return ModifyArticleResult.NOT_SIGNED;
            //로그인 하지 않았을 경우
        }
        ArticleEntity existingArticle = this.bbsMapper.selectArticleByIndex(article.getIndex());
        if (existingArticle == null) {
            return ModifyArticleResult.NO_SUCH_ARTICLE;
            // 존재하지 않은 게시물일 경우
        }
        if (!existingArticle.getUserEmail().equals(user.getEmail())) {
            return ModifyArticleResult.NOT_ALLOWED;
            // 해당 게시물을 작성한 유저와 로그인 한 유저가 동일 유저가 아닐 경우.
        }
        // 모든 경우를 제외하면 성공 쿼리를 작성한다.

        // 이걸 사용한 이유는 생성시간과 수정시간을 다르게 표시 하기 위해서 입니다.

        article.setIndex(existingArticle.getIndex());
        article.setUserEmail(existingArticle.getUserEmail());
        article.setBoardValue(existingArticle.getBoardValue());
        article.setTitle(existingArticle.getTitle());
        article.setContent(existingArticle.getContent());
        article.setView(existingArticle.getView());
        article.setCreatedAt(existingArticle.getCreatedAt());

        return CommonResult.SUCCESS;
    }











    public Enum<? extends IResult> modifyArticle(UserEntity user, ArticleEntity article) {
        ArticleEntity exsitingArticle = this.bbsMapper.selectArticleByIndex(article.getIndex());
        if (exsitingArticle == null) {
            return ModifyArticleResult.NO_SUCH_ARTICLE;
        }
        if (user == null || !user.getEmail().equals(exsitingArticle.getUserEmail())) {
            return ModifyArticleResult.NOT_ALLOWED;
        }
        exsitingArticle.setTitle(article.getTitle());
        exsitingArticle.setContent(article.getContent());
        exsitingArticle.setCreatedAt(new Date());

        return this.bbsMapper.updateArticle(exsitingArticle) == 0
                ? CommonResult.FAILURE
                : CommonResult.SUCCESS;
    }






    // ================= 좋아요 기능

    public ArticleLikeEntity[] getLikeArticles(int aid) {
        ArticleLikeEntity[] existingArticleLike = this.bbsMapper.selectArticleLike(aid);
        System.out.println(existingArticleLike.length);
        return existingArticleLike;
    }

    public boolean createArticleLike(int aid, String userEmail) {
        return this.bbsMapper.insertArticleLike(aid, userEmail);
    }

    public boolean cancelArticleLike(int aid, String userEmail) {
        return this.bbsMapper.deleteArticleLike(aid, userEmail);
    }

//    게시글 삭제하기
    public void deleteArticle(int aid) {
       this.bbsMapper.deleteArticleById(aid);
    }




}
