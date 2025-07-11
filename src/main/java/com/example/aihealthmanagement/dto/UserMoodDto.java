package com.example.aihealthmanagement.dto;

import lombok.Data;

public class UserMoodDto {
    @Data
    public static class MoodRequest {
        private String totalEvaluation;
        private Integer stressValue;
    }

    @Data
    public static class MoodResponse {
        private Long id;
        private Long userId;
        private String totalEvaluation;
        private Integer stressValue;
        private String todaysMood;
    }
}
