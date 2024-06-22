CREATE DATABASE IF NOT EXISTS `xiaomi_car` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `xiaomi_car`;

DROP TABLE IF EXISTS `car`;
CREATE TABLE `car`
(
    `vid`            varchar(32)             NOT NULL COMMENT '车辆识别码Vehicle Identification',
    `car_id`         int(11) unsigned UNIQUE NOT NULL COMMENT '车架编号',
    `battery_type`   varchar(32)             NOT NULL COMMENT '电池类型',
    `total_distance` int(11) unsigned        NOT NULL DEFAULT 0 COMMENT '总里程(km)',
    `battery_health` int(11) unsigned        NOT NULL DEFAULT 100 COMMENT '电池健康状态(%)',
    `create_time`    timestamp               NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_time`    timestamp               NOT NULL DEFAULT current_timestamp ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`      tinyint unsigned        NOT NULL DEFAULT 0 COMMENT '0=未删除,1=逻辑删除',
    PRIMARY KEY (`vid`),
    INDEX  uk_car_id (`car_id`),
    INDEX idx_battery_type (`battery_type`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='车辆信息表';

DROP TABLE IF EXISTS `rule`;
CREATE TABLE `rule`
(
    `id`                  int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '规则序号',
    `warn_id`             int(11) unsigned NOT NULL COMMENT '规则编号id',
    `warn_name`           varchar(64)      NOT NULL COMMENT '规则名称',
    `battery_type`        varchar(64)      NOT NULL COMMENT '电池类型',
    `formula_rate_config` text             NOT NULL COMMENT '预警规则',
    `create_time`         timestamp        NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_time`         timestamp        NOT NULL DEFAULT current_timestamp ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`           tinyint unsigned NOT NULL DEFAULT 0 COMMENT '0=未删除,1=逻辑删除',
    PRIMARY KEY (`id`),
    INDEX idx_warn_id (`warn_id`),
    INDEX idx_battery_type (`battery_type`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='规则表';

DROP TABLE IF EXISTS `record`;
CREATE TABLE `record`
(
    `id`         int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '预警记录id',
    `vid`        varchar(64)      NOT NULL COMMENT '车辆识别码Vehicle Identification',
    `rule_id`    int(11) unsigned NOT NULL COMMENT '规则序号',
    `warn_level` tinyint          NULL COMMENT '报警等级',
    `message`    text             NOT NULL COMMENT '报警信息',
    `occur_time` timestamp        NULL     DEFAULT current_timestamp COMMENT '发生时间',
    `deal_time`  timestamp        NULL     DEFAULT NULL COMMENT '处理时间',
    `is_delete`  tinyint unsigned NOT NULL DEFAULT 0 COMMENT '0=未删除,1=逻辑删除',
    PRIMARY KEY (`id`),
    INDEX idx_rule_id (`rule_id`),
    INDEX idx_vid (`vid`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='预警记录';


# 创建小米汽车电池预警数据库使用的一个用户
CREATE USER 'xiaomi_car_warn_service_new'@'%' IDENTIFIED BY 'XiaoMi1f1sdg4236AFGEfaf2w6g5gwsgawa5faawTwfsh';
GRANT SELECT, INSERT, UPDATE, DELETE ON `xiaomi_car`.* TO 'xiaomi_car_warn_service_new'@'%';
# GRANT ALL PRIVILEGES ON xiaomi_car.* TO 'xiaomi_car_warn_service_new'@'%';
# REVOKE GRANT OPTION ON xiaomi_car.* FROM 'xiaomi_car_warn_service_new'@'%';
FLUSH PRIVILEGES;
SHOW GRANTS FOR 'xiaomi_car_warn_service_new';
# DROP USER 'xiaomi_car_warn_service_new'@'%';


INSERT INTO xiaomi_car.rule (id, warn_id, warn_name, battery_type, formula_rate_config, create_time, update_time, is_delete) VALUES (1, 1, '电压差报警', '三元电池', '{"formula":"${Mx} - ${Mi}","rate":[{"warnLever":0,"condition":[{"operator":">=","value":5.0}]},{"warnLever":1,"condition":[{"operator":">=","value":3.0},{"operator":"<","value":5.0}]},{"warnLever":2,"condition":[{"operator":">=","value":1.0},{"operator":"<","value":3.0}]},{"warnLever":3,"condition":[{"operator":">=","value":0.6},{"operator":"<","value":1.0}]},{"warnLever":4,"condition":[{"operator":">=","value":0.2},{"operator":"<","value":0.6}]},{"warnLever":null,"condition":[{"operator":"<","value":0.2}]}]}', '2024-06-17 16:26:07', '2024-06-17 16:26:07', 0);
INSERT INTO xiaomi_car.rule (id, warn_id, warn_name, battery_type, formula_rate_config, create_time, update_time, is_delete) VALUES (2, 1, '电压差报警', '铁锂电池', '{"formula":"${Mx} - ${Mi}","rate":[{"warnLever":0,"condition":[{"operator":">=","value":2.0}]},{"warnLever":1,"condition":[{"operator":">=","value":1.0},{"operator":"<","value":2.0}]},{"warnLever":2,"condition":[{"operator":">=","value":0.7},{"operator":"<","value":1.0}]},{"warnLever":3,"condition":[{"operator":">=","value":0.4},{"operator":"<","value":0.7}]},{"warnLever":4,"condition":[{"operator":">=","value":0.2},{"operator":"<","value":0.4}]},{"warnLever":null,"condition":[{"operator":"<","value":0.2}]}]}', '2024-06-17 16:26:07', '2024-06-17 16:26:07', 0);
INSERT INTO xiaomi_car.rule (id, warn_id, warn_name, battery_type, formula_rate_config, create_time, update_time, is_delete) VALUES (3, 2, '电流差报警', '三元电池', '{"formula":"${Ix} - ${Ii}","rate":[{"warnLever":0,"condition":[{"operator":">=","value":3.0}]},{"warnLever":1,"condition":[{"operator":">=","value":1.0},{"operator":"<","value":3.0}]},{"warnLever":2,"condition":[{"operator":">=","value":0.2},{"operator":"<","value":1.0}]},{"warnLever":null,"condition":[{"operator":"<","value":0.2}]}]}', '2024-06-17 16:26:07', '2024-06-17 16:26:07', 0);
INSERT INTO xiaomi_car.rule (id, warn_id, warn_name, battery_type, formula_rate_config, create_time, update_time, is_delete) VALUES (4, 2, '电流差报警', '铁锂电池', '{"formula":"${Ix} - ${Ii}","rate":[{"warnLever":0,"condition":[{"operator":">=","value":1.0}]},{"warnLever":1,"condition":[{"operator":">=","value":0.5},{"operator":"<","value":1.0}]},{"warnLever":2,"condition":[{"operator":">=","value":0.2},{"operator":"<","value":0.5}]},{"warnLever":null,"condition":[{"operator":"<","value":0.2}]}]}', '2024-06-17 16:26:07', '2024-06-17 16:26:07', 0);

INSERT INTO xiaomi_car.car (vid, car_id, battery_type, total_distance, battery_health, create_time, update_time, is_delete) VALUES ('429s4i1v67mplcit', 1, '三元电池', 100, 100, '2024-06-19 21:54:47', '2024-06-19 22:19:21', 0);
INSERT INTO xiaomi_car.car (vid, car_id, battery_type, total_distance, battery_health, create_time, update_time, is_delete) VALUES ('weh6ai2qmi69fvqi', 2, '铁锂电池', 600, 95, '2024-06-19 21:54:47', '2024-06-19 21:54:47', 0);
INSERT INTO xiaomi_car.car (vid, car_id, battery_type, total_distance, battery_health, create_time, update_time, is_delete) VALUES ('73dzvl8d9lvzm3yc', 3, '三元电池', 300, 98, '2024-06-19 21:54:47', '2024-06-19 21:54:47', 0);

