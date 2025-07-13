package com.example.aihealthmanagement.controller;

import com.example.aihealthmanagement.common.ServiceException;
import com.example.aihealthmanagement.common.ServiceResponse;
import com.example.aihealthmanagement.repository.DietaryRecordRepository;
import com.example.aihealthmanagement.repository.FoodItemRepository;
import com.example.aihealthmanagement.repository.UserActivityRepository;
import com.example.aihealthmanagement.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;

@RestController
@RequestMapping("/api/dietary-stats")
@CrossOrigin(origins = "http://localhost:3000")
public class DietaryStatsController {

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private DietaryRecordRepository dietaryRecordRepository;

    @Autowired
    private UserActivityRepository userActivityRepository;

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails)) {
            throw new ServiceException(401, "User not authenticated");
        }
        return ((CustomUserDetails) auth.getPrincipal()).getId();
    }

    @GetMapping("/food-categories")
    public ServiceResponse<?> getFoodCategoriesData() {
        Long userId = getCurrentUserId();
        
        // Calculate last 3 months date range to include more data
        String[] dateRange = getLastThreeMonthsDateRange();
        String startDate = dateRange[0];
        String endDate = dateRange[1];
        
        System.out.println("=== Food Categories Debug ===");
        System.out.println("User ID: " + userId);
        System.out.println("Date Range: " + startDate + " to " + endDate);
        
        List<Map<String, Object>> categoryData = foodItemRepository.getCategoryDataByUserAndDateRange(
                userId, startDate, endDate);
        
        System.out.println("Raw category data size: " + categoryData.size());
        if (!categoryData.isEmpty()) {
            System.out.println("Sample raw data: " + categoryData.get(0));
            System.out.println("Available keys: " + categoryData.get(0).keySet());
        }
        
        // Transform data for frontend pie chart
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> item : categoryData) {
            Map<String, Object> chartData = new HashMap<>();
            // Use correct field names - category stays the same, but MyBatis converts total_calories to totalCalories
            Object categoryValue = item.get("category");
            Object caloriesValue = item.get("totalCalories");
            
            if (categoryValue != null && caloriesValue != null) {
                chartData.put("type", categoryValue);
                chartData.put("value", caloriesValue);
                result.add(chartData);
            }
        }
        
        System.out.println("Processed result size: " + result.size());
        System.out.println("=== End Food Categories Debug ===");
        
        return ServiceResponse.success("Food categories data retrieved successfully", result);
    }

    @GetMapping("/calorie-comparison")
    public ServiceResponse<?> getCalorieComparisonData() {
        Long userId = getCurrentUserId();
        
        // Calculate last 3 months date range to include more data
        String[] dateRange = getLastThreeMonthsDateRange();
        String startDate = dateRange[0];
        String endDate = dateRange[1];
        
        System.out.println("=== Calorie Comparison Debug ===");
        System.out.println("User ID: " + userId);
        System.out.println("Date Range: " + startDate + " to " + endDate);
        
        // Get intake data
        List<Map<String, Object>> intakeData = dietaryRecordRepository.getCaloriesIntakeByUserAndDateRange(
                userId, startDate, endDate);
        
        // Get burned data
        List<Map<String, Object>> burnedData = userActivityRepository.getCaloriesBurnedByUserAndDateRange(
                userId, startDate, endDate);
        
        System.out.println("Intake data size: " + intakeData.size());
        System.out.println("Burned data size: " + burnedData.size());
        
        if (!intakeData.isEmpty()) {
            System.out.println("Sample intake data: " + intakeData.get(0));
            System.out.println("Intake data keys: " + intakeData.get(0).keySet());
        }
        
        if (!burnedData.isEmpty()) {
            System.out.println("Sample burned data: " + burnedData.get(0));
            System.out.println("Burned data keys: " + burnedData.get(0).keySet());
        }
        
        // Combine data for line chart
        List<Map<String, Object>> result = new ArrayList<>();
        
        // Add intake data - try both field name formats
        for (Map<String, Object> item : intakeData) {
            Map<String, Object> chartData = new HashMap<>();
            // Try both camelCase and snake_case field names
            Object dateValue = item.get("recordDate");
            if (dateValue == null) dateValue = item.get("record_date");
            
            Object caloriesValue = item.get("totalCalories");
            if (caloriesValue == null) caloriesValue = item.get("total_calories");
            
            if (dateValue != null && caloriesValue != null) {
                chartData.put("date", dateValue.toString());
                chartData.put("value", caloriesValue);
                chartData.put("category", "Consumed");
                result.add(chartData);
                System.out.println("Added intake data: date=" + dateValue + ", calories=" + caloriesValue);
            }
        }
        
        // Add burned data - try both field name formats  
        for (Map<String, Object> item : burnedData) {
            Map<String, Object> chartData = new HashMap<>();
            // Try both camelCase and snake_case field names
            Object dateValue = item.get("activityDate");
            if (dateValue == null) dateValue = item.get("activity_date");
            
            Object caloriesValue = item.get("totalCalories");
            if (caloriesValue == null) caloriesValue = item.get("total_calories");
            
            if (dateValue != null && caloriesValue != null) {
                chartData.put("date", dateValue.toString());
                chartData.put("value", caloriesValue);
                chartData.put("category", "Burned");
                result.add(chartData);
                System.out.println("Added burned data: date=" + dateValue + ", calories=" + caloriesValue);
            }
        }
        
        // Sort the result by date to ensure proper chronological order
        result.sort((a, b) -> {
            String dateA = (String) a.get("date");
            String dateB = (String) b.get("date");
            return dateA.compareTo(dateB);
        });
        
        System.out.println("Final result size: " + result.size());
        System.out.println("=== End Calorie Comparison Debug ===");
        
        return ServiceResponse.success("Calorie comparison data retrieved successfully", result);
    }

    @GetMapping("/daily-summary")
    public ServiceResponse<?> getDailySummaryData() {
        Long userId = getCurrentUserId();
        String today = LocalDate.now().toString();
        
        System.out.println("=== Daily Summary Debug ===");
        System.out.println("User ID: " + userId);
        System.out.println("Today: " + today);
        
        // First try to get today's data
        List<Map<String, Object>> todayIntake = dietaryRecordRepository.getCaloriesIntakeByUserAndDateRange(
                userId, today, today);
        List<Map<String, Object>> todayBurned = userActivityRepository.getCaloriesBurnedByUserAndDateRange(
                userId, today, today);
        
        // If no data for today, find the most recent date with data
        String targetDate = today;
        if (todayIntake.isEmpty() && todayBurned.isEmpty()) {
            System.out.println("No data for today, searching for most recent data...");
            
            // Get recent data to find the latest date
            String[] dateRange = getLastThreeMonthsDateRange();
            List<Map<String, Object>> recentIntake = dietaryRecordRepository.getCaloriesIntakeByUserAndDateRange(
                    userId, dateRange[0], dateRange[1]);
            List<Map<String, Object>> recentBurned = userActivityRepository.getCaloriesBurnedByUserAndDateRange(
                    userId, dateRange[0], dateRange[1]);
            
            // Find the most recent date with any data
            LocalDate latestDate = null;
            for (Map<String, Object> item : recentIntake) {
                Object dateValue = item.get("recordDate");
                if (dateValue != null) {
                    LocalDate itemDate = LocalDate.parse(dateValue.toString());
                    if (latestDate == null || itemDate.isAfter(latestDate)) {
                        latestDate = itemDate;
                    }
                }
            }
            for (Map<String, Object> item : recentBurned) {
                Object dateValue = item.get("activityDate");
                if (dateValue != null) {
                    LocalDate itemDate = LocalDate.parse(dateValue.toString());
                    if (latestDate == null || itemDate.isAfter(latestDate)) {
                        latestDate = itemDate;
                    }
                }
            }
            
            if (latestDate != null) {
                targetDate = latestDate.toString();
                System.out.println("Using most recent date with data: " + targetDate);
                
                // Get data for the most recent date
                todayIntake = dietaryRecordRepository.getCaloriesIntakeByUserAndDateRange(
                        userId, targetDate, targetDate);
                todayBurned = userActivityRepository.getCaloriesBurnedByUserAndDateRange(
                        userId, targetDate, targetDate);
            }
        }
        
        System.out.println("Target date: " + targetDate);
        System.out.println("Intake data size: " + todayIntake.size());
        System.out.println("Burned data size: " + todayBurned.size());
        
        int caloriesConsumed = 0;
        int caloriesBurned = 0;
        
        // Calculate calories for the target date only
        for (Map<String, Object> item : todayIntake) {
            Object calories = item.get("totalCalories");
            if (calories != null) {
                caloriesConsumed += ((Number) calories).intValue();
            }
        }
        
        for (Map<String, Object> item : todayBurned) {
            Object calories = item.get("totalCalories");
            if (calories != null) {
                caloriesBurned += ((Number) calories).intValue();
            }
        }
        
        if (!todayIntake.isEmpty()) {
            System.out.println("Sample intake data: " + todayIntake.get(0));
        }
        
        if (!todayBurned.isEmpty()) {
            System.out.println("Sample burned data: " + todayBurned.get(0));
        }
        
        int netCalories = caloriesConsumed - caloriesBurned;
        
        System.out.println("Final values for " + targetDate + " - Consumed: " + caloriesConsumed + ", Burned: " + caloriesBurned + ", Net: " + netCalories);
        System.out.println("=== End Daily Summary Debug ===");
        
        Map<String, Object> result = new HashMap<>();
        result.put("caloriesConsumed", caloriesConsumed);
        result.put("caloriesBurned", caloriesBurned);
        result.put("netCalories", netCalories);
        result.put("trend", netCalories > 0 ? "up" : "down");
        result.put("date", targetDate); // 添加实际使用的日期
        
        return ServiceResponse.success("Daily summary data retrieved successfully", result);
    }

    private String[] getLastThreeMonthsDateRange() {
        LocalDate today = LocalDate.now();
        
        // Get the start date (3 months ago) and end date (today)
        LocalDate startDate = today.minusMonths(3);
        LocalDate endDate = today;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return new String[]{startDate.format(formatter), endDate.format(formatter)};
    }

    private String[] getLastWeekDateRange() {
        LocalDate today = LocalDate.now();
        
        // Get the last 7 days range
        LocalDate startDate = today.minusDays(6);
        LocalDate endDate = today;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return new String[]{startDate.format(formatter), endDate.format(formatter)};
    }
}
