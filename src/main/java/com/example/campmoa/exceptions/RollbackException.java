package com.example.campmoa.exceptions;

import com.example.campmoa.enums.CommonResult;
import com.example.campmoa.interfaces.IResult;

public class RollbackException extends Exception {
    public final IResult result;

    public RollbackException() {
        this(CommonResult.FAILURE);
    }
    public RollbackException(IResult result) {
        this.result = result;
    }
}
