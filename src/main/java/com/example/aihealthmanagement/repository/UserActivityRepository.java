package com.example.aihealthmanagement.repository;

import com.example.aihealthmanagement.entity.UserActivity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserActivityRepository {
    @Select("SELECT * FROM user_activity WHERE user_id = #{userId}")
    List<UserActivity> findByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM user_activity WHERE id = #{id}")
    UserActivity findById(@Param("id") Long id);

    @Insert("INSERT INTO user_activity(user_id, height, weight, bmi, activity_date, duration, exercise_type, steps, calories, max_heart_rate, min_heart_rate) " +
            "VALUES(#{userId}, #{height}, #{weight}, #{bmi}, #{activityDate}, #{duration}, #{exerciseType}, #{steps}, #{calories}, #{maxHeartRate}, #{minHeartRate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserActivity activity);

    @Update("UPDATE user_activity SET height=#{height}, weight=#{weight}, bmi=#{bmi}, activity_date=#{activityDate}, duration=#{duration}, exercise_type=#{exerciseType}, steps=#{steps}, calories=#{calories}, max_heart_rate=#{maxHeartRate}, min_heart_rate=#{minHeartRate} WHERE id=#{id}")
    int update(UserActivity activity);

    @Delete("DELETE FROM user_activity WHERE id = #{id}")
    int delete(@Param("id") Long id);
}
