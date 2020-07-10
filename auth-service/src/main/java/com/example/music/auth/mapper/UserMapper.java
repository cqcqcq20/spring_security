package com.example.music.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.music.common.users.AuthorityEntity;
import com.example.music.common.users.UserEntity;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("userMapper")
public interface UserMapper extends BaseMapper<UserEntity> {

    @Select("select * from user where phone = #{phone} and area = #{area}")
    UserEntity findByPhone(String phone,String area);

    @Select("SELECT user.id,role.name FROM user_role JOIN user ON user.id=user_role.uid JOIN role ON user_role.rid=role.id WHERE user_role.uid=#{uid} ")
    List<AuthorityEntity> findRoleByUser(String uid);
}
