package com.example.campmoa.enums.bbs;

import com.example.campmoa.interfaces.IResult;

public enum WriteResult implements IResult {
    NOT_ALLOWED, // 허락되지 않는 요청.. 제목, 공란, 미입력시 요청 실패..

    NO_SUCH_BOARD // 존재하지 않는 게시판.
}
