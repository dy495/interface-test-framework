use onepiece; CREATE TABLE `t_monitor_online_algorithm_merge` (
  `scope` varchar(128) NOT NULL COMMENT 'scope id',
  `scope_name` varchar(128) DEFAULT NULL,
  `date` varchar(64) NOT NULL COMMENT '统计日期，格式：year-month-day，月和日都是2位，如 2019-01-01',
  `person_num` int(11) DEFAULT NULL,
  `record_sum` int(11) DEFAULT NULL,
  `member_percent` float DEFAULT NULL,
  `avg_record` float DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `alarm` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`scope`,`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;