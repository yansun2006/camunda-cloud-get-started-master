DROP SCHEMA IF EXISTS `zeebe_demo_db`;
create SCHEMA `zeebe_demo_db`;
USE `zeebe_demo_db`;

drop table if exists t_process_info;
CREATE TABLE `t_process_info` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `org_id` bigint(11) DEFAULT NULL,
  `process_name` varchar(128) DEFAULT NULL,
  `process_def_id` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop table if exists t_process_node;
CREATE TABLE `t_process_node` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `process_id` bigint(11) DEFAULT NULL COMMENT '流程主键ID',
  `node_bpmn_id` varchar(50) DEFAULT NULL COMMENT '流程节点bpmn定义ID（建议UUID）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop table if exists t_process_node_def;
CREATE TABLE `t_process_node_def` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `node_name` varchar(200) DEFAULT NULL COMMENT '节点名字',
  `node_type` varchar(50) DEFAULT NULL COMMENT 'task job type',
  `node_retries` int(11) DEFAULT NULL COMMENT '重试次数',
  `position` int(11) DEFAULT NULL COMMENT '显示顺序ASC',
  PRIMARY KEY (`id`),
  UNIQUE KEY (`node_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop table if exists t_process_node_field;
CREATE TABLE `t_process_node_field` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `node_def_id` bigint(11) NOT NULL COMMENT '流程节点定义主键',
  `field_code` varchar(30) NOT NULL DEFAULT '' COMMENT '字段code，便于代码引用',
  `field_name` varchar(100) NOT NULL DEFAULT '' COMMENT '字段名字',
  `field_desc` varchar(200) DEFAULT NULL COMMENT '字段描述',
  `field_format` varchar(30) NOT NULL DEFAULT '' COMMENT '字段类型 string,boolean,int,float,list',
  `min_length` int(11) DEFAULT NULL COMMENT '最小长度',
  `max_length` int(11) DEFAULT NULL COMMENT '最大长度',
  `required` tinyint(11) DEFAULT '0' COMMENT '是否必填项 0:否；1:是',
  `default_value` text COMMENT '默认值',
  `position` int(11) DEFAULT NULL COMMENT '显示顺序ASC',
  `editable` tinyint(11) DEFAULT '0' COMMENT '是否可编辑',
  `visible` tinyint(11) DEFAULT '0' COMMENT '是否可见',
  `multiple` tinyint(11) DEFAULT '0' COMMENT '单选多选  0:单选；1:多选',
  `parent_Id` bigint(11) DEFAULT NULL,
  `parent_field_option_id` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop table if exists t_process_node_field_option;
CREATE TABLE `t_process_node_field_option` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `field_id` bigint(11) DEFAULT NULL COMMENT '节点字段id主键',
  `option_key` varchar(2) DEFAULT NULL,
  `option_value` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop table if exists t_process_node_field_value;
CREATE TABLE `t_process_node_field_value` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `node_id` bigint(11) DEFAULT NULL COMMENT '流程节点',
  `field_id` bigint(11) DEFAULT NULL COMMENT '字段ID',
  `field_value` text COMMENT '字段值',
  `option_key` varchar(2) DEFAULT NULL COMMENT '选项值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;