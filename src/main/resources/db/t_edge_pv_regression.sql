CREATE TABLE onepiece.`t_edge_pv_regression` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `video` varchar(256) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `pv` int(11) unsigned DEFAULT '0',
  `expect_pv` int(11) unsigned DEFAULT '0',
  `pv_accuracy_rate` varchar(8) DEFAULT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `image` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;