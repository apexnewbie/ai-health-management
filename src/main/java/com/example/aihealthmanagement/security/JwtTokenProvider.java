package com.example.aihealthmanagement.security;

import com.example.aihealthmanagement.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // 密钥（生产环境中请使用更安全的管理方式）
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // 令牌有效期（例如 1 小时）
    private final long validityInMilliseconds = 3600000;

    public String generateToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        // 可以添加其他 claims，比如用户角色等

        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }

    // 你还可以添加验证令牌、解析用户名等辅助方法
}
