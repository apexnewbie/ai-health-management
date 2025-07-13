package com.example.aihealthmanagement.repository;

import com.example.aihealthmanagement.entity.UserMood;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMoodRepository {
    @Select("SELECT * FROM user_mood WHERE user_id = #{userId}")
    List<UserMood> findByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM user_mood WHERE id = #{id}")
    UserMood findById(@Param("id") Long id);

    @Insert("INSERT INTO user_mood(user_id, total_evaluation, stress_value, record_time) " +
            "VALUES(#{userId}, #{totalEvaluation}, #{stressValue}, #{recordTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserMood mood);

    @Update("UPDATE user_mood SET total_evaluation=#{totalEvaluation}, stress_value=#{stressValue} WHERE id=#{id}")
    int update(UserMood mood);

    @Delete("DELETE FROM user_mood WHERE id = #{id}")
    int delete(@Param("id") Long id);
}
