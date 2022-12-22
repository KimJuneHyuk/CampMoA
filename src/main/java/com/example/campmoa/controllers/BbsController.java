package com.example.campmoa.controllers;

import com.example.campmoa.entities.bss.BoardEntity;
import com.example.campmoa.entities.member.UserEntity;
import com.example.campmoa.services.BbsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

@Controller(value = "com.example.campmoa.Controllers.BbsController")
@RequestMapping(value = "/qna")
public class BbsController {

    private final BbsService bbsService;

    @Autowired
    public BbsController(BbsService bbsService) {
        this.bbsService = bbsService;
    }


    @RequestMapping(value = "/",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getIndex(ModelAndView modelAndView) {
        modelAndView.setViewName("bbs/index");
        return modelAndView;
    }
//============================================================================= 글쓰기 write
    @RequestMapping(value = "write", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getWrite(
            ModelAndView modelAndView ,
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user
    ) {
        if (user == null) {
            modelAndView.setViewName("redirect:/member/userLogin");
        } else {
            modelAndView.setViewName("bbs/write");
            modelAndView.addObject(BoardEntity.ATTRIBUTE_NAME_PLURAL, this.bbsService.getBoard());
        }
        return modelAndView;
    }


//    ====================================================================================

    @RequestMapping(value = "read", method = RequestMethod.GET)
    public ModelAndView getRead(ModelAndView modelAndView) {
        modelAndView.setViewName("bbs/read");
        return modelAndView;
    }




}
