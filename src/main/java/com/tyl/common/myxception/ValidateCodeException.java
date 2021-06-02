package com.tyl.common.myxception;

import org.springframework.security.core.AuthenticationException;

/**
 * 功能描述: 〈自定义验证码异常〉
 *
 * @author yl_tao
 * @date 2021/5/31 15:59
 */
public class ValidateCodeException extends AuthenticationException {
    private static final long serialVersionUID = 5022575393500654458L;

    public ValidateCodeException(String message) {
        super(message);
    }
}