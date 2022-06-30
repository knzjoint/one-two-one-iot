CREATE USER product_manager@'%' IDENTIFIED  WITH mysql_native_password BY 'product_manager';
-- 创建数据库
create DATABASE IF NOT EXISTS product_manager DEFAULT charset utf8mb4;
-- 授权  开发环境可以添加 alter
GRANT create,insert,select,update,delete,drop ON product_manager.* TO product_manager@'%';


flush privileges;
use product_manager;


-- 用户
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户表id',
    `username` varchar(50) COMMENT '用户名',
    `truename` varchar(50) COMMENT '真实姓名',
    `avatar` varchar(256) COMMENT '头像',
    `password` varchar(256) NOT NULL COMMENT '用户密码，MD5加密',
    `email` varchar(50) DEFAULT NULL,
    `phone` varchar(20) DEFAULT NULL,
    `role` int(4) NOT NULL COMMENT '角色0-管理员,1-个人用户,2公司用户',
    `user_desc` varchar(256) COMMENT '用户描述',
	`create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',
	PRIMARY KEY (`id`),
	UNIQUE KEY `phone_unique` (`phone`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 用户日志
DROP TABLE IF EXISTS `login_logs`;
CREATE TABLE `login_logs` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户日志id',
    `user_id` int(11) NOT NULL COMMENT '用户ID',
    `login_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '登录时间',
    `logout_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '登出时间',
	`create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 产品
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '产品id',
    `user_id` int(11) NOT NULL COMMENT '用户ID',
    `product_name` varchar(256) NOT NULL COMMENT '产品名称',
    `product_en_name` varchar(256) NOT NULL COMMENT '产品的英文名称用来限制topic',
    `product_desc` varchar(512) COMMENT '产品描述',
    `deleted` BOOLEAN NOT NULL DEFAULT false COMMENT '是否删除，true删除,false未删除',
	`create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 产品和主题的绑定
DROP TABLE IF EXISTS `product_topic`;
CREATE TABLE `product_topic` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '产品主题id',
    `product_id` int(11) NOT NULL COMMENT '产品ID',
    `allow` int(1) DEFAULT 1 COMMENT '0: deny, 1: allow',
  	`ipaddr` varchar(60) DEFAULT NULL COMMENT 'IpAddress',
  	`access` int(2) NOT NULL COMMENT '1: subscribe, 2: publish, 3: pubsub',
    `topic` varchar(512) NOT NULL COMMENT '主题',
    `topic_desc` varchar(256) NOT NULL DEFAULT '' COMMENT '主题描述',
    `operator_auth` boolean NOT NULL DEFAULT true COMMENT '是否可以被删除/修改操作， true 可以， false不可以',
	`create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




-- 产品和主题的绑定
DROP TABLE IF EXISTS `product_topic_bind`;
CREATE TABLE `product_topic_bind` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '产品主题id',
    `product_id` int(11) NOT NULL COMMENT '产品ID',
    `topic_id` int(11) NOT NULL COMMENT '主题ID',
	`create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




-- 产品中的设备
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '设备id',
    `user_id` int(11) NOT NULL COMMENT '用户ID',
    `product_id` int(11) NOT NULL COMMENT '产品ID',
    `mqtt_user_id` int(11) NOT NULL COMMENT 'mqtt设备名称ID',
    `username` varchar(128) NOT NULL COMMENT '用户名',
  	`password` varchar(128) NOT NULL COMMENT '用户密码,未加密',
    `client_id` varchar(128) NOT NULL COMMENT 'mqtt设备的clientId',
    `connect_status` BOOLEAN NOT NULL COMMENT '连接状态，false，true 连接',
    `deleted` BOOLEAN NOT NULL DEFAULT false COMMENT '是否删除，true删除,false未删除',
	`create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',
	PRIMARY KEY (`id`),
	UNIQUE KEY `client_id_unique` (`client_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 设备连接信息(连接时长)
DROP TABLE IF EXISTS `connect_logs`;
CREATE TABLE `connect_logs` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '设备连接信息id',
    `device_id` int(11) NOT NULL COMMENT '设备ID',
    `connect_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '连接时间',
    `disconnect_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '断开连接时间',
	`create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 设备事件
DROP TABLE IF EXISTS device_events;
CREATE TABLE `device_events` (
	`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '设备事件id',
    `msg_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '消息的时间',
    `product_id` int(11) NOT NULL COMMENT '产品ID',
    `device_id` int(11) NOT NULL COMMENT '设备ID',
    `data_type` TINYINT NOT NULL DEFAULT 1 COMMENT '数据类型:  1:event  2:response',
    `topic` varchar(512) NOT NULL COMMENT '主题',
    `stream_id` varchar(128) NOT NULL COMMENT '数据流ID',
    `data` JSON COMMENT '接收的数据',
    `response_result` JSON COMMENT '返回的数据',
    `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',
	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 应用token表
DROP TABLE IF EXISTS `application_token`;
CREATE TABLE `application_token` (
	`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '应用token id',
	`user_id` int(11) NOT NULL COMMENT '用户ID',
    `product_id` int(11) NOT NULL COMMENT '产品ID',
    `application_name` varchar(128) NOT NULL COMMENT '应用名称',
    `role` varchar(128) NOT NULL COMMENT 'Pulsar role角色',
    `tenant_name` varchar(128) NOT NULL COMMENT '租户名',
    `namespace` varchar(128) NOT NULL COMMENT '命名空间',
    `token` varchar(512) NOT NULL COMMENT 'token',
    `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',
	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

 ALTER TABLE device_events ADD event_action varchar(32) COMMENT '事件类型' AFTER `topic`;



-- --- 产品模型 --------------
-- things,data_type,things_template,things_event,venues,venues_device,things_linkage,qrtz_cron
-- 物表  添加一个value
DROP TABLE IF EXISTS `things`;
CREATE TABLE `things` (
	`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '传感器/控制器 id',
	`user_id` int(11) NOT NULL COMMENT '用户ID',
	`client_id` varchar(128) NOT NULL COMMENT 'mqtt设备的clientId',
	`serial_number` varchar(64) NOT NULL COMMENT '序列号',
	`version` varchar(28) NOT NULL COMMENT '版本号',
	`sequence` int(8) NOT NULL COMMENT '产品队列',
	`data_type_id` int(11) NOT NULL COMMENT '数据类型id',
	`data_format` JSON NOT NULL DEFAULT ('{}') COMMENT '数据格式详情',
	`things_name` varchar(128) NOT NULL COMMENT '物名称（英文）',
	`name` varchar(128) NOT NULL COMMENT '中文名称物名称',
	`things_icon` varchar(256) COMMENT '物图链接',
	`things_desc` varchar(256) COMMENT '描述',
	`control_ui` JSON NOT NULL DEFAULT ('{}') COMMENT '控制ui',
	`things_value` varchar(256) COMMENT '物值',
	`things_type` tinyint COMMENT '1 整形， 2 浮点， 3 字符， 4 布尔类型',
	`is_show` BOOLEAN NOT NULL DEFAULT TRUE COMMENT '是否显示，true 显示',
	`rw_type` TINYINT DEFAULT 1 COMMENT '1 是只读, 2 只写， 3 读写',
	`is_startup` boolean DEFAULT true COMMENT 'true为启动，false关闭',
    `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',
	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据类型
DROP TABLE IF EXISTS `data_type`;
CREATE TABLE `data_type` (
	`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '应用token id',
	`user_id` int(11) NOT NULL COMMENT '用户ID',
	`name` varchar(64) NOT NULL COMMENT '名称',
	`is_private` boolean DEFAULT true COMMENT '是否为私有的类型, true 是,false 不是私有',
	`format_type` tinyint DEFAULT 1 COMMENT '1 整形， 2 浮点， 3 字符， 4 布尔类型',
	`json_format` JSON NOT NULL DEFAULT ('{}') COMMENT '格式类型',
	`data_type_desc` varchar(256) NOT NULL COMMENT '描述',
    `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',
	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 物产品类型模板
DROP TABLE IF EXISTS `things_template`;
CREATE TABLE `things_template` (
	`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '物产品类型模板 id',
	`user_id` int(11) NOT NULL COMMENT '用户ID',
	`is_private` boolean DEFAULT true COMMENT '是否为私有的类型, true 是,false 不是私有',
	`data_type_id` int(11) NOT NULL COMMENT '产品类型id',
	`data_format` JSON NOT NULL DEFAULT ('{}') COMMENT '数据格式详情',
	`things_name` varchar(128) NOT NULL COMMENT '物名称',
	`things_icon` varchar(256) COMMENT '物图链接',
	`things_desc` varchar(256) COMMENT '描述',
	`is_show` BOOLEAN NOT NULL DEFAULT TRUE COMMENT '是否显示，true 显示',
	`rw_type` TINYINT DEFAULT 1 COMMENT '1 是只读, 2 只写， 3 读写',
    `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',
	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 物事件
DROP TABLE IF EXISTS `things_event`;
CREATE TABLE `things_event` (
	`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '物事件 id',
	`user_id` int(11) NOT NULL COMMENT '用户ID',
	`things_id` int(11) NOT NULL COMMENT '物ID',
    `rule` JSON NOT NULL DEFAULT ('{}') COMMENT '规则',
    `is_startup` boolean DEFAULT true COMMENT 'true为启动，false关闭',
    `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',
	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 场地表名
DROP TABLE IF EXISTS `venues`;
CREATE TABLE `venues` (
	`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '场地 id',
	`user_id` int(11) NOT NULL COMMENT '用户ID',
    `venues_name` varchar(64) NOT NULL COMMENT '场地名称',
    `venues_icon` varchar(128) COMMENT '场地图标',
    `parent_id` int(11) DEFAULT 0 COMMENT '做树形结构，0时，为最顶层',
    `venues_desc` varchar(256) COMMENT '场地描述',
    `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',
	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 场地设备表
DROP TABLE IF EXISTS `venues_device`;
CREATE TABLE `venues_device` (
	`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '场地 id',
	`user_id` int(11) NOT NULL COMMENT '用户ID',
    `venues_id` int(11) NOT NULL COMMENT '场地ID',
    `device_id` int(11) NOT NULL COMMENT '设备ID',
    `venues_icon` varchar(256) COMMENT '场地图片链接',
    `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',
	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 联动
DROP TABLE IF EXISTS `things_linkage`;
CREATE TABLE `things_linkage` (
	`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '联动 id',
	`name` varchar(256) NOT NULL COMMENT '联动场景名称',
	`user_id` int(11) NOT NULL COMMENT '用户ID',
	`venues_id` int(11) NOT NULL COMMENT '场地ID',
	`priority` tinyint NOT NULL DEFAULT 0 COMMENT '优先级 0 最大',
	`control_ui` JSON NOT NULL DEFAULT ('{}') COMMENT '联动流程图用于生成规则',
    `rule` JSON NOT NULL DEFAULT ('{}') COMMENT '规则',
    `linkage_desc` varchar(256) NOT NULL COMMENT '联动场景描述',
    `is_startup` boolean DEFAULT true COMMENT 'true为启动，false关闭',
    `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',
	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 联动关联物
DROP TABLE IF EXISTS `things_linkage_link`;
CREATE TABLE `things_linkage_link` (
	`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '联动关联的条件ID',
	`things_linkage_id` int(11) NOT NULL COMMENT '联动 id',
	`link_id` int(11) NOT NULL COMMENT '关联的ID',
	`link_type` tinyint NOT NULL COMMENT '关联的类型',
    `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',
	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 定时任务
DROP TABLE IF EXISTS `qrtz_cron`;
CREATE TABLE `qrtz_cron`  (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '定时任务 id',
    `qrtz_name` varchar(256) NOT NULL COMMENT '任务名称不能为空',
--     `things_id` int(11) NOT NULL COMMENT '物ID',
    `cron_expression` varchar(256)  NOT NULL COMMENT 'cron表达式',
    `time_zone_id` varchar(80) DEFAULT NULL COMMENT '时区',
    `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',
	PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = 'Cron类型的触发器表';



INSERT INTO `product_manager`.`data_type` (`id`, `user_id`, `name`, `is_private`, `format_type`, `json_format`, `data_type_desc`, `create_time`, `update_time`) VALUES (1, 0, '整型', 0, 1, '{}', '整型', '2022-06-23 13:35:57', '2022-06-23 13:35:57');
INSERT INTO `product_manager`.`data_type` (`id`, `user_id`, `name`, `is_private`, `format_type`, `json_format`, `data_type_desc`, `create_time`, `update_time`) VALUES (2, 0, '浮点', 0, 2, '{}', '浮点', '2022-06-23 13:36:51', '2022-06-23 13:36:51');
INSERT INTO `product_manager`.`data_type` (`id`, `user_id`, `name`, `is_private`, `format_type`, `json_format`, `data_type_desc`, `create_time`, `update_time`) VALUES (3, 0, '字符', 0, 3, '{}', '字符', '2022-06-23 13:37:15', '2022-06-23 13:37:15');
INSERT INTO `product_manager`.`data_type` (`id`, `user_id`, `name`, `is_private`, `format_type`, `json_format`, `data_type_desc`, `create_time`, `update_time`) VALUES (4, 0, '布尔类型', 0, 4, '{}', '布尔', '2022-06-23 13:37:44', '2022-06-23 13:37:44');
INSERT INTO `product_manager`.`data_type` (`id`, `user_id`, `name`, `is_private`, `format_type`, `json_format`, `data_type_desc`, `create_time`, `update_time`) VALUES (5, 0, 'JSON', 0, 5, '{}', 'JSOJSON', '2022-06-23 13:38:17', '2022-06-23 13:38:17');

