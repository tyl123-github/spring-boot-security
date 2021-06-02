package com.tyl.common.config;

import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 *
 * @author yl_tao
 * @date 2021/5/31 14:26
 */
@Data
public class MyUser implements Serializable {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 账户是否未过期
     */
    private boolean accountNonExpired = true;

    /**
     * 账户是否未锁定
     */
    private boolean accountNonLocked = true;

    /**
     * 用户凭证是否没过期，即密码是否未过期
     */
    private boolean credentialsNonExpired = true;

    /**
     * 用户是否可用
     */
    private boolean enabled = true;
}
