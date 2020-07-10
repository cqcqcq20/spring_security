package com.example.music.auth.service;

import com.example.music.common.users.AuthorityEntity;
import com.example.music.common.users.UserEntity;
import com.example.music.auth.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.minbox.framework.api.boot.autoconfigure.sequence.ApiBootSequenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component("kiteUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApiBootSequenceContext apiBootSequenceContext;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return loadUserByUsername(username,"86");
    }

    public UserDetails loadUserByUsername(String username, String area) throws UsernameNotFoundException {
        UserEntity byPhone = userMapper.findByPhone(username,area);
        if (byPhone == null) {
            byPhone = createNew(username,area);
        }
        List<AuthorityEntity> entities = userMapper.findRoleByUser(String.valueOf(byPhone.getId()));
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // 用户角色也应在数据库中获取
        for (AuthorityEntity entity : entities) {
            authorities.add(new SimpleGrantedAuthority(entity.getName()));
        }

        // 线上环境应该通过用户名查询数据库获取加密后的密码
        String password = byPhone.getPassword();
        return new org.springframework.security.core.userdetails.User(String.valueOf(byPhone.getId()),password, authorities);
    }

    public UserEntity createNew(String username,String area) {
        UserEntity userEntity = new UserEntity();
        userEntity.setNickname(RandomStringUtils.randomAlphabetic(6));
        userEntity.setPhone(username);
        String password = passwordEncoder.encode(RandomStringUtils.randomAlphanumeric(16));
        userEntity.setPassword(password);
        userEntity.setArea(area);
        userEntity.setId(apiBootSequenceContext.nextId());
        if (userMapper.insert(userEntity) <= 0) {
            return null;
        }
        return userEntity;
    }
}
