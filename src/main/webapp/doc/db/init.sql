/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.5.46 : Database - sys_core
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`sys_core` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `sys_core`;

/*Table structure for table `f_notice` */

DROP TABLE IF EXISTS `f_notice`;

CREATE TABLE `f_notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(255) DEFAULT NULL COMMENT '公共标题',
  `content` text COMMENT '公告内容',
  `orde` int(255) DEFAULT '0' COMMENT '排序',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `publish_user` varchar(32) DEFAULT NULL COMMENT '发布人',
  `state` int(11) DEFAULT '0' COMMENT '状态0 新建 1 发布  2 删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `f_notice` */

/*Table structure for table `s_resource` */

DROP TABLE IF EXISTS `s_resource`;

CREATE TABLE `s_resource` (
  `resource_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '资源主键',
  `resource_name` varchar(32) DEFAULT NULL,
  `description` text,
  `resource_url` varchar(255) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `permission` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `state` int(11) DEFAULT '0',
  `type` int(11) DEFAULT '0',
  `sys_type` varchar(32) NOT NULL COMMENT '所属系统',
  PRIMARY KEY (`resource_id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;

/*Data for the table `s_resource` */

insert  into `s_resource`(`resource_id`,`resource_name`,`description`,`resource_url`,`parent_id`,`icon`,`permission`,`create_time`,`update_time`,`state`,`type`,`sys_type`) values (1,'通用后台权限管理系统','','',NULL,'','common:Sys','2015-10-22 14:10:36','2015-11-22 12:25:48',0,1,'SYS_CORE'),(2,'系统管理','系统管理','http://sys.dongtian.com:8080/sys/admin/index.do',1,'fa-gears','admin:index','2015-10-22 14:10:36','2015-11-22 12:25:48',0,0,'SYS_CORE'),(17,'用户管理','用户管理','http://sys.dongtian.com:8080/sys/admin/user/index.do',2,'fa-users','admin:user:index','2015-10-29 16:35:07','2015-11-22 12:25:48',0,2,'SYS_CORE'),(18,'角色管理','角色管理','http://sys.dongtian.com:8080/sys/admin/role/index.do',2,'fa-user-plus','admin:role:index','2015-10-29 16:35:40','2015-11-22 12:25:48',0,2,'SYS_CORE'),(19,'权限资源管理','权限资源管理','http://sys.dongtian.com:8080/sys/admin/resource/index.do',2,'fa-user-secret','admin:resource:index','2015-10-29 16:36:09','2015-11-22 12:25:48',0,2,'SYS_CORE'),(20,'新增用户','新增用户','xxx',17,'','admin:user:addUser','2015-10-29 16:36:29','2015-11-22 12:25:48',0,4,'SYS_CORE'),(21,'修改用户','修改用户','xx',17,'','admin:user:editUser','2015-10-29 16:36:58','2015-11-22 12:25:48',0,4,'SYS_CORE'),(22,'删除用户','删除用户','ss',17,'','admin:user:deleteUser','2015-10-29 16:37:23','2015-11-22 12:25:48',0,4,'SYS_CORE'),(23,'新增角色','新增角色','xx',18,'','admin:role:addRole','2015-10-29 16:38:04','2015-11-22 12:25:48',0,4,'SYS_CORE'),(24,'修改角色','修改角色','xxx',18,'','admin:role:editRole','2015-10-29 16:38:27','2015-11-22 12:25:48',0,4,'SYS_CORE'),(25,'删除权限资源','删除权限资源','ddd',19,'','admin:resource:deleteResource','2015-10-29 16:39:03','2015-11-22 12:25:48',0,4,'SYS_CORE'),(26,'删除角色','删除角色','xx',18,'','admin:role:deleteRole','2015-10-29 16:39:27','2015-11-22 12:25:48',0,4,'SYS_CORE'),(27,'修改权限资源','修改权限资源','xxx',19,'','admin:resource:editResource','2015-10-29 16:39:53','2015-11-22 12:25:48',0,4,'SYS_CORE'),(28,'新增权限权限资源','新增权限资源','xxx',19,'','admin:resource:addResource','2015-10-29 16:40:27','2015-11-22 12:25:48',0,4,'SYS_CORE'),(29,'商品管理系统','商品管理','xxx',NULL,'','xxxxx','2015-10-29 16:41:08','2015-10-29 16:41:08',0,0,'PRODUCT_CORE'),(31,'商品管理','商品管理','xxx',29,'','xxxxx','2015-10-29 16:42:38','2015-10-29 16:42:38',0,1,'PRODUCT_CORE'),(34,'商品审核管理','商品审核管理','xxxx',31,'','xxxxxx','2015-10-29 16:50:16','2015-10-29 16:50:16',0,2,'PRODUCT_CORE'),(35,'商品审核','商品审核','dxx',34,'','checkProcut','2015-10-29 16:51:07','2015-10-29 16:51:07',0,3,'PRODUCT_CORE'),(36,'商品发布管理','商品发布管理','xxx',31,'','productPublish','2015-10-29 16:51:48','2015-10-29 16:51:48',0,2,'PRODUCT_CORE'),(37,'商品发布','商品发布','xx',36,'','xxxxxxx','2015-10-29 16:52:11','2015-10-29 16:52:11',0,3,'PRODUCT_CORE'),(38,'角色授权','角色授权','...',18,'','admin:role:settingResource','2015-11-22 12:21:48','2015-11-22 12:25:48',0,3,'SYS_CORE'),(39,'设置角色','设置角色','xxx',17,'','admin:user:settingRole','2015-11-22 12:22:55','2015-11-22 12:25:48',0,3,'SYS_CORE'),(40,'个人云盘系统','个人云盘系统','',NULL,'','yun:index','2015-11-22 13:09:10','2015-11-22 13:09:10',0,0,'CLOUD_YUN'),(41,'个人云盘','个人云盘','',40,'fa-cloud','cloud','2015-11-22 13:11:16','2015-11-22 13:11:16',0,1,'CLOUD_YUN'),(43,'全部文件','','',41,'fa-file','cloud:allFile','2015-11-22 13:13:16','2015-11-22 13:13:16',0,2,'CLOUD_YUN'),(44,'我的图片','','',41,'fa-photo','cloud:picture','2015-11-22 13:14:53','2015-11-22 13:14:53',0,2,'CLOUD_YUN'),(45,'文档文档','','',41,'fa-file-text','cloud:document','2015-11-22 13:15:20','2015-11-22 13:15:20',0,2,'CLOUD_YUN'),(46,'我的视频','','',41,'fa-video-camera','cloud:video','2015-11-22 13:16:02','2015-11-22 13:16:02',0,2,'CLOUD_YUN'),(47,'我的种子','','',41,'fa-rocket','cloud:bt','2015-11-22 13:16:36','2015-11-22 13:16:36',0,2,'CLOUD_YUN'),(48,'我的音乐','','',41,'fa-music','cloud:music','2015-11-22 13:17:45','2015-11-22 13:17:45',0,2,'CLOUD_YUN'),(49,'我的回收站','','',41,'fa-recycle','cloud:trash','2015-11-22 13:19:14','2015-11-22 13:19:14',0,2,'CLOUD_YUN'),(50,'水果网站维护系统','水果网站维护系统','http://fruit.dongtian.com:8280/fruit/admin/index.do',56,'fa-users','admin:fruit:index','2015-11-26 13:36:27','2015-11-26 13:36:27',0,1,'sys_fruit'),(51,'最新公告管理','最新公告管理','http://fruit.dongtian.com:8280/fruit/admin/notice/index.do',50,'fa-users','admin:fruit:notice:index','2015-11-26 13:38:06','2015-11-26 13:38:06',0,2,'sys_fruit'),(52,'新闻资讯管理','','http://fruit.dongtian.com:8280/fruit/admin/news/index.do',50,'fa-users','admin:fruit:news:index','2015-11-26 13:41:01','2015-11-26 13:41:01',0,2,'sys_fruit'),(53,'网站轮播图管理','','http://fruit.dongtian.com:8280/fruit/admin/carousel/index.do',50,'','admin:fruit:carousel:index','2015-11-26 13:43:18','2015-11-26 13:43:18',0,2,'sys_fruit'),(54,'产品展示中心管理','','http://fruit.dongtian.com:8280/fruit/admin/product/index.do',50,'','admin:fruit:product:index','2015-11-26 13:45:51','2015-11-26 13:45:51',0,2,'sys_fruit'),(55,'关于我们管理','','http://fruit.dongtian.com:8280/fruit/admin/aboutUs/index.do',50,'','admin:fruit:about:index','2015-11-26 13:47:04','2015-11-26 13:47:04',0,2,'sys_fruit'),(56,'公司门户网站管理系统','公司门户网站管理系统',NULL,NULL,NULL,'admin:fruit',NULL,NULL,0,0,'sys_fruit');

/*Table structure for table `s_role` */

DROP TABLE IF EXISTS `s_role`;

CREATE TABLE `s_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(32) DEFAULT NULL,
  `state` int(11) DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `s_role` */

insert  into `s_role`(`role_id`,`role_name`,`state`,`create_time`,`update_time`,`description`) values (1,'超级管理员',0,'2015-10-29 12:08:13','2015-10-29 12:08:21','我是超级管理员'),(3,'游客',0,'2015-10-29 12:08:19','2015-10-29 12:08:27','我是游客'),(4,'巡航员',0,'2015-10-29 13:57:27','2015-11-21 14:17:07','我是巡航员');

/*Table structure for table `s_role_resource` */

DROP TABLE IF EXISTS `s_role_resource`;

CREATE TABLE `s_role_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL,
  `resource_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=468 DEFAULT CHARSET=utf8;

/*Data for the table `s_role_resource` */

insert  into `s_role_resource`(`id`,`role_id`,`resource_id`) values (347,2,56),(348,2,50),(349,2,51),(350,2,52),(351,2,53),(352,2,54),(353,2,55),(354,2,1),(355,2,2),(356,2,17),(357,2,20),(358,2,21),(359,2,22),(360,2,39),(361,2,18),(362,2,23),(363,2,24),(364,2,26),(365,2,38),(366,2,19),(367,2,25),(368,2,27),(369,2,28),(370,2,29),(371,2,31),(372,2,34),(373,2,35),(374,2,36),(375,2,37),(376,2,40),(377,2,41),(378,2,43),(379,2,44),(380,2,45),(381,2,46),(382,2,47),(383,2,48),(384,2,49),(385,3,40),(386,3,41),(387,3,43),(388,3,44),(389,3,45),(390,3,46),(391,3,47),(392,3,48),(393,3,49),(394,4,56),(395,4,50),(396,4,51),(397,4,52),(398,4,53),(399,4,54),(400,4,55),(401,4,1),(402,4,2),(403,4,17),(404,4,20),(405,4,21),(406,4,22),(407,4,39),(408,4,18),(409,4,23),(410,4,24),(411,4,26),(412,4,38),(413,4,19),(414,4,25),(415,4,27),(416,4,28),(417,4,29),(418,4,31),(419,4,34),(420,4,35),(421,4,36),(422,4,37),(423,4,40),(424,4,41),(425,4,43),(426,4,44),(427,4,45),(428,4,46),(429,4,47),(430,4,48),(431,4,49),(432,1,56),(433,1,50),(434,1,51),(435,1,52),(436,1,53),(437,1,54),(438,1,55),(439,1,1),(440,1,2),(441,1,17),(442,1,20),(443,1,21),(444,1,22),(445,1,18),(446,1,23),(447,1,24),(448,1,26),(449,1,19),(450,1,25),(451,1,27),(452,1,28),(453,1,29),(454,1,31),(455,1,34),(456,1,35),(457,1,36),(458,1,37),(459,1,40),(460,1,41),(461,1,43),(462,1,44),(463,1,45),(464,1,46),(465,1,47),(466,1,48),(467,1,49);

/*Table structure for table `s_user` */

DROP TABLE IF EXISTS `s_user`;

CREATE TABLE `s_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户主键',
  `user_name` varchar(32) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `state` int(11) DEFAULT '0',
  `age` int(11) DEFAULT '0',
  `sex` int(11) DEFAULT '0',
  `address` varchar(255) DEFAULT NULL,
  `mobile` varchar(32) DEFAULT NULL,
  `email` varchar(32) DEFAULT NULL,
  `nick_name` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `reg_time` datetime DEFAULT NULL,
  `user_type` int(11) DEFAULT '0',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `s_user` */

insert  into `s_user`(`user_id`,`user_name`,`password`,`state`,`age`,`sex`,`address`,`mobile`,`email`,`nick_name`,`create_time`,`update_time`,`reg_time`,`user_type`) values (2,'admin','123456',0,25,0,'河南省商丘市','15910860051','466862016@qq.com','超级管理员','2015-10-16 13:36:32','2015-10-16 13:36:32','2015-10-16 13:36:32',0),(3,'dongtian','123456',0,0,0,'北京市朝阳区','13433333333','333@qq.com','一般管理员','2015-10-16 13:45:14','2015-10-16 13:45:14','2015-10-16 13:45:14',0),(5,'呜呜呜呜呜呜','111111',1,0,0,NULL,'15910860050','111@qq.cc','顶顶顶的','2015-10-22 11:47:03','2015-10-22 11:47:03','2015-10-22 11:47:03',0),(6,'xxxxx','1234',0,0,0,'河南省','15610843321','4555@qq.com','xxxxx1','2015-11-20 15:28:27','2015-11-21 12:29:38','2015-11-20 15:28:27',0);

/*Table structure for table `s_user_attempts` */

DROP TABLE IF EXISTS `s_user_attempts`;

CREATE TABLE `s_user_attempts` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(32) DEFAULT NULL COMMENT '用户名',
  `attempts` int(11) DEFAULT '0' COMMENT '尝试登录次数',
  `last_update_time` datetime DEFAULT NULL COMMENT '最后登录尝试次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `s_user_attempts` */

insert  into `s_user_attempts`(`id`,`user_name`,`attempts`,`last_update_time`) values (1,'jack',0,'2015-05-26 12:01:46'),(2,'admin',3,'2015-05-26 14:20:26'),(3,'admin',1,'2015-11-05 14:24:35');

/*Table structure for table `s_user_role` */

DROP TABLE IF EXISTS `s_user_role`;

CREATE TABLE `s_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

/*Data for the table `s_user_role` */

insert  into `s_user_role`(`id`,`user_id`,`role_id`) values (15,2,1),(16,2,2),(17,2,3),(18,2,4),(19,5,4),(20,5,3),(21,5,1),(22,3,1),(24,6,4),(25,6,1);

/* Procedure structure for procedure `t` */

/*!50003 DROP PROCEDURE IF EXISTS  `t` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`cabbage`@`%` PROCEDURE `t`()
BEGIN
DECLARE i INT DEFAULT 0;
  WHILE i < 4000000 DO
  INSERT INTO test1(CODE,NAME,datesa)
  VALUES('001','小王','2011-07-15');
  SET i = i + 1;
  END WHILE;
    END */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
