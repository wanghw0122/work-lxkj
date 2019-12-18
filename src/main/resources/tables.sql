CREATE TABLE `rc_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_account` varchar(50) NOT NULL DEFAULT '' COMMENT '用户的邮箱账号',
  `three_party_id` varchar(50) NOT NULL DEFAULT '' COMMENT '第三方平台的用户id',
  `three_party_email` varchar(50) NOT NULL DEFAULT '' COMMENT '第三方用户的邮箱',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '用户密码',
  `user_name` varchar(100) NOT NULL DEFAULT '' COMMENT '用户名',
  `app_id` int(11) NOT NULL DEFAULT '0' COMMENT '应用id',
  `background` varchar(500) NOT NULL DEFAULT '',
  `head_img` varchar(500) NOT NULL DEFAULT '' COMMENT '用户头像',
  `gender` tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户性别 1 男性 2 女性',
  `country_id` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '用户所在国家id',
  `country_name` varchar(50) NOT NULL DEFAULT '' COMMENT '用户所在的国家名称',
  `gold_num` decimal(10,0) NOT NULL DEFAULT '0' COMMENT '金币数量',
  `language_id` varchar(200) NOT NULL DEFAULT '' COMMENT '语言id',
  `language_name` varchar(500) NOT NULL DEFAULT '' COMMENT '语言名称，多个名称用，分割',
  `age` tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户的年龄',
  `birthday` date DEFAULT NULL COMMENT '用户出生日期',
  `platform_type` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '用户使用的平台类型1 ios 2 android',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户账号的登录类型 1 注册登录，2 facebook登录，3 ，google+登录',
  `pay_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否是付费用户',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '当前账号的状态 1.可用，2 禁用 ，3 被举报',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户信息的更新时间',
  `stone` decimal(10,0) NOT NULL DEFAULT '0' COMMENT '钻石数量',
  `stone_version` int(11) NOT NULL DEFAULT '0' COMMENT '钻石版本号',
  PRIMARY KEY (`id`),
  KEY `index_three_party_id` (`three_party_id`,`app_id`),
  KEY `index_appId_user_account` (`app_id`,`user_account`),
  KEY `index_id_password` (`id`,`password`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储用户基本信息';



CREATE TABLE `rc_quick_chat_config` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) NOT NULL DEFAULT '' COMMENT '配置类型',
  `config_text` varchar(500) DEFAULT NULL COMMENT '配置内容，json格式',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `has_del` smallint(1) NOT NULL DEFAULT '0' COMMENT '0 有效， 1 无效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `rc_quick_chat_user_pool` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id` int(10) NOT NULL DEFAULT '0' COMMENT '用户ID',
    `pool_id` tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户池ID',
    `cannel` tinyint(4) DEFAULT '0' COMMENT '用户渠道',
    `vip_level` tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户等级',
    `gender` tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户性别',
    `country_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户国家',
    `eroticism_behavior` tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户色情标识',
    `head_img` varchar(500) NOT NULL COMMENT '用户头像',
    `user_name` varchar(50) NOT NULL COMMENT '用户昵称',
    `age` tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户年龄',
    `app_id` int(11) NOT NULL DEFAULT '0' COMMENT '应用ID',
    `stone_version` int(11) NOT NULL DEFAULT '0' COMMENT '应用版本号',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
    `active_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户活跃时间',
    `status` tinyint(4) NOT NULL COMMENT '用户可用状态',
    `extend` varchar(50) DEFAULT NULL COMMENT '扩展字段',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4