package com.example.campmoa.enums.bbs;

import com.example.campmoa.interfaces.IResult;

public enum ModifyArticleResult implements IResult {
    NO_SUCH_ARTICLE, // 존재하지 않는 게시글 일 경우
    NOT_ALLOWED,    // 게시글 작성자 != 로그인 한 유저와 다를 경우.
    NOT_SIGNED      // 로그인 하지 않았을 경우.
}
