use onepiece;CREATE TABLE `t_capture` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `map_id` int(11) DEFAULT NULL,
  `region_id` int(11) DEFAULT NULL,
  `entrance_id` int(11) unsigned DEFAULT '0',
  `status` varchar(45) DEFAULT NULL,
  `capture_total` int(11) unsigned DEFAULT '0',
  `face_data_not_null` int(11) unsigned DEFAULT '0',
  `capture_ratio` varchar(8) DEFAULT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `image` varchar(512) DEFAULT NULL,
  `video` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
