CREATE DATABASE IF NOT EXISTS goldengym DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE goldengym;

CREATE TABLE IF NOT EXISTS `user` (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE COMMENT '用户名',
    password    VARCHAR(100) NOT NULL COMMENT '密码（BCrypt加密）',
    email       VARCHAR(100) COMMENT '邮箱',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `exercise_record` (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id        BIGINT NOT NULL COMMENT '关联用户ID',
    type           VARCHAR(20) NOT NULL COMMENT '运动类型：有氧运动/力量训练/功能性训练',
    date           DATE NOT NULL COMMENT '运动日期',
    duration       INT  NOT NULL COMMENT '运动时长（分钟）',
    calories       INT  COMMENT '消耗卡路里（kcal）',
    avg_heart_rate INT  COMMENT '平均心率（bpm）',
    note           VARCHAR(255) COMMENT '备注',
    create_time    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES `user`(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='运动记录表';

CREATE TABLE IF NOT EXISTS `exercise_goal` (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT NOT NULL COMMENT '关联用户ID',
    period          VARCHAR(10) NOT NULL COMMENT '目标周期：每日/每周/每月',
    target_days     INT  COMMENT '目标天数',
    target_duration INT  COMMENT '目标总时长（分钟）',
    target_calories INT  COMMENT '目标总卡路里',
    completed       TINYINT(1) DEFAULT 0 COMMENT '是否完成',
    create_time     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES `user`(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='目标表';

-- 插入默认用户（密码：admin123，BCrypt加密）
INSERT IGNORE INTO `user` (username, password, email) VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'admin@goldengym.com');

-- 性能索引
ALTER TABLE `exercise_record` ADD INDEX idx_user_date (`user_id`, `date`);
ALTER TABLE `exercise_goal` ADD INDEX idx_user_id (`user_id`);
