package com.example.campmoa.Controllers;

import com.example.campmoa.entities.ContactAuthEntity;
import com.example.campmoa.entities.ContactCountryEntity;
import com.example.campmoa.entities.UserEntity;
import com.example.campmoa.enums.CommonResult;
import com.example.campmoa.exceptions.RollbackException;
import com.example.campmoa.interfaces.IResult;
import com.example.campmoa.services.MemberService;
import org.apache.catalina.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Controller(value = "com.example.campmoa.Controllers.MemberController")
@RequestMapping(value = "/member")
public class MemberController {


    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    //    SNS인증 GET
    @RequestMapping(value = "userRegisterAuth", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getUserRegisterAuth(ContactAuthEntity contactAuth) throws
            InvalidKeyException,
            IOException,
            NoSuchAlgorithmException {
        contactAuth.setIndex(-1)
                .setCode(null)
                .setSalt(null)
                .setCreatedAt(null)
                .setExpiresAt(null)
                .setExpired(false);
        IResult result;
        try {
            result = this.memberService.registerAuth(contactAuth);
        } catch (RollbackException ex) {
            result = ex.result;
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseJson.put("salt", contactAuth.getSalt());
        }
        return responseJson.toString();
    }

    //    SNS인증 POST
    @RequestMapping(value = "userRegisterAuth", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String postUserRegisterAuth(ContactAuthEntity contactAuth) {
        contactAuth.setIndex(-1)
                .setCreatedAt(null)
                .setExpiresAt(null)
                .setExpired(false);
        IResult result;
        try {
            result = this.memberService.checkContactAuth(contactAuth);
        } catch (RollbackException ex) {
            result = ex.result;
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }


//    ==================================================================================================


    //    회원가입 GET 000
    @RequestMapping(value = "userRegister", method = RequestMethod.GET)
    public ModelAndView getUserRegister(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                        ModelAndView modelAndView) {
        if (user != null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        modelAndView.addObject(ContactCountryEntity.ATTRIBUTE_NAME_PLURAL, this.memberService.getContactCountries());
        modelAndView.setViewName("member/userRegister");
        return modelAndView;
    }

    @RequestMapping(value = "userRegister", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String userRegister(ContactAuthEntity contactAuth,
                               UserEntity user) {
        contactAuth.setIndex(-1)
                .setCreatedAt(null)
                .setExpiresAt(null)
                .setExpired(false);
        user.setStatusValue("OKY")
                .setRegisterAt(new Date());
        IResult result;
        try {
            result = this.memberService.createUser(contactAuth, user);
        } catch (RollbackException ex) {
            result = ex.result;
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }


//    ============================================ 회원 가입 성공 했을경우 보여주는 페이지 ========================================

    @RequestMapping(value = "userRegisterDone", method = RequestMethod.GET)
    public ModelAndView getUserRegisterDone(ModelAndView modelAndView) {
        modelAndView.setViewName("member/userRegisterDone");
        return modelAndView;
    }
//    ===============================================================================================

    @RequestMapping(value = "myPage", method = RequestMethod.GET)
    public ModelAndView getMyPage (ModelAndView modelAndView) {
        modelAndView.setViewName("member/myPage");
        return modelAndView;
    }


//===================================================================================================


    //    로그인 상태를 화면에 보여지게 하는 메서드 // GET
    @RequestMapping(value = "userLogin", method = RequestMethod.GET)
    public ModelAndView getUserLogin(
            ModelAndView modelAndView,
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user
    ) {
//        이미 로그인을 했거나 , 로그인이 되어있다면 메인페이지로 전달.
        if (user != null) {
            modelAndView.setViewName("redirect:/");
        }
        modelAndView.setViewName("member/userLogin");
        return modelAndView;
    }

    @RequestMapping(value = "userLogin", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String postUserLogin(
            HttpSession session,
            UserEntity user
    ) {
        user.setName(null)
                .setContactCountryValue(null)
                .setContact(null)
                .setStatusValue(null)
                .setRegisterAt(null);
        IResult result = this.memberService.loginUser(user);
        if (result == CommonResult.SUCCESS) {
            session.setAttribute(UserEntity.ATTRIBUTE_NAME, user);
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

//  ==================================================로그 아웃

    @RequestMapping(value = "userLogout", method = RequestMethod.GET)
    public ModelAndView getUserLogout(ModelAndView modelAndView,
                                      HttpSession session) {
        session.removeAttribute(UserEntity.ATTRIBUTE_NAME);
        modelAndView.setViewName("member/userLogin");
        return modelAndView;




    }

}
