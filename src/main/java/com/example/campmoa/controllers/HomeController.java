package com.example.campmoa.controllers;

import com.example.campmoa.entities.acconpany.AccArticleEntity;
import com.example.campmoa.mappers.IAccMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller(value = "com.example.campmoa.Controller.HomeController")
// SpringBoot의 명시적 Controller임을 선언하여 주입시켜준다.
@RequestMapping(value = "/")
//class 의 RequestMapping = 전역 주소 , Method 의 RequestMapping = 전역주소 / 지역주소를 의미한다.
public class HomeController {

    private final IAccMapper accMapper;

    @Autowired
    public HomeController(IAccMapper accMapper) {
        this.accMapper = accMapper;
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getIndex(
            ModelAndView modelAndView
            ) {
        AccArticleEntity[] articles = accMapper.mainAccompanyPic();
        List<AccArticleEntity> list = new ArrayList<>();

        for (AccArticleEntity article : articles) {
            article.setContent(article.getContent()
                    .replaceAll("<[^>]*>","")
                    .replaceAll("<[^>]*>",""));
            list.add(article);
        }
        modelAndView.addObject("articles", list);
        modelAndView.setViewName("home/index");
        return modelAndView;
    }

//    @controller(value = "com.example.campmoa.Controller.HomeController")
//    컨트롤러 임을 선언하고 경로까지 선언 하므로써 중복의 이름을 선언에 대한 프로젝트 import 방지로하기 위해 value ="모든 경로"를 지정함.

//    RequestMapping(value ="/") :: 클래스 위에 requestMapping를 통해 Servlet으로 부터 웹 해당 주소를 설정할 수 있다.

//    public class HomeController {
//    클래스 생성

//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    메서드의 Mapping 를 통해 전체 전역 주소와 지역 주소를 같게 함으로써 mian 페이지를 지정해 주었다.

//    public ModelAndView getIndex(ModelAndView) {
//    메서드를 생성하고 반환 값은 ModelAndView 로 한다. ModelAndView 를 통해 server의 결과값을 mav.addObject를 통해 전달 할 수도 있다.
//    mav.setView()을 통해 내가 지정한 HTML를 웹페이지로 보여질 수 있게 설정 가능하다.
//    return modelandView를 통해 해당 메서드의 반환 값을 전달할 수 있다.


}
