package com.example.campmoa.interfaces;

public interface IResult {
    public static final String ATTRIBUTE_NAME = "result";
//    인터페이스는 상수 선언만 가능 하고 상수 선언 시 :: public static final 생략 가능.
    String ATTRIBUTE_NAME_PLURAL = "results";

//    인터페이스 메서드 선언시 public abstract 생략가능
    public abstract String name();
}
