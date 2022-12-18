package com.example.campmoa.enums;

import com.example.campmoa.entities.ContactCountryEntity;
import com.example.campmoa.interfaces.IResult;

public enum CommonResult implements IResult {
    FAILURE,
    SUCCESS,
    EXPIRED,
    DUPLICATE,
//    회원 일시 정시 상태인 SUSPENDED;
    SUSPENDED,
//    인증 만료 실패 경우인 FAILURE_EXPIRED
    FAILURE_EXPIRED,
    RESIGNED
//    탈퇴 회원

}
