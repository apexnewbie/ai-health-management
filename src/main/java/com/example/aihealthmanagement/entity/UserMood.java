package com.example.aihealthmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMood {
    private Long id;
    private Long userId;
    private String totalEvaluation;
    private Integer stressValue;
    private String todaysMood;
    private LocalDateTime recordTime;
}
