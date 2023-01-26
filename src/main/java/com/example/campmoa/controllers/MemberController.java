package com.example.campmoa.controllers;

import com.example.campmoa.entities.member.ContactAuthEntity;
import com.example.campmoa.entities.member.ContactCountryEntity;
import com.example.campmoa.entities.member.UserEntity;
import com.example.campmoa.enums.CommonResult;
import com.example.campmoa.exceptions.RollbackException;
import com.example.campmoa.interfaces.IResult;
import com.example.campmoa.services.MemberService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Controller(value = "com.example.campmoa.Controllers.MemberController")
@RequestMapping(value = "/member")
public class MemberController {

    private final MemberService memberService;
//   private final MeberService 선언을 통해 해당 메서드에서만 접근가능하고 더이상의 상속을 받지 못하게 final 처리를 하였다.

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
//    @Autowired 를 통해 springboot에게 생성자를통하여 spring이 인식하는 한 객체생성을 할수 있도록 맡기는 어노테이션을 사용.





    //    SNS인증 GET =================================================================================
//    전역 mapping주소인 member/userRegisterAuth 로 주소위치를 설정하였다.
    @RequestMapping(value = "userRegisterAuth", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
//    ResponseBody를 통해 요청에 대한 결과를 자바의 결과값으로 Json 방식으로 전달하기 위한 어노테이션을 사용하였다.
    public String getUserRegisterAuth(ContactAuthEntity contactAuth) throws
            InvalidKeyException,
            IOException,
            NoSuchAlgorithmException {
//        String 반환타입의 메서드 getUserRegisterAuth(유저회원가입인증) (매개변수로ContactAuthEntity 사용)
        contactAuth.setIndex(-1)
                .setCode(null)
                .setSalt(null)
                .setCreatedAt(null)
                .setExpiresAt(null)
                .setExpired(false);
//        input으로 들어오는 값을 제외한 나머지값을 초기화 설정을하여 매개변수에 담아 전달하였다.

        IResult result;
//        IResult 인터페이스를 통해 수많은 Enum의 상수들의 결과를 구현토록하여 결과를 Json으로 전달하도록 하였다.
        try {
            result = this.memberService.registerAuth(contactAuth);
//            result = Autowired 어노테이션을 통한 service의 메서드 registerAuth 이용하여 결과 판단을 하도록하였다.
        } catch (RollbackException ex) {
            result = ex.result;
//            넘어오는 결과값으 오류일 경우, 직접 만들 Exception(예외)를 통하여 결과를 Enum의 FailUre를 반환토록 설정.
        }
        JSONObject responseJson = new JSONObject();
//        JSON 을 생성하여 결과(key) : 값(value)를 쌍으로 js로 전달하여 ajax 비동기 연동방식으로 처리하기위한 문자열 값을 생성하여 전달하도록 하였다.
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
//        IResult.Attribute_Name 상수를 사용함으로써 오타율을 줄였다("result") ,전달되는 영문의 결과는 모두 소문자로 표현하여 값비교를 원만히 하도록 설정.
        if (result == CommonResult.SUCCESS) {
//          넘어온 결과값이 success인 경우
            responseJson.put("salt", contactAuth.getSalt());
//            경과값으로 salt(난수)를 설정하여 salt를 Html hidden에 생성하여 넘어오는 값에 대한 비교를 하기위해 Ajax처리를 하였다.
        }
        return responseJson.toString();
    }


    //    SNS인증 POST  ==================================================================
    @RequestMapping(value = "userRegisterAuth", method = RequestMethod.POST, produces = "application/json")
//    RequestMapping를 통해, member/userRegisterAuth 주소로 들어오는 정보를 읽어들인다. 전달방식 post를 통하여 다른유저로 부터 넘어오는 정보를 보여지지 않는다.
    @ResponseBody
//    ResponseBody 어노테이션을통하여 해당 메서드의 결과값을 JSON 형식의 문자열 객체로 전달 하였다.
    public String postUserRegisterAuth(ContactAuthEntity contactAuth) {
        contactAuth.setIndex(-1)
                .setCreatedAt(null)
                .setExpiresAt(null)
                .setExpired(false);
        IResult result;
//        메서드 반환타입 String 인 이유는 Json 타입의 전달 방식이 key:value 쌍의 문자열 로 전달되기 떄문이다.
//       매개변수 ContactAuthEntity를 통하여 넘어오는 결과값을 제외한 나머지 값들을 초기화 처리를 하였다.

        try {
            result = this.memberService.checkContactAuth(contactAuth);
//        result = 인증번호를 5분 유효 시간 안에 인증번호 6자리를 인증 했을경우에 대한 결과 값.failure, expired, success,
        } catch (RollbackException ex) {
            result = ex.result;
//         예외가 발생할 경우, 예외처리 failure 처리하겠다.
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
//        모든 결과를 js 처리 ajax 처리 하기위해 result : success, 등의 결과로 반환.
        return responseJson.toString();
    }







    //    ==================================================================     회원가입 GET / POST
    @RequestMapping(value = "userRegister", method = RequestMethod.GET)
    public ModelAndView getUserRegister(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
            ModelAndView modelAndView) {
//        @SessionAttribute 의 정보를 session에 올려두고 존재 여부를 판단 하기 위해 사용.
        if (user != null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
//         session 에 유저가 존재한다면, 회원가입 버튼을 눌렀을 경우 메인페이지로 돌아가게 설정.
        }

        modelAndView.addObject(ContactCountryEntity.ATTRIBUTE_NAME_PLURAL, this.memberService.getContactCountries());
//        modelAndView 객체를 통해 addObject를 통하여 DB에 저장된 결과를 불러와 select > option 의 값으로 설정.
        modelAndView.setViewName("member/userRegister");
//        그리고 해당 값들을 member/userRegister.html Thymeleaf 를 사용하여 값을 뿌려준다.
        return modelAndView;
    }

//      ================================================

    @RequestMapping(value = "userRegister", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
//    회원가입 버튼 클릭시 넘어오는 client의 정보를 가지고 결과 값의 여부를 ajax 처리를 하기 위해 @responseBody 를 사용한다.
    public String userRegister(
            ContactAuthEntity contactAuth,
            UserEntity user
    ) {
        contactAuth.setIndex(-1)
                .setCreatedAt(null)
                .setExpiresAt(null)
                .setExpired(false);
//        매개변수로 들어온 연락처 인증과 , user input 값으로 넘어오는 값을 제외한 나머지 값들에 대한 초기화 작업.
        user.setStatusValue("OKY")
                .setRegisterAt(new Date());

        IResult result;
//        IResult 을 구현한 Enum 의 변수이름을 result 로 설정하고, 해당 result 의 결과 반환값으로 service 에서 메서드 결과값을 활용한다.
        try {
            result = this.memberService.createUser(contactAuth, user);
//            매개변수로 넘어간 값들의 결과가 없거나, 정규식 이 맞지 않을경우 실패, 인증번호가 생성되지 않았거나, 이미 인증만료시간을 지났으면 실패,
//            모든 결과를 통과하였는데 DB 의 insert 결과가 0 이라면 예외 처리 실패, 그게 아니라면 success 의 로 JSONOBJECT 로 담아 result : success등의 결과를 반환.
        } catch (RollbackException ex) {
            result = ex.result;
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }







//    ============================================ 회원 가입 성공 했을경우 보여주는 페이지

//    @RequestMapping(value = "userRegisterDone", method = RequestMethod.GET)
    @GetMapping(value = "userRegisterDone")
    public ModelAndView getUserRegisterDone(ModelAndView modelAndView) {
//        접근제한제 public 반환 타입 ModelAndView 메서드명 getUserRegisterDone ( 매개변수 : ModelAndView modelAndView)
        modelAndView.setViewName("member/userRegisterDone");
//        매개변수의.내장메서드 setViewName(클라이언트에게 보여줄 페이지를 설정할수 있음 "member/RegisterDone.html");
        return modelAndView;

    }







//    ================================================= 개인정보 수정으로 인한 비빌번호 변경.

//    @RequestMapping(value = "myPage", method = RequestMethod.GET)
    @GetMapping(value = "myPage")
    public ModelAndView getMyPage(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user
            , ModelAndView modelAndView
    ) {
        if (user == null) {
            modelAndView.setViewName("member/userLogin");
            return modelAndView;
//            Session이 존재하지 않을 경우 로그인 화면으로 보내 로그인을 하도록 유도..
//            modelANdView.내장 메서드인 setViewName(를 활용하여 "member/userLogin.html"로 보여준다.
        }

        modelAndView.setViewName("member/myPage");
        return modelAndView;
    }

//  myPage 에서 현재 비밀번호를 입력하고 확인버튼을 눌렀을경우 현재 비밀번호가 맞는지 확인하기 위해 server의 저장된 값을 확인하고
//  그에 관련 ajax 처리를 하는 controller 메서드 ================================================================

    @RequestMapping(value = "userPasswordCheck", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
//    @RequestMapping(value="userPasswordCheck"의 값을 설정해 줌으로써 해당 메서드 실행 주소는 member/userPasswordCheck 주소로 들어갔을떄 실행이 되고 , 전달방식은 post 로써 ajax 처리를 하기위해 @ResponseBody 어노테이션을 활용하여 JSONObject 객체로 javaScript로 전달하여 결과처리를 한다.
    public String postRecoverPassword(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "password", required = false) String password
//            @RequestParam(value="html에 설정된 name= "값"을 사용하여 결과를 받아올 수 있다.
    ) {
        IResult result = this.memberService.checkUserEmailByPassword(email, password);
//        result 의 결과로써 memberService.checkUserEmailByPassword(메서드를 활용하여 넘어온 값에 해당된 email, password를 기준으로 검사를 한다.)
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
//        해당 결과에 따른 JSONObejct 의 결과 값으로 key : value 쌍으로 Js로 결과를 넘겨 ajax 비동기
        return responseJson.toString();
    }


//    모든 조건결과를 통과 후, 새로운 비밀번호 설정을 위해 , 전달 방식을PATCH 를 통해 클라이언트로 부터 들어온 요청값을 기준으로 기존 DB
//    column 의 값들을 수정할 수 있도록 하는 메서드 controller ===============================================

    @RequestMapping(value = "userRecoverPassword", method = RequestMethod.PATCH)
    @ResponseBody

    public String patchRecoverPassword(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "password") String password
//            session으로 내려받은 email 과 현재비밀번호 확인 버튼을 누르면서 검색된 DB 의 record를 통하여 값을 불러와
    ) {
        IResult result = this.memberService.recoverPassword(email, password);
//        불러온 값에 대한 비밀번호의 재설정을 위해 service의 recoverPassword(메서드를 호출하여 input 값에 대해 검색 및 수정.)

        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
//        result 로 넘어온 service의 결과를 기준으로 result : ?? 로 js 로 결과를 넘겨줌으로써 ajax 비동기 처리를 구현토록 한다.
        return responseJson.toString();
    }








//===================================================================================================


    //    로그인 상태를 화면에 보여지게 하는 메서드 // GET  / POST
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


//    로그인 페이지에서 id 와 password 의 값들을 입력하고 click 했을떄, 그 결과값을은 유저의 눈에 보이지 않고 넘겨와 확인해야하기 떄문에,
//    전달방식 POST 를 통해 ResTFul 을 통해 ajax 처리를 한다.

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









//  ==================================================  로그 아웃

    @RequestMapping(value = "userLogout", method = RequestMethod.GET)
    public ModelAndView getUserLogout(ModelAndView modelAndView,
                                      HttpSession session) {
        session.removeAttribute(UserEntity.ATTRIBUTE_NAME);
        modelAndView.setViewName("member/userLogin");
        return modelAndView;
    }

}
