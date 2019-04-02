CREATE TABLE onepiece.t_pv_uv (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `map_id` int(11) DEFAULT NULL,
  `region_id` int(11) DEFAULT NULL,
  `entrance_id` int(11) unsigned DEFAULT '0',
  `status` varchar(45) DEFAULT NULL,
  `pv` int(11) unsigned DEFAULT '0',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `pv_accuracy_rate` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
