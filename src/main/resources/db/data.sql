
-- ----------------------------
-- Records of biz_category
-- ----------------------------
INSERT INTO biz_category VALUES (1,0,'常用推荐',null,1,null,1,'2022-12-09 21:37:32','2022-12-09 21:37:32');

-- ----------------------------
-- Records of biz_link
-- ----------------------------
INSERT INTO biz_website VALUES (1, 'CSDN', 'https://tarzan.blog.csdn.net', '洛阳泰山博客', 1, '1334512682@qq.com', '1334512682', 1, 1, '', 1,'2021-09-13 23:24:25', '2021-06-28 10:51:18');

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, 'SITE_NAME', '洛阳泰山', 1, '网站名称');
INSERT INTO `sys_config` VALUES (2, 'SITE_KWD', 'Java JavaScript Spring SpringBoot Vue React', 1, '网站关键字');
INSERT INTO `sys_config` VALUES (3, 'SITE_DESC', '个人博客网站，技术交流，经验分享。', 1, '网站描述');
INSERT INTO `sys_config` VALUES (4, 'SITE_PERSON_PIC', 'https://tse1-mm.cn.bing.net/th/id/OIP.Ups1Z8igjNjLuDfO38XhTgHaHa?pid=Api&rs=1', 1, '站长头像');
INSERT INTO `sys_config` VALUES (5, 'SITE_PERSON_NAME', '洛阳泰山', 1, '站长名称');
INSERT INTO `sys_config` VALUES (6, 'SITE_PERSON_DESC', '90后少年，热爱写bug，热爱编程，热爱学习，分享一些个人经验，共同学习，少走弯路。Talk is cheap, show me the code!', 1, '站长描述');
INSERT INTO `sys_config` VALUES (7, 'BAIDU_PUSH_URL', 'http://data.zz.baidu.com/urls?site=#&token=EjnfUGGJ1drKk4Oy', 1, '百度推送地址');
INSERT INTO `sys_config` VALUES (8, 'STATISTICS_CODE', '', 1, '统计代码');
-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0,'公告管理', '公告管理', '/notices', 'notices',1, 1, 'fas fa-volume-up', 1, '2021-07-13 15:05:47', '2021-02-27 10:53:14');
INSERT INTO `sys_menu` VALUES (2, 0,'用户管理', '用户管理', '/users', 'users',1, 1, 'fas fa-chess-queen', 1, '2021-07-13 15:05:47', '2021-02-27 10:53:14');
INSERT INTO `sys_menu` VALUES (3, 0,'网站管理', '网站管理', NULL, 'website', 0, 3, 'fas fa-columns', 1, '2021-07-24 15:44:23', '2021-04-19 19:08:46');
INSERT INTO `sys_menu` VALUES (4, 0,'运维管理', '运维管理', NULL, 'operation',0, 7, 'fas fa-people-carry', 1, '2021-07-06 15:19:26', '2021-04-19 19:09:59');
INSERT INTO `sys_menu` VALUES (5, 3,'基础信息', '基础设置', '/siteInfo', 'siteInfo', 1, 1, 'fas fa-chess-queen', 1, '2021-07-24 15:48:13', '2021-07-24 17:43:39');
INSERT INTO `sys_menu` VALUES (6, 3,'导航管理', '导航管理', '/websites', 'websites',1, 3, 'fas fa-chess-queen', 1, '2021-07-25 11:05:49', '2021-07-25 11:07:03');
INSERT INTO `sys_menu` VALUES (6, 3,'友链管理', '友链管理', '/links', 'links',1, 4, 'fas fa-chess-queen', 1, '2021-07-25 11:05:49', '2021-07-25 11:07:03');
INSERT INTO `sys_menu` VALUES (7, 3,'导航分类', '导航分类', '/categories', 'categories',1, 3, 'fas fa-chess-queen', 1, '2021-07-25 17:43:50', '2021-04-19 20:33:27');
INSERT INTO `sys_menu` VALUES (8, 3,'留言管理', '留言管理', '/comments', 'comments', 1, 4, 'fas fa-chess-queen', 1, '2021-08-10 09:44:41', '2021-09-19 15:44:13');
INSERT INTO `sys_menu` VALUES (9, 4,'数据监控', '数据监控', '/database/monitoring', 'database',1, 1, 'fas fa-chess-queen', 1, '2021-07-06 15:19:55', '2021-07-06 15:19:55');
INSERT INTO `sys_menu` VALUES (10,4,'数据备份', '数据备份', '/db/backup', 'dbBackup',1, 3, 'fas fa-clone', 1, '2021-07-20 16:01:23', '2021-07-20 16:01:23');
INSERT INTO `sys_menu` VALUES (11,4,'登录日志', '登录日志', '/login/logs', 'loginLogs', 1, 2, 'fas fa-th-list', 1, '2021-07-20 16:01:23', '2021-07-20 16:01:23');
INSERT INTO `sys_menu` VALUES (12,4,'错误日志', '错误日志', '/error/logs', 'errorLogs',1, 4, 'fas fa-skull', 1, '2021-07-20 16:01:23', '2021-07-20 16:01:23');
