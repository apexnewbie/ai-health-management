package com.example.aihealthmanagement.service;

import com.example.aihealthmanagement.entity.UserMood;

import java.util.List;

public interface UserMoodService {
    List<UserMood> listByUserId(Long userId);
    UserMood getById(Long id);
    void create(UserMood mood);
    void update(UserMood mood);
    void delete(Long id);
}
