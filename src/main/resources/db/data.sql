
-- ----------------------------
-- Records of biz_category
-- ----------------------------
INSERT INTO biz_category VALUES (1,0,'常用推荐',null,1,null,1,'2022-12-09 21:37:32','2022-12-09 21:37:32');
INSERT INTO biz_category VALUES (9,0,'社区资讯',null,2,null,1,'2022-12-09 21:37:50','2022-12-09 21:37:50');
INSERT INTO biz_category VALUES (10,0,'灵感采集',null,3,null,1,'2022-12-09 21:38:15','2022-12-09 21:38:15');
INSERT INTO biz_category VALUES (11,0,'素材资源',null,4,null,1,'2022-12-09 21:39:08','2022-12-09 21:39:08');
INSERT INTO biz_category VALUES (12,0,'常用工具',null,5,null,1,'2022-12-09 21:39:30','2022-12-09 21:39:30');
INSERT INTO biz_category VALUES (13,0,'学习教程',null,6,null,1,'2022-12-09 21:39:50','2022-12-09 21:39:50');
INSERT INTO biz_category VALUES (14,0,'UED团队',null,7,null,1,'2022-12-09 21:40:35','2022-12-09 21:40:35');
INSERT INTO biz_category VALUES (15,10,'发现产品',null,1,null,1,'2022-12-09 21:41:23','2022-12-09 21:41:23');
INSERT INTO biz_category VALUES (16,10,'界面灵感',null,2,null,1,'2022-12-09 21:41:55','2022-12-09 21:42:06');
INSERT INTO biz_category VALUES (17,10,'网页灵感',null,3,null,1,'2022-12-09 21:42:36','2022-12-09 21:42:36');
INSERT INTO biz_category VALUES (18,11,'图标素材',null,1,null,1,'2022-12-09 21:45:05','2022-12-09 21:45:05');
INSERT INTO biz_category VALUES (19,11,'LOGO设计',null,2,null,1,'2022-12-09 21:45:43','2022-12-09 21:45:43');
INSERT INTO biz_category VALUES (20,11,'平面素材',null,3,null,1,'2022-12-09 21:52:53','2022-12-09 21:52:53');
INSERT INTO biz_category VALUES (21,11,'UI资源',null,4,null,1,'2022-12-09 21:53:15','2022-12-09 21:53:15');
INSERT INTO biz_category VALUES (22,11,'Sketch资源',null,5,null,1,'2022-12-09 21:53:58','2022-12-09 22:05:43');
INSERT INTO biz_category VALUES (23,11,'字体资源',null,6,null,1,'2022-12-09 21:54:38','2022-12-09 22:06:03');
INSERT INTO biz_category VALUES (24,11,'Mockup',null,7,null,1,'2022-12-09 21:55:25','2022-12-09 21:55:25');
INSERT INTO biz_category VALUES (25,11,'摄影图库',null,8,null,1,'2022-12-09 21:55:52','2022-12-09 21:55:52');
INSERT INTO biz_category VALUES (26,11,'PPT资源',null,9,null,1,'2022-12-09 21:56:58','2022-12-09 21:56:58');
INSERT INTO biz_category VALUES (27,12,'图形创意',null,1,null,1,'2022-12-09 21:58:06','2022-12-09 21:58:06');
INSERT INTO biz_category VALUES (28,12,'界面设计',null,2,null,1,'2022-12-09 21:58:21','2022-12-09 21:58:21');
INSERT INTO biz_category VALUES (29,12,'交互动效',null,3,null,1,'2022-12-09 21:58:40','2022-12-09 21:58:40');
INSERT INTO biz_category VALUES (30,12,'在线配色',null,4,null,1,'2022-12-09 21:59:14','2022-12-09 21:59:14');
INSERT INTO biz_category VALUES (31,12,'Chrome插件',null,6,null,1,'2022-12-09 21:59:33','2022-12-09 22:03:07');
INSERT INTO biz_category VALUES (32,13,'设计规范',null,1,null,1,'2022-12-09 22:00:13','2022-12-09 22:02:10');
INSERT INTO biz_category VALUES (33,13,'视频教程',null,2,null,1,'2022-12-09 22:00:34','2022-12-09 22:04:52');
INSERT INTO biz_category VALUES (34,12,'在线工具',null,5,null,1,'2022-12-09 22:02:30','2022-12-09 22:02:40');
INSERT INTO biz_category VALUES (35,13,'设计文章',null,3,null,1,'2022-12-09 22:03:45','2022-12-09 22:03:45');
INSERT INTO biz_category VALUES (36,13,'设计电台',null,4,null,1,'2022-12-09 22:04:18','2022-12-09 22:04:18');
INSERT INTO biz_category VALUES (37,13,'交互设计',null,5,null,1,'2022-12-09 22:04:35','2022-12-09 22:04:35');

-- ----------------------------
-- Records of biz_link
-- ----------------------------
INSERT INTO biz_website VALUES (1, 'CSDN', 'https://tarzan.blog.csdn.net', '洛阳泰山博客', 1, '1334512682@qq.com', '1334512682', 1, 1, '', 1,'2021-09-13 23:24:25', '2021-06-28 10:51:18');

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, 'CLOUD_STORAGE_CONFIG', '{\"type\":4,\"qiniuDomain\":\"http://xxx.com\",\"qiniuPrefix\":\"img/blog\",\"qiniuAccessKey\":\"xxxAccessKey\",\"qiniuSecretKey\":\"xxxSecretKey\",\"qiniuBucketName\":\"blog\",\"aliyunDomain\":\"\",\"aliyunPrefix\":\"\",\"aliyunEndPoint\":\"\",\"aliyunAccessKeyId\":\"\",\"aliyunAccessKeySecret\":\"\",\"aliyunBucketName\":\"\",\"qcloudDomain\":\"\",\"qcloudPrefix\":\"\",\"qcloudSecretId\":\"\",\"qcloudSecretKey\":\"\",\"qcloudBucketName\":\"\",\"qcloudRegion\":\"\"}', 1, '云存储配置信息');
INSERT INTO `sys_config` VALUES (5, 'SITE_NAME', '洛阳泰山', 1, '网站名称');
INSERT INTO `sys_config` VALUES (6, 'SITE_KWD', 'Java JavaScript Spring SpringBoot Vue React', 1, '网站关键字');
INSERT INTO `sys_config` VALUES (7, 'SITE_DESC', '个人博客网站，技术交流，经验分享。', 1, '网站描述');
INSERT INTO `sys_config` VALUES (8, 'SITE_PERSON_PIC', 'https://tse1-mm.cn.bing.net/th/id/OIP.Ups1Z8igjNjLuDfO38XhTgHaHa?pid=Api&rs=1', 1, '站长头像');
INSERT INTO `sys_config` VALUES (9, 'SITE_PERSON_NAME', '洛阳泰山', 1, '站长名称');
INSERT INTO `sys_config` VALUES (10, 'SITE_PERSON_DESC', '90后少年，热爱写bug，热爱编程，热爱学习，分享一些个人经验，共同学习，少走弯路。Talk is cheap, show me the code!', 1, '站长描述');
INSERT INTO `sys_config` VALUES (11, 'BAIDU_PUSH_URL', 'http://data.zz.baidu.com/urls?site=#&token=EjnfUGGJ1drKk4Oy', 1, '百度推送地址');
INSERT INTO `sys_config` VALUES (11, 'STATISTICS_CODE', '', 1, '统计代码');
-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (2, '用户管理', '用户管理', '/users', 'users', 0, 1, 1, 'fas fa-chess-queen', 1, '2021-07-13 15:05:47', '2021-02-27 10:53:14');
INSERT INTO `sys_menu` VALUES (3, '网站管理', '网站管理', NULL, 'website', 0, 0, 3, 'fas fa-columns', 1, '2021-07-24 15:44:23', '2021-04-19 19:08:46');
INSERT INTO `sys_menu` VALUES (4, '运维管理', '运维管理', NULL, 'operation', 0, 0, 7, 'fas fa-people-carry', 1, '2021-07-06 15:19:26', '2021-04-19 19:09:59');
INSERT INTO `sys_menu` VALUES (5, '基础信息', '基础设置', '/siteInfo', 'siteInfo', 3, 1, 1, 'fas fa-chess-queen', 1, '2021-07-24 15:48:13', '2021-07-24 17:43:39');
INSERT INTO `sys_menu` VALUES (6, '导航管理', '导航管理', '/websites', 'websites', 3, 1, 3, 'fas fa-chess-queen', 1, '2021-07-25 11:05:49', '2021-07-25 11:07:03');
INSERT INTO `sys_menu` VALUES (6, '友链管理', '友链管理', '/links', 'links', 3, 1, 4, 'fas fa-chess-queen', 1, '2021-07-25 11:05:49', '2021-07-25 11:07:03');
INSERT INTO `sys_menu` VALUES (7, '导航分类', '导航分类', '/categories', 'categories', 3, 1, 3, 'fas fa-chess-queen', 1, '2021-07-25 17:43:50', '2021-04-19 20:33:27');
INSERT INTO `sys_menu` VALUES (8, '留言管理', '留言管理', '/comments', 'comments', 3, 1, 4, 'fas fa-chess-queen', 1, '2021-08-10 09:44:41', '2021-09-19 15:44:13');
INSERT INTO `sys_menu` VALUES (9, '数据监控', '数据监控', '/database/monitoring', 'database', 4, 1, 1, 'fas fa-chess-queen', 1, '2021-07-06 15:19:55', '2021-07-06 15:19:55');
INSERT INTO `sys_menu` VALUES (10, '数据备份', '数据备份', '/db/backup', 'dbBackup', 4, 1, 3, 'fas fa-clone', 1, '2021-07-20 16:01:23', '2021-07-20 16:01:23');
INSERT INTO `sys_menu` VALUES (11, '登录日志', '登录日志', '/login/logs', 'loginLogs', 4, 1, 2, 'fas fa-th-list', 1, '2021-07-20 16:01:23', '2021-07-20 16:01:23');
INSERT INTO `sys_menu` VALUES (12, '错误日志', '错误日志', '/error/logs', 'errorLogs', 4, 1, 4, 'fas fa-skull', 1, '2021-07-20 16:01:23', '2021-07-20 16:01:23');
