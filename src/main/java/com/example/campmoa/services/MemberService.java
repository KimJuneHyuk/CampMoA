package com.example.campmoa.services;


import com.example.campmoa.components.MailComponent;
import com.example.campmoa.components.SmsComponent;
import com.example.campmoa.entities.member.ContactAuthEntity;
import com.example.campmoa.entities.member.ContactCountryEntity;
import com.example.campmoa.entities.member.UserEntity;
import com.example.campmoa.enums.CommonResult;
import com.example.campmoa.exceptions.RollbackException;
import com.example.campmoa.interfaces.IResult;
import com.example.campmoa.mappers.IMemberMapper;
import com.example.campmoa.regex.MemberRegex;
import com.example.campmoa.utils.CryptoUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service(value = "com.example.campmoa.services.MemberService")
public class MemberService {
    private final IMemberMapper memberMapper;
    private final SmsComponent smsComponent;
    private final MailComponent mailComponent;

    @Autowired
    public MemberService(IMemberMapper memberMapper, SmsComponent smsComponent, MailComponent mailComponent) {
        this.memberMapper = memberMapper;
        this.smsComponent = smsComponent;
        this.mailComponent = mailComponent;
    }

    //   연락처 인증 생성 부분.......contactAuthEntity 관련 메서드 ========= 중요!!!**
    @Transactional
    protected IResult createContactAuth(ContactAuthEntity contactAuth) throws RollbackException, NoSuchAlgorithmException, IOException, InvalidKeyException {
//        해당 메서드 createContactAuth 연락처 인증번호 생성 (매개변수 ContactAuthEntity) 을 사용하여 생성.
        if (contactAuth.getContact() == null || !contactAuth.getContact().matches(MemberRegex.USER_CONTACT)) {
            return CommonResult.FAILURE;
//            매개변수 객체로 넘어온 연락처값이 존재하지 않거나 , 정규식이 맞지 않다면 failure 처리 조건문.
        }
        Date createdAt = new Date();
//        날짜 데이터를 생성하여 현재시간을 설정.
        Date expiresAt = DateUtils.addMinutes(createdAt, 5);
//        commons.lang3의존성의 DateUtile.addMinutes(를 사용하여 현재시간을 기준으로 5분동안 유효시간을 생성)
        String code = RandomStringUtils.randomNumeric(6);
//        commons.lang3 의존성을 사용하여 RandomStringUtils.randomNumeric(인증번호 랜덤 6자리를 생성)
        String salt = CryptoUtils.hashSha512(String.format("%s%s%d%f%f",
                contactAuth.getContact(),
                code,
                createdAt.getTime(),
                Math.random(),
                Math.random()));
//        salt(란) :: SHA-512를 통해 단방향 암호화를 통해 유저 회원가입 토큰을 전달시켜주고 , 랜덤으로 생성된 128자 난수를 생성하여 해킹위험을 방지하도록 salt라는 변수로 저장시켜 인증키가 생성되고 인증될 경우 인증키는 같을 수 있지만 해당 사이트가 아닌 다른곳에서 같은 인증번호로 접근할 경우 salt 값을 비교하여 전달되지 않을경우 실패처리를 하기위해 생성하여 사용한다.

        contactAuth.setCode(code)
                .setSalt(salt)
                .setCreatedAt(createdAt)
                .setExpiresAt(expiresAt)
                .setExpired(false);
//        인증하기 버튼을 클릭할 경우, 기본적으로 생성된 code, salt, CreatedAt, ExpiresAt, Expired 를 설정하여, 인증될경우 Expired 를 업데이트 처리하여 true로 값을 바꿔준다.

        if (this.memberMapper.insertContactAuth(contactAuth) == 0) {
            // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            // return CommonResult.FAILURE;
            throw new RollbackException();
//      java 설정된 ContactAuth 값을 server에 insert하기위한 쿼리를 실행하였고, insert, update, delete 의 반환 타입은 항상 영향을 받은 record의 수 int형을 반환하기 떄문에 영향을 받은 쿼리의 결과 값이 0 일 경우 영향을 받은 쿼리가 없다는 뜻이기 떄문에 예외를 통한 Failure 처리를 한다.
        }

        String smsContent = String.format("[캠프모아] 인증번호 [%s] 를 입력해 주세요.", contactAuth.getCode());
        System.out.println(smsContent + "문자 전송 메세지" + contactAuth.getCode() + "<---코드 6자리 생성된거 전송");
//        위 모든 경우를 통과하였다면 NCP문자인증 API 설정에 의해 문자를 보내도록 한다.

        if (this.smsComponent.send(contactAuth.getContact(), smsContent) != 202) {
            throw new RollbackException();
//            문자인증 API 클래스의 메서드 send(통해 연락처는 input으로 넘어온 연락처, 문자내용) 담아 전달하였을 경우 Html error code ::성공이 아니라면 예외처리를 통한 Failure 처리를 한다.
        }
        return CommonResult.SUCCESS;
    }





//    문자 인증 code, salt 등의... db컬럼 값이 insert 처리 되었고, 인증번호 문자 발송 된지 아닌지를 판단 하는 메서드
    @Transactional
//    Transactional 을 통하여 해당 메서드의 실행도중 오류가 생겼을떄 진행된 상태들을 모두 초기화 시키는 어노테이션을 주입시켜준다.
    public IResult registerAuth(ContactAuthEntity contactAuth) throws
            IOException,
            InvalidKeyException,
            NoSuchAlgorithmException,
            RollbackException {
//        해당 메서드를 사용한 Controller은 만드지 ContactAuthEntity을 매개변수로 받아야한다. 해당매개 변수의 전달된 값을 통해 조건을 실행하기 위한 Service의 매서드를 작성하였다.

        if (contactAuth.getContact() == null || !contactAuth.getContact().matches(MemberRegex.USER_CONTACT)) {
            return CommonResult.FAILURE;
//            contactAuth.getContact()의 값이 없거나 정규식이 맞지 않으면 실패 처리의 조건문
        }

        if (this.memberMapper.selectUserByContact(UserEntity.build().setContact(contactAuth.getContact())) != null) {
//            select * form ~ where `contact` = users.getContact() 결과 값.
            return CommonResult.DUPLICATE;
//            Server에 접근하여 해당정보를를 매개변수의 contactAuth.getContact()를 기준으로 검색하였는데 검색된 User 정보가 존재할 경우 해당 연락처는 이미 존재하는 연락처로 처리 하기위한DuPlicate 조건문.
        }

        if (this.createContactAuth(contactAuth) != CommonResult.SUCCESS) {
            throw new RollbackException();
//            위 모든 조건을 통과 하였지만 문자인증번호가 생성되지 않았을경우 (Success가 아닐경우), 실패처리를 하기위한 Exception을 사용한다.
        }
        return CommonResult.SUCCESS;
//        무엇이 성공인가 (문자인증 발송이 정상처리되었고, 정상적으로 server에 모든결과가 insert 되었다라는 의미.)
    }






//    해당인증번호 인증 결과가 같다면 update ()
//    HTMl 웹 단에서 클라이언트가 인증번호 확인하기 버튼을 클릭했을 경우 실행되는 메서드
    @Transactional
    public IResult checkContactAuth(ContactAuthEntity contactAuth) throws RollbackException {
        if (contactAuth.getContact() == null ||
                contactAuth.getCode() == null ||
                contactAuth.getSalt() == null ||
                !contactAuth.getContact().matches(MemberRegex.USER_CONTACT) ||
                !contactAuth.getCode().matches(MemberRegex.CONTACT_AUTH_CODE) ||
                !contactAuth.getSalt().matches(MemberRegex.CONTACT_AUTH_SALT)) {
            return CommonResult.FAILURE;
        }
//        IResult 타입을 사용하여 반환값을 string 타입의 상수 Enum 열거를 사용한다.
//        매개변수로 들어온 결과 값이 아무것도 없다고, 정규식이 맞지 않을 경우, 실패처리 하는 조건문.

        contactAuth = this.memberMapper.selectContactAuthByContactCodeSalt(contactAuth);
//        매개변수의 담겨 넘어온 값을 기준으로 server의 select 쿼리를 실행 매개변수의 객체로 변환.
//        select * form ~ where code , salt , contact

        if (contactAuth == null) {
            return CommonResult.FAILURE;
//            select 쿼리의 반환 결과가 존재하지 않는다면 Failure 처리.
        }

        //        인증시간을 안했거나 인증시간이 초과하였을경우
        if (contactAuth.isExpired() || new Date().compareTo(contactAuth.getExpiresAt()) > 0) {
//            a. compareTo(b) > 0 :: a 가 b 값을 비교하여 a가 거 크다면 1 반환 아니면 0
            return CommonResult.EXPIRED;
        }

        contactAuth.setExpired(true);
//        위 모든 조건을 통과하였을 경우 기존 값 Expired(false) >> true 로 바꿔 객체에 담아준다.
        if (this.memberMapper.updateContactAuth(contactAuth) == 0) {
            throw new RollbackException();
//            server의 update를 통해 input 정보 그대로에 true로 변경한 쿼리를 실행하였는데 영향을 받은 레코드가 없다면 ( 0 이라는뜻) 그러면 예외처리를 통한 실패 failure 처리
        }
        return CommonResult.SUCCESS;
    }






    //   유효성 검사를통과하고 문자인증 또한 통과 하였을 경우 회원가입 완료를 시키기 위한 메서드()
    @Transactional
    public CommonResult createUser(ContactAuthEntity contactAuth, UserEntity user) throws RollbackException {
        if (contactAuth.getContact() == null ||
                contactAuth.getCode() == null ||
                contactAuth.getSalt() == null ||
                !contactAuth.getContact().matches(MemberRegex.USER_CONTACT) ||
                !contactAuth.getCode().matches(MemberRegex.CONTACT_AUTH_CODE) ||
                !contactAuth.getSalt().matches(MemberRegex.CONTACT_AUTH_SALT)) {
            return CommonResult.FAILURE;
//            input 으로 넘어온 결과가 없거나 , 정규식이 맞지 않을 경우 failure 처리.
        }

        contactAuth = this.memberMapper.selectContactAuthByContactCodeSalt(contactAuth);
//        매개변수의 값을 input 으로 넘어온 결과를 통해 반환된 DB 쿼리의 결과 값으로 설정.
        if (contactAuth == null || !contactAuth.isExpired()) {
            return CommonResult.FAILURE;
//        그 결과 값이 없거나, !contactAuth.isExpired() :: false 이란 뜻.. 일 경우, return Failure
        }

        if (user.getEmail() == null ||
                user.getPassword() == null ||
                user.getName() == null ||
                user.getContact() == null ||
                !user.getEmail().matches(MemberRegex.USER_EMAIL) ||
                !user.getPassword().matches(MemberRegex.USER_PASSWORD) ||
                !user.getName().matches(MemberRegex.USER_NAME) ||
                !user.getContact().matches(MemberRegex.USER_CONTACT)) {
            return CommonResult.FAILURE;
//         input 값으로 넘어온 결과값이 없거나, 정규식에 맞지 않을경우 failure 처리.
        }

        user.setPassword(CryptoUtils.hashSha512(user.getPassword()));
//        input 값으로 넘어온 비밀번호를 단방향 암호화 SHA-512 128자로 바꿔준다.
        if (this.memberMapper.insertUser(user) == 0) {
            throw new RollbackException();
//        input 값으로 넘어온 결과들을 DB insert했을떄, 영향을 받은 record 가 없다( 0: 의미한다),
//        자체생성한 예외를 통한 failure 처리를 한다.
        }
        return CommonResult.SUCCESS;
    }






    //    email 중복 체크 하기. =============================================
    public IResult checkUserEmail(UserEntity user) {
        if (user.getEmail() == null ||
                !user.getEmail().matches(MemberRegex.USER_EMAIL)) {
            return CommonResult.FAILURE;
//            회원가입시 input email 이 값이 없거나, 정규식이 맞지 않을경우, failure 처리.
        }

        user = this.memberMapper.selectUserByEmail(user);
//        유효성 검사를 통화한 결과값의 select * from ~ where email = email; 검색하였을떄
        return user == null
                ? CommonResult.SUCCESS
                : CommonResult.DUPLICATE;
//        반환 결과값이 없다면 회원에 존재하지 않은 email 이기 때문에 사용가능하다.
    }







    //    비밀번호 수정을 위해 이메일과 비밀번호로 기존 유저의 존재 여부 체크 하기 / 로그인할 경우 / checkUserEmailByPassword
    public IResult checkUserEmailByPassword(String email, String password) {
        if (email == null ||
                password == null ||
                !email.matches(MemberRegex.USER_EMAIL) ||
                !password.matches(MemberRegex.USER_PASSWORD)) {
            return CommonResult.FAILURE;
//
        }
        String pw = CryptoUtils.hashSha512(password);

//        user.setPassword(CryptoUtils.hashSha512(user.getPassword()));
        UserEntity existingUser = this.memberMapper.selectCheckPassword(email, pw);
        System.out.println(email);
        System.out.println(pw);
        System.out.println("디비 쿼리로 읽어 들어온 결과 :" + existingUser);
        if (existingUser == null) {
            return CommonResult.FAILURE;
        }
        if (existingUser.getStatusValue().equals("SUS")) {
            return CommonResult.SUSPENDED;
        }
        if (existingUser.getStatusValue().equals("RES")) {
            return CommonResult.RESIGNED;
        }

        return CommonResult.SUCCESS;

    }


    public ContactCountryEntity[] getContactCountries() {
        return this.memberMapper.selectContactCountries();
    }



//  ==========================================================================

    @Transactional
    public IResult loginUser(UserEntity user) {
        if (user.getEmail() == null ||
                user.getPassword() == null ||
                !user.getEmail().matches(MemberRegex.USER_EMAIL) ||
                !user.getPassword().matches(MemberRegex.USER_PASSWORD)) {
            return CommonResult.FAILURE;
        }

        user.setPassword(CryptoUtils.hashSha512(user.getPassword()));
//        System.out.println(user.getPassword());
        UserEntity existingUser = this.memberMapper.selectUserByEmailPassword(user);

//        System.out.println(existingUser);
        if (existingUser == null) {
            return CommonResult.FAILURE;
        }

        user.setEmail(existingUser.getEmail())
                .setPassword(existingUser.getPassword())
                .setName(existingUser.getName())
                .setContactCountryValue(existingUser.getContactCountryValue())
                .setContact(existingUser.getContact())
                .setStatusValue(existingUser.getStatusValue())
                .setRegisterAt(existingUser.getRegisterAt());

//        System.out.println(user);
        if (user.getStatusValue().equals("SUS")) {
            return CommonResult.SUSPENDED;
        }
        if (user.getStatusValue().equals("RES")) {
            return CommonResult.RESIGNED;
        }
        return CommonResult.SUCCESS;
    }

    @Transactional
    public IResult recoverPassword(String email, String password) {
        if (email == null ||
                password == null ||
                !email.matches(MemberRegex.USER_EMAIL) ||
                !password.matches(MemberRegex.USER_PASSWORD)) {
            return CommonResult.FAILURE;
        }

        String hashPassword = CryptoUtils.hashSha512(password);
        int status = this.memberMapper.updateExistingPassword(email, hashPassword);

        if (status == 0) {
            return CommonResult.FAILURE;
        }

        return CommonResult.SUCCESS;
    }


    public UserEntity getUser(String userEmail) {
        return this.memberMapper.selectUserByName(userEmail);
    }
}
