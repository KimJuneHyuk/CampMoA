package com.example.campmoa.controllers;

import com.example.campmoa.entities.acconpany.*;
import com.example.campmoa.entities.member.UserEntity;
import com.example.campmoa.enums.CommonResult;
import com.example.campmoa.interfaces.IResult;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
            RegionEntity region
    ) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        JSONObject responseJson = new JSONObject();
        AccArticleSearchVo[] articles = this.accompanyService.searchArticles(region, lastArticleId);
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

        JSONArray continentsJson = new JSONArray();
        for (ContinentEntity continent : this.accompanyService.getContinents()) {
            JSONObject continentJson = new JSONObject();
            continentJson.put("value", continent.getValue());
            continentJson.put("text", continent.getText());
            continentsJson.put(continentJson);
        }
        JSONArray countriesJson = new JSONArray();
        for (CountryEntity country : this.accompanyService.getCountries()) {
            JSONObject countryJson = new JSONObject();
            countryJson.put("continentValue", country.getContinentValue());
            countryJson.put("value", country.getValue());
            countryJson.put("text", country.getText());
            countriesJson.put(countryJson);
        }
        JSONArray regionsJson = new JSONArray();
        for (RegionEntity region : this.accompanyService.getRegions()) {
            JSONObject regionJson = new JSONObject();
            regionJson.put("continentValue", region.getContinentValue());
            regionJson.put("countryValue", region.getCountryValue());
            regionJson.put("value", region.getValue());
            regionJson.put("text", region.getText());
            regionsJson.put(regionJson);
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(ContinentEntity.ATTRIBUTE_NAME_PLURAL, continentsJson);
        responseJson.put(CountryEntity.ATTRIBUTE_NAME_PLURAL, countriesJson);
        responseJson.put(RegionEntity.ATTRIBUTE_NAME_PLURAL, regionsJson);
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
            modelAndView.setViewName("redirect:/member/userLogin");
        } else {
            modelAndView.setViewName("accompany/accWrite");
        }
        return modelAndView;
    }



    @RequestMapping(value = "write", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postWrite(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME) UserEntity user,
            @RequestParam(value = "coverImageFile") MultipartFile coverImageFile,
            @RequestParam(value = "dateFromStr") String dateFromStr,
            @RequestParam(value = "dateToStr") String dateToStr,
            AccArticleEntity article
    ) throws IOException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        article.setIndex(-1)
                .setUserEmail(user.getEmail())
                .setCreatedAt(new Date())
                .setCoverImage(coverImageFile.getBytes())
                .setCoverImageMime(coverImageFile.getContentType())
                .setDateFrom(dateFormat.parse(dateFromStr))
                .setDateTo(dateFormat.parse(dateToStr));
        IResult result = this.accompanyService.putArticle(article);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseJson.put("id", article.getIndex());
        }
        return responseJson.toString();
    }


    //    다운로드용 맵핑
    @RequestMapping(value = "image/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImage(@PathVariable(value = "id") int id) {
        ImageEntity image = this.accompanyService.getImage(id);
        if (image == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders headers = new HttpHeaders();
        String[] mimeArray = image.getMime().split("/");
        String mimeType = mimeArray[0];
        String mimeSubType = mimeArray[1];
        headers.setContentLength(image.getData().length);
        headers.setContentType(new MediaType(mimeType, mimeSubType, StandardCharsets.UTF_8));
        return new ResponseEntity<>(image.getData(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "image", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postImage(
            @RequestParam(value = "upload") MultipartFile upload,
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME) UserEntity user
    ) throws IOException {
        ImageEntity image = ImageEntity.build()
                .setUserEmail(user.getEmail())
                .setCreatedAt(new Date())
                .setName(upload.getOriginalFilename())
                .setMime(upload.getContentType())
                .setData(upload.getBytes());
        IResult result = this.accompanyService.uploadImage(image);
        JSONObject responseJson = new JSONObject();

        if (result == CommonResult.SUCCESS) {
            responseJson.put("url", String.format("http://localhost:8080/accompany/image/%d", image.getIndex()));
        } else {
            JSONObject errorJson = new JSONObject();
            errorJson.put("message", "이미지 업로드에 실패하였습니다. 잠시 후 다시 시도해 주세요.");
            responseJson.put("error", errorJson);
        }
        return responseJson.toString();
    }



}
