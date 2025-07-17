/* ===========================================================
 *  AI-Health-Management  ——  Full Database Schema (One-click)
 *  Target: MySQL 8.x.  If you use 5.7, remove CHECK / GENERATED columns.
 * =========================================================== */

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

START TRANSACTION;

/* -----------------------------------------------------------
 *  0. User master table
 * --------------------------------------------------------- */
CREATE TABLE IF NOT EXISTS `user` (
                                      id           BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      username     VARCHAR(50)  NOT NULL UNIQUE,
    password     VARCHAR(100) NOT NULL,
    email        VARCHAR(100) NOT NULL UNIQUE,
    create_time  DATETIME
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = 'System users';

/* -----------------------------------------------------------
 *  1. Extended user profile  (one-to-one)
 * --------------------------------------------------------- */
CREATE TABLE IF NOT EXISTS user_profile (
                                            id             BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            user_id        BIGINT NOT NULL COMMENT 'FK to user.id',
                                            last_name      VARCHAR(50)  NOT NULL COMMENT 'Last name',
    first_name     VARCHAR(50)  NOT NULL COMMENT 'First name',
    age            TINYINT UNSIGNED NOT NULL COMMENT 'Age (years)',
    occupation     VARCHAR(100) NOT NULL COMMENT 'Occupation',
    gender         ENUM('male','female','other') NOT NULL COMMENT 'Gender',
    favorite_sport VARCHAR(50)  NOT NULL COMMENT 'Preferred sport',
    UNIQUE KEY uq_user_profile (user_id),
    CONSTRAINT fk_user_profile_user
    FOREIGN KEY (user_id) REFERENCES `user`(id)
    ON DELETE CASCADE
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = 'Extended profile (static fields)';

/* -----------------------------------------------------------
 *  2. User activity / health monitoring records (one-to-many)
 * --------------------------------------------------------- */
CREATE TABLE IF NOT EXISTS user_activity (
                                             id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             user_id         BIGINT NOT NULL COMMENT 'FK to user.id',
                                             height          DECIMAL(4,2)  NOT NULL COMMENT 'Height (m)',
    weight          DECIMAL(5,2)  NOT NULL COMMENT 'Weight (kg)',
    bmi             DECIMAL(4,1)            COMMENT 'Body-mass index',
    activity_date   DATE          NOT NULL COMMENT 'Date of activity',
    duration        SMALLINT UNSIGNED NOT NULL COMMENT 'Duration (min)',
    exercise_type   VARCHAR(30)            COMMENT 'Type of exercise',
    steps           INT UNSIGNED  NOT NULL COMMENT 'Step count',
    calories        INT UNSIGNED  NOT NULL COMMENT 'Calories burned (kcal)',
    max_heart_rate  SMALLINT UNSIGNED       COMMENT 'Maximum heart rate',
    min_heart_rate  SMALLINT UNSIGNED       COMMENT 'Minimum heart rate',
    INDEX idx_user_date (user_id, activity_date),
    CONSTRAINT fk_activity_user
    FOREIGN KEY (user_id) REFERENCES `user`(id)
    ON DELETE CASCADE
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = 'User activity / health records';

/* -----------------------------------------------------------
 *  3. Dietary Record — overall information for each meal
 * --------------------------------------------------------- */
CREATE TABLE IF NOT EXISTS dietary_record (
                                              id             BIGINT AUTO_INCREMENT PRIMARY KEY,
                                              user_id        BIGINT NOT NULL COMMENT 'FK to user.id',
                                              record_date    DATE NOT NULL,
                                              record_time    TIME NOT NULL,
                                              meal_type      ENUM('breakfast','lunch','dinner','snack') NOT NULL,
    notes          VARCHAR(255),
    total_calories INT NOT NULL COMMENT 'Total calories for this meal (kcal)',
    create_time    DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_diet_user_date (user_id, record_date),
    CONSTRAINT fk_diet_user
    FOREIGN KEY (user_id) REFERENCES `user`(id)
    ON DELETE CASCADE
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = 'Dietary record table';

/* -----------------------------------------------------------
 *  4. Food Item — nutrition details linked to a meal
 * --------------------------------------------------------- */
CREATE TABLE IF NOT EXISTS food_item (
                                         id                BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         dietary_record_id BIGINT NOT NULL COMMENT 'FK to dietary_record.id',
                                         name              VARCHAR(100) NOT NULL COMMENT 'Food name',
    category          ENUM('fruits','vegetables','grains',
                           'protein','dairy','snacks',
                           'beverages','other') NOT NULL COMMENT 'Food category',
    quantity          DECIMAL(10,2) NOT NULL COMMENT 'Amount',
    unit              VARCHAR(20)  NOT NULL COMMENT 'Measurement unit',
    calories          INT          NOT NULL COMMENT 'Calories for this item (kcal)',
    create_time       DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_food_record (dietary_record_id),
    CONSTRAINT fk_food_record
    FOREIGN KEY (dietary_record_id) REFERENCES dietary_record(id)
    ON DELETE CASCADE
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = 'Food item table';

/* -----------------------------------------------------------
 *  5. User mood records
 *     (MySQL 8.x GENERATED / CHECK syntax in use)
 * --------------------------------------------------------- */
CREATE TABLE IF NOT EXISTS user_mood (
                                         id               BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         user_id          BIGINT NOT NULL COMMENT 'FK to user.id',
                                         total_evaluation ENUM('Stable','Unstable') DEFAULT NULL COMMENT 'Coarse evaluation flag',
    stress_value     INT NOT NULL CHECK (stress_value BETWEEN 0 AND 100)
    COMMENT 'Stress score (0-100)',
    todays_mood      VARCHAR(20) GENERATED ALWAYS AS (
                                                         CASE
                                                         WHEN stress_value > 80 THEN 'Very good'
                                                         WHEN stress_value BETWEEN 60 AND 80 THEN 'Good'
                                                         WHEN stress_value BETWEEN 40 AND 59 THEN 'Sad'
                                                         ELSE 'Depressed'
                                                         END
                                                     ) STORED,
    record_time      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp',
    CONSTRAINT fk_user_mood_user
    FOREIGN KEY (user_id) REFERENCES `user`(id)
    ON DELETE CASCADE
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = 'User mood record';

COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

/* ---------- All tables created successfully ---------- */
