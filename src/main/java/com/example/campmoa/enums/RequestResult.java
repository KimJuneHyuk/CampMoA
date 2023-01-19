package com.example.campmoa.enums;

import com.example.campmoa.interfaces.IResult;

public enum RequestResult implements IResult {
    NOT_FOUND,  // 찾지 못함
    NOT_SIGNED, // 로그인 하지 않음
    YOURSELF    // 본인이 동행 신청 클릭시
}
