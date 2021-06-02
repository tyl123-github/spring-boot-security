package com.tyl.common.config;

import com.tyl.common.myxception.ValidateCodeException;
import com.tyl.controller.ValidateController;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 功能描述: 〈自定义验证码过滤器〉
 *
 * @author yl_tao
 * @date 2021/5/31 16:00
 */
@Component
@RequiredArgsConstructor
public class ValidateCodeFilter extends OncePerRequestFilter {

    private final AuthenticationFailureHandler authenticationFailureHandler;

    private static final String LOGIN_URL = "/login";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, @NotNull HttpServletResponse httpServletResponse,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        if (StringUtils.equalsIgnoreCase(LOGIN_URL, httpServletRequest.getRequestURI())
                && StringUtils.equalsIgnoreCase(httpServletRequest.getMethod(), "post")) {
            try {
                validateCode(httpServletRequest);
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void validateCode(HttpServletRequest httpServletRequest) throws ServletRequestBindingException {
        HttpSession session = httpServletRequest.getSession();
        Object codeInSession = session.getAttribute("SESSION_KEY_IMAGE_CODE");
        String imageCode = httpServletRequest.getParameter("imageCode");
        if (StringUtils.isBlank(imageCode)) {
            throw new ValidateCodeException("验证码不能为空！");
        }
        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在！");
        }
        /*if (codeInSession.isExpire()) {
            throw new ValidateCodeException("验证码已过期！");
        }*/
        if (!StringUtils.equalsIgnoreCase((CharSequence) codeInSession, imageCode)) {
            throw new ValidateCodeException("验证码不正确！");
        }
        session.removeAttribute( ValidateController.SESSION_KEY_IMAGE_CODE);
    }

}