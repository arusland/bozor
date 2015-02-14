CREATE TABLE IF NOT EXISTS `persistent_token` (
  `series` varchar(255) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `token_value` varchar(255) NOT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  `ip_address` varchar(39) DEFAULT NULL,
  `token_date` datetime NOT NULL,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;