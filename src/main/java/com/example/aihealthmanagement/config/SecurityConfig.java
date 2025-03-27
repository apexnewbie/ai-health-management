package com.example.aihealthmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF（无状态 REST API 可关闭 CSRF）
                .csrf(AbstractHttpConfigurer::disable)
                // 配置请求认证规则
                .authorizeHttpRequests(authorize -> authorize
                        // 放行 /auth/register 和 /auth/login 接口
                        .requestMatchers("/auth/register", "/auth/login",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html").permitAll()
                        // 其他接口都需要认证
                        .anyRequest().authenticated()
                )
                // 使用无状态 session 管理，不使用传统 session 机制
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 禁用 HTTP Basic 认证
                .httpBasic(AbstractHttpConfigurer::disable);

        // 如果后续集成 JWT 验证过滤器，可以在这里添加

        return http.build();
    }

    // 定义 BCryptPasswordEncoder Bean，供全局使用（例如在 AuthService 中也可以注入）
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
