package com.example.aihealthmanagement.service.impl;

import com.example.aihealthmanagement.common.ServiceException;
import com.example.aihealthmanagement.entity.UserMood;
import com.example.aihealthmanagement.repository.UserMoodRepository;
import com.example.aihealthmanagement.service.UserMoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserMoodServiceImpl implements UserMoodService {

    private final UserMoodRepository repository;

    @Autowired
    public UserMoodServiceImpl(UserMoodRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserMood> listByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public UserMood getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void create(UserMood mood) {
        mood.setRecordTime(LocalDateTime.now());
        repository.insert(mood);
    }

    @Override
    public void update(UserMood mood) {
        if (repository.findById(mood.getId()) == null) {
            throw new ServiceException(404, "Mood not found");
        }
        repository.update(mood);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }
}
