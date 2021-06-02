package com.tyl.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 功能描述: 〈图形验证码〉
 *
 * @author yl_tao
 * @date 2021/5/31 15:44
 */
@RestController
public class ValidateController {

    public final static String SESSION_KEY_IMAGE_CODE = "SESSION_KEY_IMAGE_CODE";


    /**
     * 功能描述: 〈获取图形验证码〉
     *
     * @param request: 请求参数
     * @return {@link  String}
     * @author yl_tao
     * @date 2021/5/31 15:50
     */
    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(200, 100);
        String code = circleCaptcha.getCode();
        HttpSession session = request.getSession();
        session.setAttribute(SESSION_KEY_IMAGE_CODE, code);
        circleCaptcha.getImageBase64Data();
        circleCaptcha.write(response.getOutputStream());
    }
}