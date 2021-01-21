CREATE TABLE onepiece.`t_monitor_online_scope_device` (
  `day` varchar(16) NOT NULL,
  `scope_id` int(11) NOT NULL,
  `scope_name` varchar(64) DEFAULT NULL COMMENT '店铺名称',
  `device_id` varchar(64) NOT NULL,
  `device_name` varchar(64) DEFAULT NULL COMMENT '设备名称',
  `device_status` varchar(16) DEFAULT NULL COMMENT '设备状态',
  `device_type` varchar(16) DEFAULT NULL COMMENT '设备类型',
  `device_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `edit_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`day`,`scope_id`,`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
