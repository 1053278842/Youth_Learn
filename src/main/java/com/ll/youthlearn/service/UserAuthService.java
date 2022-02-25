package com.ll.youthlearn.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ll.youthlearn.entity.User;
import com.ll.youthlearn.mapper.IUserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/2/15 21:23
 * @Description :
 */
@Service
public class UserAuthService implements UserDetailsService {

    @Resource
    IUserMapper userMapper;

    final HttpSession session;

    public UserAuthService(HttpSession session) {
        this.session = session;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", username));
        //TODO session不安全
        session.setAttribute("USER_INFO",user);
        if (user==null){
            throw new UsernameNotFoundException("用户"+username+"登录失败,用户名不存在！");
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
