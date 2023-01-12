package com.example.campmoa.controllers;

import com.example.campmoa.entities.acconpany.AccArticleEntity;
import com.example.campmoa.entities.acconpany.CountryEntity;
import com.example.campmoa.entities.acconpany.ContinentEntity;
import com.example.campmoa.entities.member.UserEntity;
import com.example.campmoa.services.AccompanyService;
import com.example.campmoa.services.MemberService;
import com.example.campmoa.vos.accompany.AccArticleSearchVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.StandardCharsets;

@Controller(value = "com.example.compmao.controllers.AccompanyController")
@RequestMapping(value = "/accompany")
public class AccompanyController {


    private final AccompanyService accompanyService;
    private final MemberService memberService;

    @Autowired
    public AccompanyController(AccompanyService accompanyService, MemberService memberService) {
        this.accompanyService = accompanyService;
        this.memberService = memberService;
    }





    //    기본 웹서버에 보여지기 위한 ModelAndView / GET
    @RequestMapping(value = "/" ,method = RequestMethod.GET)
    public ModelAndView getIndex(ModelAndView modelAndView) {
        modelAndView.setViewName("accompany/index");
        return modelAndView;
    }


    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    json object타입으로 정보전달을 위해 사용하는 produces :: MediaType. ~
    @ResponseBody
    public String postIndex(
            @RequestParam(value = "lastArticleId") int lastArticleId,
//            @요청 데이터 lastArticleId 를 사용하기 위함.
            CountryEntity city
    ) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        JSONObject responseJson = new JSONObject();
        AccArticleSearchVo[] articles = this.accompanyService.searchArticles(city, lastArticleId);
        for (AccArticleSearchVo accArticle : articles) {
            accArticle.setContent(accArticle.getContent()
                    .replaceAll("<[^>]*>", "")
                    .replaceAll("&[^;]*;",""));
//            < 어쩌고 저쩌고 > 형태들을을 뒤에 나온 "" 빈 문자 형태로 다 교환해버린다 //하지만 유니코드나 &lt; &gt; &nbsp; 등은.... 그대로 남는다.
        }
        responseJson.put(AccArticleEntity.ATTRIBUTE_NAME_PLURAL, new JSONArray(om.writeValueAsString(articles)));
        return responseJson.toString();
    }

    @RequestMapping(value = "/", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchIndex() throws JsonProcessingException {
         ObjectMapper mapper = new ObjectMapper();

         JSONArray continentsJson = new JSONArray(mapper.writeValueAsString(this.accompanyService.getContinents()));

         JSONArray citiesJson = new JSONArray(mapper.writeValueAsString(this.accompanyService.getCities()));

         JSONObject responseJson = new JSONObject();
         responseJson.put(ContinentEntity.ATTRIBUTE_NAME_PLURAL, continentsJson);
         responseJson.put(CountryEntity.ATTRIBUTE_NAME_PLURAL, citiesJson);

         return responseJson.toString();
    }




    @RequestMapping(value = "cover-image/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getCoverImage (
            @PathVariable(value = "id") int id) {
        AccArticleEntity article = this.accompanyService.getArticle(id);
        if (article == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String[] mimeArray = article.getCoverImageMime().split("/");
        String mimeType = mimeArray[0];
        String mineSubType = mimeArray[1];
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(article.getCoverImage().length);
        headers.setContentType(new MediaType(mimeType, mineSubType, StandardCharsets.UTF_8));

        return new ResponseEntity<>(article.getCoverImage(), headers, HttpStatus.OK);
    }








    //    동행신청하기 글쓰기 페이지 accWrite / GET
    @RequestMapping(value = "accWrite", method = RequestMethod.GET)
    public ModelAndView getAccWrite(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
            ModelAndView modelAndView
    ) {
        if (user == null) {
            modelAndView.setViewName("redirect:/");
        }
        modelAndView.setViewName("accompany/accWrite");
        return modelAndView;
    }



}
