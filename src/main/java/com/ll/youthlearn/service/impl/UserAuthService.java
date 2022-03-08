package com.ll.youthlearn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ll.youthlearn.entity.OrgPath;
import com.ll.youthlearn.entity.User;
import com.ll.youthlearn.mapper.IOrgPathMapper;
import com.ll.youthlearn.mapper.IUserMapper;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class UserAuthService implements UserDetailsService {

    @Resource
    IUserMapper userMapper;

    private final IOrgPathMapper orgPathMapper;

    final HttpSession session;

    public UserAuthService(HttpSession session, IOrgPathMapper orgPathMapper) {
        this.session = session;
        this.orgPathMapper = orgPathMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", username));
        user.setPaths(orgPathMapper.selectList(new QueryWrapper<OrgPath>().eq("user_id",user.getId()).orderByAsc("org_path")));
        //判空
        if(user.getPaths().size()!=0){
            user.setCurrent_path(user.getPaths().get(0));
        }
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
