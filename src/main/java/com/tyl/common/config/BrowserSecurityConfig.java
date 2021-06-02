package com.tyl.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * TODO
 *
 * @author yl_tao
 * @date 2021/5/31 14:05
 */
@Configuration
@RequiredArgsConstructor
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    private final MyAuthenticationSuccessHandler authenticationSuccessHandler;

    private final MyAuthenticationFailureHandler authenticationFailureHandler;

    private final ValidateCodeFilter validateCodeFilter;

    private final DataSource dataSource;

    private UserDetailsService userDetailService;

    /**
     * 解决循环依赖 set注入
     *
     * @param userDetailService 自定义用户认证实现类
     */
    @Autowired
    public void setUserDetailService(@Qualifier("userDetailServiceImpl") UserDetailsService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 添加验证码校验过滤器
                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                // 登录跳转 URL
                .loginPage("/authentication/require")
                .loginProcessingUrl("/login")
                // 处理登录成功
                .successHandler(authenticationSuccessHandler)
                // 处理登录失败
                .failureHandler(authenticationFailureHandler)
                .and()
                .rememberMe()
                // 配置 token 持久化仓库
                .tokenRepository(persistentTokenRepository())
                // remember 过期时间，单为秒
                .tokenValiditySeconds(3600)
                // 处理自动登录逻辑
                .userDetailsService(userDetailService)
                .and()
                //授权配置
                .authorizeRequests()
                .antMatchers("/css/**",
                        "/authentication/require",
                        "/login.html",
                        "/code/image")
                .permitAll()
                //所有请求
                .anyRequest()
                //都需要认证
                .authenticated()
                .and()
                //CSRF攻击防御
                .csrf().disable();
    }

    /**
     * 功能描述: 〈用于密码加密〉
     *
     * @return {@link  PasswordEncoder}
     * @author yl_tao
     * @date 2021/5/31 14:23
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
