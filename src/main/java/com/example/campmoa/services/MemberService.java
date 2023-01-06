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

//   연락처 인증 생성 부분.......contactAuthEntity 관련 메서드
    @Transactional
    protected IResult createContactAuth (ContactAuthEntity contactAuth) throws RollbackException, NoSuchAlgorithmException, IOException, InvalidKeyException {
        if (contactAuth.getContact() == null || !contactAuth.getContact().matches(MemberRegex.USER_CONTACT)) {
            return CommonResult.FAILURE;
        }
        Date createdAt = new Date();
        Date expiresAt = DateUtils.addMinutes(createdAt, 5);
        String code = RandomStringUtils.randomNumeric(6);
        String salt = CryptoUtils.hashSha512(String.format("%s%s%d%f%f" ,
                contactAuth.getContact(),
                code,
                createdAt.getTime(),
                Math.random(),
                Math.random()));
//        System.out.println("인증번호 생성 관련 아이들");
//        System.out.println(createdAt);
//        System.out.println(expiresAt);
//        System.out.println(code);
//        System.out.println(salt);
//        System.out.println("====끝====");

        contactAuth.setCode(code)
                .setSalt(salt)
                .setCreatedAt(createdAt)
                .setExpiresAt(expiresAt)
                .setExpired(false);
        if (this.memberMapper.insertContactAuth(contactAuth) == 0) {
            // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            // return CommonResult.FAILURE;
            throw new RollbackException();
        }
        String smsContent = String.format("[캠프모아] 인증번호 [%s] 를 입력해 주세요.", contactAuth.getCode());
        System.out.println(smsContent + "문자 전송 메세지" + contactAuth.getCode() + "<---코드 6자리 생성된거 전송");
        if (this.smsComponent.send(contactAuth.getContact(), smsContent) != 202) {
            throw new RollbackException();
        }
        return CommonResult.SUCCESS;
    }


    @Transactional
    public IResult checkContactAuth (ContactAuthEntity contactAuth) throws RollbackException {
        if (contactAuth.getContact() == null ||
                contactAuth.getCode() == null ||
                contactAuth.getSalt() == null ||
                !contactAuth.getContact().matches(MemberRegex.USER_CONTACT) ||
                !contactAuth.getCode().matches(MemberRegex.CONTACT_AUTH_CODE) ||
                !contactAuth.getSalt().matches(MemberRegex.CONTACT_AUTH_SALT)) {
            return CommonResult.FAILURE;
        }
        contactAuth = this.memberMapper.selectContactAuthByContactCodeSalt(contactAuth);
//        System.out.println(contactAuth + "checkContactAuth");
        if (contactAuth == null) {
            return CommonResult.FAILURE;
        }

        //        인증시간을 안했거나 인증시간이 초과하였을경우
        if (contactAuth.isExpired() || new Date().compareTo(contactAuth.getExpiresAt()) > 0) {
            return CommonResult.EXPIRED;
        }
        contactAuth.setExpired(true);
        if (this.memberMapper.updateContactAuth(contactAuth) == 0) {
            throw new RollbackException();
        }
        return CommonResult.SUCCESS;
    }

//    ===========================================================
    @Transactional
    public CommonResult createUser (ContactAuthEntity contactAuth, UserEntity user) throws RollbackException {
        if (contactAuth.getContact() == null ||
                contactAuth.getCode() == null ||
                contactAuth.getSalt() == null ||
                !contactAuth.getContact().matches(MemberRegex.USER_CONTACT) ||
                !contactAuth.getCode().matches(MemberRegex.CONTACT_AUTH_CODE) ||
                !contactAuth.getSalt().matches(MemberRegex.CONTACT_AUTH_SALT)) {
            return CommonResult.FAILURE;
        }
        contactAuth = this.memberMapper.selectContactAuthByContactCodeSalt(contactAuth);
        if (contactAuth == null || !contactAuth.isExpired()) {
            return CommonResult.FAILURE;
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
        }
        user.setPassword(CryptoUtils.hashSha512(user.getPassword()));
        if (this.memberMapper.insertUser(user) == 0) {
            throw new RollbackException();
        }
        return CommonResult.SUCCESS;
    }

    //    email 중복 체크 하기.........
    public IResult checkUserEmail(UserEntity user) {
        if (user.getEmail() == null ||
                !user.getEmail().matches(MemberRegex.USER_EMAIL)) {
            return CommonResult.FAILURE;
        }
        user = this.memberMapper.selectUserByEmail(user);
        return user == null
                ? CommonResult.SUCCESS
                : CommonResult.DUPLICATE;
    }


//    비밀번호 수정을 위해 이메일과 비밀번호로 기존 유조의 존재 여부 체크 하기  checkUserEmailByPassword
    public IResult checkUserEmailByPassword(String email, String password) {
        if (email == null ||
                password == null ||
                !email.matches(MemberRegex.USER_EMAIL) ||
                !password.matches(MemberRegex.USER_PASSWORD)) {
            return CommonResult.FAILURE;
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


    @Transactional
    public IResult registerAuth(ContactAuthEntity contactAuth) throws
            IOException,
            InvalidKeyException,
            NoSuchAlgorithmException,
            RollbackException {
        if (contactAuth.getContact() == null || !contactAuth.getContact().matches(MemberRegex.USER_CONTACT)) {
            return CommonResult.FAILURE;
        }
        if (this.memberMapper.selectUserByContact(UserEntity.build().setContact(contactAuth.getContact())) != null) {
            return CommonResult.DUPLICATE;
        }
        if (this.createContactAuth(contactAuth) != CommonResult.SUCCESS) {
            throw new RollbackException();
        }
        return CommonResult.SUCCESS;
    }

//  ==========================================================================

    @Transactional
    public IResult loginUser (UserEntity user) {
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

}
