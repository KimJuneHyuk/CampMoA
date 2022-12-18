package com.example.campmoa.mappers;

import com.example.campmoa.entities.ContactAuthEntity;
import com.example.campmoa.entities.ContactCountryEntity;
import com.example.campmoa.entities.EmailAuthEntity;
import com.example.campmoa.entities.UserEntity;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMemberMapper {
    int insertContactAuth(ContactAuthEntity contactAuth);
    int insertEmailAuth(EmailAuthEntity emailAuth);
    int insertUser(UserEntity user);


    ContactAuthEntity selectContactAuthByContactCodeSalt(ContactAuthEntity contactAuth);
    UserEntity selectUserByContact(UserEntity user);
    UserEntity selectUserByEmail(UserEntity user);
    UserEntity selectUserByEmailPassword(UserEntity user);
    ContactCountryEntity[] selectContactCountries();


    int updateContactAuth(ContactAuthEntity contactAuth);
}
