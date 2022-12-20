package com.example.campmoa.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller(value = "com.example.campmoa.Controllers.BbsController")
@RequestMapping(value = "/qna")
public class BbsController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getIndex(ModelAndView modelAndView) {
        modelAndView.setViewName("bbs/index");
        return modelAndView;
    }

    @RequestMapping(value = "write", method = RequestMethod.GET)
    public ModelAndView getWrite(ModelAndView modelAndView) {
        modelAndView.setViewName("bbs/write");
        return modelAndView;
    }

    @RequestMapping(value = "read", method = RequestMethod.GET)
    public ModelAndView getRead(ModelAndView modelAndView) {
        modelAndView.setViewName("bbs/read");
        return modelAndView;
    }




}
