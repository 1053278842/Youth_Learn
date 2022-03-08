package com.ll.youthlearn.config;

import com.ll.youthlearn.service.impl.UserAuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/2/15 21:17
 * @Description :
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Resource
    UserAuthService service;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //TODO 开启csrf,ajax问题使用meta元素设置token解决
        http
            .csrf().disable()
            .authorizeRequests()
//                .antMatchers("/memberTable").authenticated()
                .antMatchers("/register","/login","/Stage/updateStage",
                        "/Org/getTopOrgList","/Org/getOrgsAllStage","/User/Register","/logout","/Org/checkOrgAvailable").permitAll()
                .antMatchers("/static/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login");
        http.logout().logoutSuccessUrl("/memberTable");
    }

    @Bean
    public BCryptPasswordEncoder encoder (){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(service)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

}
