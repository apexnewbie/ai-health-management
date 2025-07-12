/* 新增用户心情记录表 —— 直接复制整段在 MySQL 8.x 命令行执行即可 */
/* 如果你在 MySQL 5.7 或更旧版本上，请改用触发器方案 */

CREATE TABLE user_mood (
                           id               BIGINT AUTO_INCREMENT PRIMARY KEY,        -- 主键
                           user_id          BIGINT NOT NULL,                          -- 关联 user.id
                           total_evaluation ENUM('Stable','Unstable') DEFAULT NULL,   -- 总体评估
                           stress_value     INT NOT NULL                              -- 压力值 0-100
                               CHECK (stress_value BETWEEN 0 AND 100),

    /* 依据 stress_value 自动计算今日心情 */
                           todays_mood      VARCHAR(20) GENERATED ALWAYS AS (
                               CASE
                                   WHEN stress_value > 80 THEN 'Very good'
                                   WHEN stress_value BETWEEN 60 AND 80 THEN 'Good'
                                   WHEN stress_value BETWEEN 40 AND 59 THEN 'Sad'
                                   ELSE 'Depressed'
                                   END
                               ) STORED,

                           record_time      DATETIME DEFAULT CURRENT_TIMESTAMP,       -- 记录时间

    /* 外键约束：删除用户时级联删除其心情记录 */
                           CONSTRAINT fk_user_mood_user
                               FOREIGN KEY (user_id) REFERENCES `user`(id)
                                   ON DELETE CASCADE
) ENGINE = InnoDB;
