package com.offcn.user.exception;

import com.offcn.user.enums.UserExceptionEnum;

public class UserException extends RuntimeException {
    public UserException() {
    }

    public UserException(UserExceptionEnum enumException){

        super(enumException.getMessage());
    }

}
