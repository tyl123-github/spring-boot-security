package com.tyl.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能描述: 〈测试  spring security〉
 *
 * @author yl_tao
 * @date 2021/5/31 14:02
 */
@RestController
public class TestController {

    /**
     * 功能描述: 〈测试接口〉
     *
     * @return {@link  String}
     * @author yl_tao
     * @date 2021/5/31 14:13
     */
    @GetMapping("/hello")
    public String hello() {
        return "hello spring security";
    }
}