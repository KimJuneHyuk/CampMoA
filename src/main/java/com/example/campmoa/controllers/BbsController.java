package com.example.campmoa.controllers;

import com.example.campmoa.entities.bbs.ArticleEntity;
import com.example.campmoa.entities.bbs.BoardEntity;
import com.example.campmoa.entities.member.UserEntity;
import com.example.campmoa.enums.CommonResult;
import com.example.campmoa.enums.bbs.WriteResult;
import com.example.campmoa.interfaces.IResult;
import com.example.campmoa.mappers.IBbsMapper;
import com.example.campmoa.services.BbsService;
import com.example.campmoa.vos.bbs.ArticleVo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller(value = "com.example.campmoa.Controllers.BbsController")
@RequestMapping(value = "/qna")
public class BbsController {

    private final BbsService bbsService;

    private final IBbsMapper bbsMapper;

    @Autowired
    public BbsController(BbsService bbsService, IBbsMapper bbsMapper) {
        this.bbsService = bbsService;
        this.bbsMapper = bbsMapper;
    }


    //  게시판 리스트 보기...전체 보기!!!
    @RequestMapping(value = "list",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getIndex(
            ModelAndView modelAndView,
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user
    ) {
        if (user == null) {
            modelAndView.setViewName("redirect:/member/userLogin");
        } else {
            modelAndView.setViewName("bbs/list");
            ArticleVo[] articles = this.bbsService.getArticles();
            modelAndView.addObject(ArticleEntity.ATTRIBUTE_NAME_PLURAL, articles);
        }
        return modelAndView;
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
            ArticleEntity article
            ) {
        article.setIndex(aid);
        Enum<?> result = this.bbsService.modifyArticle(user, article);
        // 위 메서드를 받아오는 메서드의 타입이 Enum 이기 때문에, Enum 타입으로 받아옴

        JSONObject responseJson = new JSONObject();
        // Object 타입으로 결과 값을 key: value 를 담아서 js로 결과를 넘겨준다.
        // 즉            key == result : value == success, Failure, NO_SUCH_ARTICLE...등
        responseJson.put("result", result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseJson.put("aid", aid);
        }
        return responseJson.toString();
    }


//    ================================================================================= Read

    @RequestMapping(value = "read", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getRead(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
            @RequestParam(value = "aid", required = false) int aid,
            ModelAndView modelAndView
    ) {
        if (user == null) {
            modelAndView.setViewName("redirect:/member/userLogin");
        }
        ArticleVo article = this.bbsService.readArticle(aid);
        modelAndView.setViewName("bbs/read");
        modelAndView.addObject("article", article);

        if (article != null) {
            BoardEntity board = this.bbsMapper.selectBoardById(article.getBoardValue());
            modelAndView.addObject("board", board);
        }

        return modelAndView;
    }


//    @RequestMapping(value = "read", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
//    public ModelAndView getRead(ModelAndView modelAndView) {
//        modelAndView.setViewName("bbs/read");
//        return modelAndView;
//    }


}
