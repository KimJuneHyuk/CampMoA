package com.example.campmoa.entities.member;

import java.util.Date;
import java.util.Objects;

public class UserEntity  {
    public static final String ATTRIBUTE_NAME = "memberUser";
    public static final String ATTRIBUTE_NAME_PLURAL = "memberUsers";


    public static UserEntity build() {
        return new UserEntity();
    }



    private String email;
    private String password;
    private String name;
    private String contactCountryValue;
    private String contact;
    private String statusValue;
    private Date registerAt;

    public UserEntity() {
    }

    public UserEntity(String email, String password, String name, String contactCountryValue, String contact, String statusValue, Date registerAt) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.contactCountryValue = contactCountryValue;
        this.contact = contact;
        this.statusValue = statusValue;
        this.registerAt = registerAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getContactCountryValue() {
        return contactCountryValue;
    }

    public UserEntity setContactCountryValue(String contactCountryValue) {
        this.contactCountryValue = contactCountryValue;
        return this;
    }

    public String getContact() {
        return contact;
    }

    public UserEntity setContact(String contact) {
        this.contact = contact;
        return this;
    }

    public String getStatusValue() {
        return statusValue;
    }

    public UserEntity setStatusValue(String statusValue) {
        this.statusValue = statusValue;
        return this;
    }

    public Date getRegisterAt() {
        return registerAt;
    }

    public UserEntity setRegisterAt(Date registerAt) {
        this.registerAt = registerAt;
        return this;
    }
}
