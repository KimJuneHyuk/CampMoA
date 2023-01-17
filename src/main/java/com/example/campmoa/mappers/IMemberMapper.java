package com.example.campmoa.mappers;

import com.example.campmoa.entities.member.ContactAuthEntity;
import com.example.campmoa.entities.member.ContactCountryEntity;
import com.example.campmoa.entities.member.EmailAuthEntity;
import com.example.campmoa.entities.member.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IMemberMapper {
    int insertContactAuth(ContactAuthEntity contactAuth);
    int insertEmailAuth(EmailAuthEntity emailAuth);
    int insertUser(UserEntity user);


    ContactAuthEntity selectContactAuthByContactCodeSalt(ContactAuthEntity contactAuth);
    UserEntity selectUserByContact(UserEntity user);
    UserEntity selectUserByEmail(UserEntity user);
//    UserEntity selectUserEmailByPassword(UserEntity user);


    UserEntity selectCheckPassword(@Param(value = "email") String email,
                                   @Param(value = "password") String password);
    UserEntity selectUserByEmailPassword(UserEntity user);



    ContactCountryEntity[] selectContactCountries();


    int updateExistingPassword(@Param(value = "email") String email,
                               @Param(value = "password") String password);

    int updateContactAuth(ContactAuthEntity contactAuth);

    UserEntity selectUserByName(String userEmail);
}
