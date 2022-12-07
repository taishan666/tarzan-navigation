
-- ----------------------------
-- Records of biz_category
-- ----------------------------
INSERT INTO `biz_category` VALUES (1, 0, '前端技术', '主要收集、整理的基础前端类文章', 1, 'fa fa-css3', 1, '2021-01-14 21:34:54', '2021-07-25 17:57:50');
INSERT INTO `biz_category` VALUES (2, 0, '后端技术', '网站中记录的后端类文章，包括Java、Spring、SpringBoot、MySQL、大数据和其他在日常工作学习中所用的后端技术', 10, 'fa fa-coffee', 1, '2021-01-14 21:34:57', '2021-09-14 15:28:24');
INSERT INTO `biz_category` VALUES (3, 0, '其他文章', '记录网站建设以及日常工作、学习中的闲言碎语', 50, 'fa fa-folder-open-o', 1, '2021-01-20 22:28:03', '2021-09-14 15:28:50');
INSERT INTO `biz_category` VALUES (4, 0, '领悟', '记录个人生活等文章', 40, NULL, 1, '2021-08-02 11:20:26', '2021-09-14 15:28:38');
INSERT INTO `biz_category` VALUES (5, 3, '总结', '总结反思', 1, NULL, 1, '2021-09-11 11:28:15', '2021-09-11 11:28:15');
INSERT INTO `biz_category` VALUES (6, 0, '工具资源', '开发工具、服务端工具、中间件', 20, NULL, 1, '2021-09-14 15:26:39', '2021-09-14 15:28:28');

-- ----------------------------
-- Records of biz_link
-- ----------------------------
INSERT INTO `biz_link`(`id`, `name`, `url`, `description`, `img`, `email`, `qq`, `status`, `origin`, `remark`, `create_time`, `update_time`) VALUES (1, 'CSDN', 'https://tarzan.blog.csdn.net', '洛阳泰山博客', 'https://profile-avatar.csdnimg.cn/d3fa448cb5d24ebf850b107df8fee498_weixin_40986713.jpg', '1334512682@qq.com', '1334512682', 1, 1, '', '2021-09-13 23:24:25', '2021-06-28 10:51:18');

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

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '工作台', '工作台', '/dashboard', 'dashboard', 0, 1, 1, 'fas fa-home', 1, '2021-09-27 21:22:02', '2021-02-27 10:53:14');
INSERT INTO `sys_menu` VALUES (3, '用户管理', '用户管理', '/users', 'users', 0, 1, 1, 'fas fa-chess-queen', 1, '2021-07-13 15:05:47', '2021-02-27 10:53:14');
INSERT INTO `sys_menu` VALUES (4, '列表查询', '用户列表查询', '/user/list', 'user:list', 3, 2, 0, NULL, 1, '2021-07-13 15:09:24', '2021-10-09 05:38:29');
INSERT INTO `sys_menu` VALUES (5, '新增', '新增用户', '/user/add', 'user:add', 3, 2, 0, NULL, 1, '2021-07-13 15:06:50', '2021-02-28 17:58:46');
INSERT INTO `sys_menu` VALUES (6, '编辑', '编辑用户', '/user/edit', 'user:edit', 3, 2, 0, NULL, 1, '2021-07-13 15:08:03', '2021-02-27 10:53:14');
INSERT INTO `sys_menu` VALUES (7, '删除', '删除用户', '/user/delete', 'user:delete', 3, 2, 0, NULL, 1, '2021-07-13 15:08:42', '2021-02-27 10:53:14');
INSERT INTO `sys_menu` VALUES (8, '批量删除', '批量删除用户', '/user/batch/delete', 'user:batchDelete', 3, 2, 0, '', 1, '2021-07-11 01:53:09', '2021-07-11 01:53:09');
INSERT INTO `sys_menu` VALUES (9, '分配角色', '分配角色', '/user/assign/role', 'user:assignRole', 3, 2, 0, NULL, 1, '2021-07-13 15:09:24', '2021-10-09 05:38:29');
INSERT INTO `sys_menu` VALUES (22, '运维管理', '运维管理', NULL, NULL, 0, 0, 7, 'fas fa-people-carry', 1, '2021-07-06 15:19:26', '2021-04-19 19:09:59');
INSERT INTO `sys_menu` VALUES (23, '数据监控', '数据监控', '/database/monitoring', 'database', 22, 1, 1, 'fas fa-chess-queen', 1, '2021-07-06 15:19:55', '2021-07-06 15:19:55');
INSERT INTO `sys_menu` VALUES (35, '网站管理', '网站管理', NULL, NULL, 0, 0, 3, 'fas fa-columns', 1, '2021-07-24 15:44:23', '2021-04-19 19:08:46');
INSERT INTO `sys_menu` VALUES (36, '基础信息', '基础设置', '/siteInfo', 'siteInfo', 35, 1, 1, 'fas fa-chess-queen', 1, '2021-07-24 15:48:13', '2021-07-24 17:43:39');
INSERT INTO `sys_menu` VALUES (37, '保存', '基础设置-保存', '/siteInfo/save', 'siteInfo:save', 36, 2, 0, '', 1, '2021-07-24 15:49:12', '2021-07-24 15:49:12');
INSERT INTO `sys_menu` VALUES (38, '系统公告', '系统公告', '/notifies', 'notifies', 35, 1, 2, 'fas fa-chess-queen', 0, '2021-07-24 23:40:45', '2021-09-13 12:34:18');
INSERT INTO `sys_menu` VALUES (39, '查询', '系统公告-查询', '/notify/list', 'notify:list', 38, 2, 0, '', 0, '2021-07-24 23:41:30', '2021-09-13 12:33:19');
INSERT INTO `sys_menu` VALUES (40, '新增', '系统公告-新增', '/notify/add', 'notify:add', 38, 2, 0, '', 0, '2021-07-24 23:42:20', '2021-09-13 12:33:26');
INSERT INTO `sys_menu` VALUES (42, '编辑', '系统公告-编辑', '/notify/edit', 'notify:edit', 38, 2, 0, '', 0, '2021-07-24 23:44:39', '2021-09-13 12:33:52');
INSERT INTO `sys_menu` VALUES (43, '删除', '系统公告-删除', '/notify/delete', 'notify:delete', 38, 2, 0, '', 0, '2021-07-24 23:45:27', '2021-09-13 12:33:57');
INSERT INTO `sys_menu` VALUES (44, '批量删除', '批量删除公告', '/notify/batch/delete', 'notify:batchDelete', 38, 2, 0, '', 0, '2021-07-24 23:46:25', '2021-09-13 12:34:02');
INSERT INTO `sys_menu` VALUES (45, '友链管理', '友情链接', '/links', 'links', 35, 1, 3, 'fas fa-chess-queen', 1, '2021-07-25 11:05:49', '2021-07-25 11:07:03');
INSERT INTO `sys_menu` VALUES (46, '查询', '友链-查询', '/link/list', 'link:list', 45, 2, 0, '', 1, '2021-07-25 11:06:44', '2021-07-25 11:06:44');
INSERT INTO `sys_menu` VALUES (47, '新增', '友链-新增', '/link/add', 'link:add', 45, 2, 0, '', 1, '2021-07-25 11:07:46', '2021-07-25 11:07:46');
INSERT INTO `sys_menu` VALUES (48, '编辑', '友链-编辑', '/link/edit', 'link:edit', 45, 2, 0, '', 1, '2021-07-25 11:08:21', '2021-07-25 11:08:21');
INSERT INTO `sys_menu` VALUES (49, '删除', '友链-删除', '/link/delete', 'link:delete', 45, 2, 0, '', 1, '2021-07-25 11:08:53', '2021-07-25 11:08:53');
INSERT INTO `sys_menu` VALUES (50, '批量删除', '友链-批量删除', '/link/batch/delete', 'link:batchDelete', 45, 2, 0, '', 1, '2021-07-25 11:09:40', '2021-07-25 11:09:40');
INSERT INTO `sys_menu` VALUES (51, '审核', '友链-审核', '/link/audit', 'link:audit', 45, 2, 0, '', 1, '2021-07-25 11:42:28', '2021-07-25 11:42:28');
INSERT INTO `sys_menu` VALUES (53, '分类管理', '分类管理', '/categories', 'categories', 35, 1, 3, 'fas fa-chess-queen', 1, '2021-07-25 17:43:50', '2021-04-19 20:33:27');
INSERT INTO `sys_menu` VALUES (54, '新增', '新增分类', '/category/add', 'category:add', 53, 2, 0, '', 1, '2021-07-25 17:44:28', '2021-07-25 17:44:28');
INSERT INTO `sys_menu` VALUES (55, '编辑', '编辑分类', '/category/edit', 'category:edit', 53, 2, 0, '', 1, '2021-07-25 17:44:52', '2021-07-25 17:44:52');
INSERT INTO `sys_menu` VALUES (56, '删除', '删除分类', '/category/delete', 'category:delete', 53, 2, 0, '', 1, '2021-07-25 17:45:28', '2021-07-25 17:45:28');
INSERT INTO `sys_menu` VALUES (58, '查询', '分类查询', '/category/list', 'category:list', 53, 2, 0, '', 1, '2021-07-25 17:49:43', '2021-07-25 17:49:43');
INSERT INTO `sys_menu` VALUES (72, '评论管理', '评论管理', '/comments', 'comments', 35, 1, 4, 'fas fa-chess-queen', 1, '2021-08-10 09:44:41', '2021-09-19 15:44:13');
INSERT INTO `sys_menu` VALUES (73, '查询', '查询', '/comment/list', 'comment:list', 72, 2, 0, '', 1, '2021-08-10 09:46:54', '2021-08-10 09:46:54');
INSERT INTO `sys_menu` VALUES (74, '审核', '审核评论', '/comment/audit', 'comment:audit', 72, 2, 0, '', 1, '2021-08-10 09:57:11', '2021-08-10 09:57:11');
INSERT INTO `sys_menu` VALUES (75, '回复', '回复评论', '/comment/reply', 'comment:audit', 72, 2, 0, '', 1, '2021-08-10 10:04:28', '2021-08-10 10:04:28');
INSERT INTO `sys_menu` VALUES (76, '删除', '删除评论', '/comment/delete', 'comment:delete', 72, 2, 0, '', 1, '2021-08-10 10:06:27', '2021-08-10 10:06:27');
INSERT INTO `sys_menu` VALUES (77, '批量删除', '批量删除评论', '/comment/batch/delete', 'comment:batchDelete', 72, 2, 0, '', 1, '2021-08-10 10:07:57', '2021-08-10 10:07:57');
INSERT INTO `sys_menu` VALUES (89, '登录日志', '登录日志', '/login/logs', 'loginLogs', 22, 1, 2, 'fas fa-th-list', 1, '2021-07-20 16:01:23', '2021-07-20 16:01:23');
INSERT INTO `sys_menu` VALUES (90, '数据备份', '数据备份', '/db/backup', 'dbBackup', 22, 1, 3, 'fas fa-clone', 1, '2021-07-20 16:01:23', '2021-07-20 16:01:23');
INSERT INTO `sys_menu` VALUES (91, '错误日志', '错误日志', '/error/logs', 'errorLogs', 22, 1, 4, 'fas fa-skull', 1, '2021-07-20 16:01:23', '2021-07-20 16:01:23');
INSERT INTO `sys_menu` VALUES (92, '服务监控', '服务监控', '/server/monitoring', 'database', 22, 1, 1, 'fas fa-chess-queen', 1, '2021-07-06 15:19:55', '2021-07-06 15:19:55');
