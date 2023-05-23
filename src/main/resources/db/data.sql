INSERT INTO SYS_CONFIG VALUES (5,'SITE_NAME','泰山导航',1,'网站名称');
INSERT INTO SYS_CONFIG VALUES (6,'SITE_KWD','Java JavaScript Spring SpringBoot Vue React',1,'网站关键字');
INSERT INTO SYS_CONFIG VALUES (7,'SITE_DESC','个人博客网站，技术交流，经验分享。',1,'网站描述');
INSERT INTO SYS_CONFIG VALUES (8,'SITE_PERSON_PIC','e29cb694ada82a0dbd874a06576ddf13',1,'站长头像');
INSERT INTO SYS_CONFIG VALUES (9,'SITE_PERSON_NAME','洛阳泰山',1,'站长名称');
INSERT INTO SYS_CONFIG VALUES (10,'SITE_PERSON_DESC','90后少年，热爱写bug，热爱编程，热爱学习，分享一些个人经验，共同学习，少走弯路。Talk is cheap, show me the code!',1,'站长描述');
INSERT INTO SYS_CONFIG VALUES (11,'BAIDU_PUSH_URL','http://data.zz.baidu.com/urls?site=#&token=EjnfUGGJ1drKk4Oy',1,'百度推送地址');
INSERT INTO SYS_CONFIG VALUES (11,'STATISTICS_CODE','',1,'统计代码');

INSERT INTO SYS_MENU VALUES (1,0,'仪表盘','仪表盘','/dashboard','dashboard',1,1,'fas fa-home',1,'2021-09-27 21:22:02','2021-02-27 10:53:14');
INSERT INTO SYS_MENU VALUES (2,0,'公告管理','公告管理','/notices','notices',1,1,'fas fa-volume-up',1,'2021-07-13 15:05:47','2021-02-27 10:53:14');
INSERT INTO SYS_MENU VALUES (3,0,'导航管理','导航管理',null,'nav',0,3,'fas fa-bookmark',1,'2021-07-24 15:44:23','2021-04-19 19:08:46');
INSERT INTO SYS_MENU VALUES (4,0,'审核管理','审核管理',null,'audit',0,4,'fas fa-feather-alt',1,'2021-07-24 15:44:23','2021-04-19 19:08:46');
INSERT INTO SYS_MENU VALUES (5,0,'网站管理','网站管理',null,'website',0,5,'fas fa-columns',1,'2021-07-24 15:44:23','2021-04-19 19:08:46');
INSERT INTO SYS_MENU VALUES (6,0,'运维管理','运维管理',null,'operation',0,6,'fas fa-people-carry',1,'2021-07-06 15:19:26','2021-04-19 19:09:59');
INSERT INTO SYS_MENU VALUES (7,5,'基础信息','基础设置','/siteInfo','siteInfo',1,1,'fas fa-chess-queen',1,'2021-07-24 15:48:13','2021-07-24 17:43:39');
INSERT INTO SYS_MENU VALUES (8,5,'用户管理','用户管理','/users','users',1,2,'fas fa-user',1,'2021-07-13 15:05:47','2021-02-27 10:53:14');
INSERT INTO SYS_MENU VALUES (9,3,'导航分类','导航分类','/categories','categories',1,3,'fas fa-sitemap',1,'2021-07-25 17:43:50','2021-04-19 20:33:27');
INSERT INTO SYS_MENU VALUES (10,3,'导航网站','导航网站','/websites','websites',1,3,'fas fa-chess-queen',1,'2021-07-25 11:05:49','2021-07-25 11:07:03');
INSERT INTO SYS_MENU VALUES (11,4,'网站审核','网站审核','/websites/review','brokenSites',1,4,'fas fa-chess-queen',1,'2021-07-25 11:05:49','2021-07-25 11:07:03');
INSERT INTO SYS_MENU VALUES (12,4,'友链审核','友链审核','/links','links',1,4,'fas fa-link',1,'2021-07-25 11:05:49','2021-07-25 11:07:03');
INSERT INTO SYS_MENU VALUES (13,4,'评论审核','评论审核','/comments','comments',1,4,'fas fa-comment',1,'2021-08-10 09:44:41','2021-09-19 15:44:13');
INSERT INTO SYS_MENU VALUES (14,6,'数据监控','数据监控','/database/monitoring','database',1,1,'fas fa-database',1,'2021-07-06 15:19:55','2021-07-06 15:19:55');
INSERT INTO SYS_MENU VALUES (15,6,'数据备份','数据备份','/db/backup','dbBackup',1,3,'fas fa-clone',1,'2021-07-20 16:01:23','2021-07-20 16:01:23');
INSERT INTO SYS_MENU VALUES (16,6,'登录日志','登录日志','/login/logs','loginLogs',1,2,'fas fa-th-list',1,'2021-07-20 16:01:23','2021-07-20 16:01:23');
INSERT INTO SYS_MENU VALUES (17,6,'错误日志','错误日志','/error/logs','errorLogs',1,4,'fas fa-skull',1,'2021-07-20 16:01:23','2021-07-20 16:01:23');
