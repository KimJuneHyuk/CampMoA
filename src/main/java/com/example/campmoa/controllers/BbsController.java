package com.example.campmoa.controllers;

import com.example.campmoa.entities.bbs.ArticleEntity;
import com.example.campmoa.entities.bbs.ArticleLikeEntity;
import com.example.campmoa.entities.bbs.BoardEntity;
import com.example.campmoa.entities.bbs.CommentEntity;
import com.example.campmoa.entities.member.UserEntity;
import com.example.campmoa.enums.CommonResult;
import com.example.campmoa.enums.bbs.ModifyArticleResult;
import com.example.campmoa.enums.bbs.WriteResult;
import com.example.campmoa.interfaces.IResult;
import com.example.campmoa.mappers.IBbsMapper;
import com.example.campmoa.models.PagingModel;
import com.example.campmoa.services.BbsService;
import com.example.campmoa.vos.bbs.ArticleVo;
import org.apache.catalina.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.SmartView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller(value = "com.example.campmoa.Controllers.BbsController")
@RequestMapping(value = "/{bid}")
public class BbsController {

    private final BbsService bbsService;

    private final IBbsMapper bbsMapper;

    @Autowired
    public BbsController(BbsService bbsService, IBbsMapper bbsMapper) {
        this.bbsService = bbsService;
        this.bbsMapper = bbsMapper;
    }


    //  게시판 리스트 보기...전체 보기!!!
    @RequestMapping(value = "/list",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getIndex(
            ModelAndView modelAndView,
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
            @PathVariable("bid") String bid,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "criterion", required = false) String criterion,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
//        defaultValue 값을 줌으로써 페이지 1으로 찍히고 있는 상황.
        if (user == null) {
            modelAndView.setViewName("redirect:/member/userLogin");
        } else {

            modelAndView.setViewName("bbs/list");
            int totalCount = this.bbsService.getArticles().length;  // 전체 게시글 수
            PagingModel paging = new PagingModel(10, totalCount, page);
            modelAndView.addObject("paging", paging);
            ArticleVo[] articles = this.bbsService.getSearchArticle(bid, paging, criterion, keyword);
            modelAndView.addObject(ArticleEntity.ATTRIBUTE_NAME_PLURAL, articles);

//            System.out.println(paging.totalCount);
//            System.out.println(paging.requestPage);
//            System.out.println(paging.minPage);
//            System.out.println(paging.startPage);
//            System.out.println(paging.maxPage);
//            System.out.println(paging.endPage);
        }
        return modelAndView;
    }


    @RequestMapping(value = "delete", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getDeleteArticle(
            @RequestParam(value = "aid") int aid,
            @PathVariable String bid,
            @RequestParam String page
//            ModelAndView modelAndView
            ) {
        bbsService.deleteArticle(aid);
//        modelAndView.addObject("page", page);
        return "redirect:/"+bid+"/list?page=" + page;
    }


    //============================================================================= 글쓰기 write
    @RequestMapping(value = "write", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getWrite(
            ModelAndView modelAndView,
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
            @RequestParam(value = "bid", required = false) String bid
    ) {
        if (user == null) {
            modelAndView.setViewName("redirect:/member/userLogin");
        } else {
            modelAndView.setViewName("bbs/write");
            BoardEntity board =
                    bid == null ? null : this.bbsMapper.selectBoardById(bid);
            modelAndView.addObject("boardId", board);
            modelAndView.addObject(BoardEntity.ATTRIBUTE_NAME_PLURAL, this.bbsService.getBoard());
        }
        return modelAndView;
    }


    //write ,method 포스트 방식으로 ajax 반환
    @RequestMapping(value = "write", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postWrite(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
            @RequestParam(value = "bid", required = false) String bid,
            ArticleEntity article
    ) {
        Enum<?> result; //= this.bbsService.RegisterBoard(user, bid, article)
        if (user == null) {
            result = WriteResult.NOT_ALLOWED;
        } else if (bid == null) {
            result = WriteResult.NO_SUCH_BOARD;
        }

        result = this.bbsService.RegisterBoard(article);

        JSONObject responseJson = new JSONObject();
        if (result == CommonResult.SUCCESS) {
            responseJson.put("aid", article.getIndex());
        }
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }


//    ===================================== Modify 게시글 수정하기. 화면에 보여야 되는 부분입니다.

    @RequestMapping(value = "modify", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getModify(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
//            세션 으로 유저 정보 확인
            @RequestParam(value = "aid") int articleIndex,
//            aid = articleIndex 입니다. 해당 인덱스의 게시글 검색에 사용
            ModelAndView modelAndView
    ) {
        modelAndView.setViewName("bbs/modify");
        ArticleEntity article = new ArticleEntity();
        article.setIndex(articleIndex);

        Enum<?> result = this.bbsService.prepareModifyArticle(article, user);
        modelAndView.addObject("article", article);
        modelAndView.addObject("result", result.name());

        return modelAndView;
    }


    @RequestMapping(value = "modify", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchModify(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
            @RequestParam(value = "aid") int aid,
            @RequestParam(value = "page") String page,
            ArticleEntity article
    ) {
        article.setIndex(aid);
        Enum<?> result = this.bbsService.modifyArticle(user, article);
        // 위 메서드를 받아오는 메서드의 타입이 Enum 이기 때문에, Enum 타입으로 받아옴

        JSONObject responseJson = new JSONObject();
        // Object 타입으로 결과 값을 key: value 를 담아서 js로 결과를 넘겨준다.
        // 즉            key == result : value == success, Failure, NO_SUCH_ARTICLE...
        responseJson.put("result", result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseJson.put("aid", aid);
            responseJson.put("page", page);
        }
        return responseJson.toString();
    }


//    ================================================================================= Read

    @RequestMapping(value = "read", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getRead(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
            @RequestParam(value = "aid", required = false) int aid,
            ModelAndView modelAndView,
            @RequestParam(value = "page") String page,
            @SessionAttribute(value = "page" ,required = false) PagingModel pagingModel,
            HttpServletRequest request
            ) {
        if (user == null) {
            modelAndView.setViewName("redirect:/member/userLogin");
        }
        ArticleVo article = this.bbsService.readArticle(aid);
        modelAndView.setViewName("bbs/read");
        modelAndView.addObject("article", article);
//        int likeNum = this.bbsMapper.selectLikeInfo(aid);
//        modelAndView.addObject("like", likeNum);

        HttpSession session = request.getSession();

        if (article != null) {
            BoardEntity board = this.bbsMapper.selectBoardById(article.getBoardValue());
//            PagingModel paging = new
            int totalCount = this.bbsService.getArticles().length;  // 전체 게시글 수
            PagingModel paging = new PagingModel(10, totalCount, Integer.parseInt(page));
            modelAndView.addObject("paging", paging);
            modelAndView.addObject("board", board);
//            modelAndView.addObject("liked", article.isArticleLiked());
//            modelAndView.addObject("likeCount", article.getArticleLikedCount());
            session.setAttribute("pagingModel", pagingModel);
        }
        return modelAndView;
    }


    //  좋아요 기능 구현

    @RequestMapping(value = "article-like", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ArticleLikeEntity[] getArticleLike(
            @RequestParam(value = "aid", required = false) int aid
//            @RequestParam(value = "userEmail", required = false) String userEmail
    ) {
        //1. 모든 like테이블을 읽어오기 (현재 aid와 일치하는 게시물만) => list로 (Vo혹은 Entity형식의)
        return this.bbsService.getLikeArticles(aid);
    }


    @RequestMapping(value = "article-like", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void postArticleLike(
            int aid,
            @RequestParam(value = "userEmail", required = false) String userEmail
    ) {
        this.bbsService.createArticleLike(aid, userEmail);
        System.out.println(aid);
        System.out.println(userEmail);
    }

    @RequestMapping(value = "article-like", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean deleteArticleLike(
            int aid,
            @RequestParam(value = "userEmail", required = false) String userEmail
    ) {
        return this.bbsService.cancelArticleLike(aid, userEmail);
    }








//    댓글 달기 ==============================================
    @RequestMapping(value = "/inserComment", method = RequestMethod.GET)
    @ResponseBody
    public String getComment(
            @RequestParam(value = "articleIndex") int articleIndex,
            @RequestParam(value = "userEail") String userEmail,
            @RequestParam(value = "content") String content
    ) {
        CommentEntity comment = new CommentEntity();
        comment.setContent(content);
        comment.setUserEmail(userEmail);
        comment.setArticleIndex(articleIndex);
        int state = this.bbsMapper.commentInsert(comment); // 첫 댓글달기.

        JSONObject responseJson = new JSONObject();

        if (state != 0) {
            responseJson.put("result", "success");
        } else {
            responseJson.put("result", "failure");
        }
        return responseJson.toString();
    }

//    댓글의 답글을 달 경우......
    @RequestMapping(value = "/InsertReplyComment", method = RequestMethod.GET)
    public String getReplyComment (
            CommentEntity comment,
            @RequestParam(value = "articleIndex", required = false) int articleIndex,
            @RequestParam(value = "userEmail", required = false) String userEmail,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "commentIndex") int commentIndex
    ) {
        CommentEntity parent = this.bbsMapper.selectParentComments(commentIndex);
        System.out.println(parent.toString());

        comment.setContent(content);
        comment.setUserEmail(userEmail);
        comment.setCommentGroup(parent.getCommentGroup());
        comment.setCommentLevel(parent.getCommentLevel() + 1);
        comment.setArticleIndex(articleIndex);
        this.bbsMapper.replySequence(parent);
        int state = this.bbsMapper.replayInsert(comment);

        if (state != 0) {
            return "redirect:/qna/read?aid=" + articleIndex;
        } else {
            return "redirect:/qna/read?aid=" + articleIndex;
        }
    }

}
