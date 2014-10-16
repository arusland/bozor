SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


CREATE TABLE IF NOT EXISTS `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `product_type_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_pe3bpejwrpkqc4ao43xrw0wpo` (`product_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;


CREATE TABLE IF NOT EXISTS `product_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` double DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `product_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_692vhya96i14fhg5bqqvohcii` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;


CREATE TABLE IF NOT EXISTS `product_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL UNIQUE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;


ALTER TABLE `product`
ADD CONSTRAINT `FK_pe3bpejwrpkqc4ao43xrw0wpo` FOREIGN KEY (`product_type_id`) REFERENCES `product_type` (`id`);

ALTER TABLE `product_item`
ADD CONSTRAINT `FK_692vhya96i14fhg5bqqvohcii` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`);

-- initial data - ru
insert into product_type(id, name) values(1, 'Не назначено');

-- initial data - en
-- insert into product_type(id, name) values(1, 'Not specified');