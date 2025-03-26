package com.example.aihealthmanagement.repository;

import com.example.aihealthmanagement.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserRepository {

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(@Param("username") String username);

    @Insert("INSERT INTO user(username, password, email, create_time) " +
            "VALUES(#{username}, #{password}, #{email}, #{createTime})")
    int insert(User user);
}
