package com.example.aihealthmanagement.controller;

import com.example.aihealthmanagement.common.ServiceException;
import com.example.aihealthmanagement.common.ServiceResponse;
import com.example.aihealthmanagement.dto.UserMoodDto;
import com.example.aihealthmanagement.entity.UserMood;
import com.example.aihealthmanagement.security.CustomUserDetails;
import com.example.aihealthmanagement.service.UserMoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-moods")
public class UserMoodController {

    @Autowired
    private UserMoodService userMoodService;

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails)) {
            throw new ServiceException(401, "User not authenticated");
        }
        return ((CustomUserDetails) auth.getPrincipal()).getId();
    }

    @GetMapping
    public ServiceResponse<List<UserMood>> list() {
        Long userId = getCurrentUserId();
        List<UserMood> moods = userMoodService.listByUserId(userId);
        return ServiceResponse.success(moods);
    }

    @GetMapping("/{id}")
    public ServiceResponse<UserMood> get(@PathVariable Long id) {
        UserMood mood = userMoodService.getById(id);
        if (mood == null) {
            throw new ServiceException(404, "Mood not found");
        }
        return ServiceResponse.success(mood);
    }

    @PostMapping
    public ServiceResponse<?> create(@RequestBody UserMoodDto.MoodRequest request) {
        Long userId = getCurrentUserId();
        UserMood mood = UserMood.builder()
                .userId(userId)
                .totalEvaluation(request.getTotalEvaluation())
                .stressValue(request.getStressValue())
                .build();
        userMoodService.create(mood);
        return ServiceResponse.success("Created", null);
    }

    @PutMapping("/{id}")
    public ServiceResponse<?> update(@PathVariable Long id, @RequestBody UserMoodDto.MoodRequest request) {
        Long userId = getCurrentUserId();
        UserMood mood = UserMood.builder()
                .id(id)
                .userId(userId)
                .totalEvaluation(request.getTotalEvaluation())
                .stressValue(request.getStressValue())
                .build();
        userMoodService.update(mood);
        return ServiceResponse.success("Updated", null);
    }

    @DeleteMapping("/{id}")
    public ServiceResponse<?> delete(@PathVariable Long id) {
        userMoodService.delete(id);
        return ServiceResponse.success("Deleted", null);
    }
}
