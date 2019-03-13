CREATE TABLE `t_pv_uv` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `map_id` int(11) DEFAULT NULL,
  `map_pv` int(11) unsigned DEFAULT '0',
  `map_uv` int(11) unsigned DEFAULT NULL,
  `region_id` int(11) DEFAULT NULL,
  `region_pv` int(11) unsigned DEFAULT '0',
  `region_uv` int(11) unsigned DEFAULT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8