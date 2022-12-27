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


//  게시판 리스트 보기...
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
            ModelAndView modelAndView ,
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




//    ================================================================================= Read

    @RequestMapping(value = "read" , method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
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
