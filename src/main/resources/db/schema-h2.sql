DROP TABLE IF EXISTS `biz_category`;
CREATE TABLE IF NOT EXISTS `biz_category` (
                                                `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
                                                `pid` int UNSIGNED NULL DEFAULT NULL,
                                                `name` varchar(50) NULL DEFAULT NULL COMMENT '文章类型名',
                                                `description` varchar(200) NULL DEFAULT NULL COMMENT '类型介绍',
                                                `sort` int NULL DEFAULT NULL COMMENT '排序',
                                                `icon` varchar(100) NULL DEFAULT NULL COMMENT '图标',
                                                `status` tinyint UNSIGNED NULL DEFAULT 1 COMMENT '是否可用',
                                                `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间',
                                                `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间'
    );

DROP TABLE IF EXISTS `biz_website`;
CREATE TABLE IF NOT EXISTS `biz_website` (
                                            `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
                                            `name` varchar(50) NOT NULL COMMENT '链接名',
                                            `url` varchar(200) NOT NULL COMMENT '链接地址',
                                            `description` varchar(255) NULL DEFAULT NULL COMMENT '链接介绍',
                                            `image_id` varchar(50) NULL  DEFAULT NULL COMMENT 'logo图片id',
                                            `email` varchar(100) NULL DEFAULT NULL COMMENT '站长邮箱',
                                            `qq` varchar(50) NULL DEFAULT NULL COMMENT '站长qq',
                                            `status` int UNSIGNED NULL DEFAULT NULL COMMENT '状态',
                                            `origin` int NULL DEFAULT NULL COMMENT '1-管理员添加 2-自助申请',
                                            `remark` varchar(255) NULL DEFAULT NULL COMMENT '备注',
                                            `category_id` int  NULL DEFAULT NULL COMMENT '分类id',
                                            `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间',
                                            `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间'
    );

DROP TABLE IF EXISTS `biz_link`;
CREATE TABLE IF NOT EXISTS `biz_link` (
                                            `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
                                            `name` varchar(50) NOT NULL COMMENT '链接名',
                                            `url` varchar(200) NOT NULL COMMENT '链接地址',
                                            `description` varchar(255) NULL DEFAULT NULL COMMENT '链接介绍',
                                            `image_id` varchar(50) NULL  DEFAULT NULL COMMENT 'logo图片id',
                                            `email` varchar(100) NULL DEFAULT NULL COMMENT '友链站长邮箱',
                                            `qq` varchar(50) NULL DEFAULT NULL COMMENT '友链站长qq',
                                            `status` int UNSIGNED NULL DEFAULT NULL COMMENT '状态',
                                            `origin` int NULL DEFAULT NULL COMMENT '1-管理员添加 2-自助申请',
                                            `remark` varchar(255) NULL DEFAULT NULL COMMENT '备注',
                                            `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间',
                                            `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间'
    );

DROP TABLE IF EXISTS `biz_image`;
CREATE TABLE IF NOT EXISTS `biz_image` (
                                           `id` varchar(50)  NOT NULL DEFAULT '' COMMENT 'MD5主键',
                                           `base64` text NULL DEFAULT NULL COMMENT '图片base64码',
                                           `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间'
    );

DROP TABLE IF EXISTS `biz_comment`;
CREATE TABLE IF NOT EXISTS `biz_comment` (
                                                `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
                                                `sid` int NULL DEFAULT NULL COMMENT '被评论的文章或者页面的ID(-1:留言板)',
                                                `user_id` varchar(20) NULL DEFAULT NULL COMMENT '评论人的ID',
                                                `pid` int UNSIGNED NULL DEFAULT NULL COMMENT '父级评论的id',
                                                `reply_id` int UNSIGNED NULL DEFAULT NULL COMMENT '回复评论的id',
                                                `qq` varchar(13) NULL DEFAULT NULL COMMENT '评论人的QQ（未登录用户）',
                                                `nickname` varchar(13) NULL DEFAULT NULL COMMENT '评论人的昵称（未登录用户）',
                                                `avatar` varchar(255) NULL DEFAULT NULL COMMENT '评论人的头像地址',
                                                `email` varchar(100) NULL DEFAULT NULL COMMENT '评论人的邮箱地址（未登录用户）',
                                                `url` varchar(200) NULL DEFAULT NULL COMMENT '评论人的网站地址（未登录用户）',
                                                `status` tinyint(1) NULL DEFAULT 0 COMMENT '评论的状态',
                                                `ip` varchar(64) NULL DEFAULT NULL COMMENT '评论时的ip',
                                                `lng` varchar(50) NULL DEFAULT NULL COMMENT '经度',
                                                `lat` varchar(50) NULL DEFAULT NULL COMMENT '纬度',
                                                `address` varchar(100) NULL DEFAULT NULL COMMENT '评论时的地址',
                                                `os` varchar(64) NULL DEFAULT NULL COMMENT '评论时的系统类型',
                                                `os_short_name` varchar(10) NULL DEFAULT NULL COMMENT '评论时的系统的简称',
                                                `browser` varchar(64) NULL DEFAULT NULL COMMENT '评论时的浏览器类型',
                                                `browser_short_name` varchar(10) NULL DEFAULT NULL COMMENT '评论时的浏览器的简称',
                                                `content` varchar(2000) NULL DEFAULT NULL COMMENT '评论的内容',
                                                `remark` varchar(100) NULL DEFAULT NULL COMMENT '备注（审核不通过时添加）',
                                                `support` int UNSIGNED NULL DEFAULT 0 COMMENT '支持（赞）',
                                                `oppose` int UNSIGNED NULL DEFAULT 0 COMMENT '反对（踩）',
                                                `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间',
                                                `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间'
    );

DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE IF NOT EXISTS `sys_config` (
                                            `id` bigint NOT NULL AUTO_INCREMENT,
                                            `sys_key` varchar(50) NULL DEFAULT NULL COMMENT 'key',
                                            `sys_value` varchar(2000) NULL DEFAULT NULL COMMENT 'value',
                                            `status` tinyint NULL DEFAULT 1 COMMENT '状态   0：隐藏   1：显示',
                                            `remark` varchar(500) NULL DEFAULT NULL COMMENT '备注'
    );

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE IF NOT EXISTS `sys_menu` (
                                            `id` int NOT NULL AUTO_INCREMENT,
                                            `name` varchar(100) NOT NULL COMMENT '权限名称',
                                            `description` varchar(255) NULL DEFAULT NULL COMMENT '权限描述',
                                            `url` varchar(255) NULL DEFAULT NULL COMMENT '权限访问路径',
                                            `perms` varchar(255) NULL DEFAULT NULL COMMENT '权限标识',
                                            `parent_id` int NULL DEFAULT NULL COMMENT '父级权限id',
                                            `type` int NULL DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
                                            `order_num` int NULL DEFAULT 0 COMMENT '排序',
                                            `icon` varchar(50) NULL DEFAULT NULL COMMENT '图标',
                                            `status` int NOT NULL COMMENT '状态：1有效；2删除',
                                            `create_time` datetime(0) NULL DEFAULT NULL,
                                            `update_time` datetime(0) NULL DEFAULT NULL
    );

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE IF NOT EXISTS `sys_user` (
                                            `id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
                                            `username` varchar(50) NOT NULL COMMENT '用户名',
                                            `password` varchar(50) NOT NULL COMMENT '密码',
                                            `salt` varchar(128) NULL DEFAULT NULL COMMENT '加密盐值',
                                            `nickname` varchar(50) NULL DEFAULT NULL COMMENT '昵称',
                                            `email` varchar(50) NULL DEFAULT NULL COMMENT '邮箱',
                                            `phone` varchar(50) NULL DEFAULT NULL COMMENT '联系方式',
                                            `sex` int NULL DEFAULT NULL COMMENT '年龄：1男2女',
                                            `age` int NULL DEFAULT NULL COMMENT '年龄',
                                            `image_id` varchar(50) NULL DEFAULT NULL COMMENT '头像地址(图片id)',
                                            `status` int NOT NULL COMMENT '用户状态：1有效; 2删除',
                                            `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                            `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                            `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间'
    );

DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE IF NOT EXISTS `sys_login_log` (
                                            `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
                                            `name` varchar(50) NULL DEFAULT NULL COMMENT '姓名',
                                            `phone` varchar(15) NULL DEFAULT NULL COMMENT '手机号',
                                            `login_name` varchar(50) NULL DEFAULT NULL COMMENT '登录名',
                                            `source` varchar(20) NULL DEFAULT NULL COMMENT '来源',
                                            `source_ip` varchar(128) NULL DEFAULT NULL COMMENT '来源',
                                            `start_time` datetime(0) NULL DEFAULT NULL COMMENT '登录时间',
                                            `end_time` datetime(0) NULL DEFAULT NULL COMMENT '登出时间',
                                            `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间'
    );

DROP TABLE IF EXISTS `sys_log_error`;
CREATE TABLE IF NOT EXISTS `sys_log_error` (
                                    `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
                                    `remote_ip` varchar(25) NULL DEFAULT NULL COMMENT '操作IP地址',
                                    `user_agent` varchar(1000) NULL DEFAULT NULL COMMENT '用户代理',
                                    `request_uri` varchar(50) NULL DEFAULT NULL COMMENT '请求URI',
                                    `method` varchar(20) NULL DEFAULT NULL COMMENT '来源',
                                    `method_class` varchar(128) NULL DEFAULT NULL COMMENT '方法类',
                                    `method_name` varchar(255)  NULL DEFAULT NULL COMMENT '方法名',
                                    `params` text NULL DEFAULT NULL COMMENT '操作提交的数据',
                                    `stack_trace` text NULL COMMENT '堆栈跟踪',
                                    `exception_name` varchar(255) NULL DEFAULT NULL COMMENT '异常名称',
                                    `message` text NULL COMMENT '信息',
                                    `file_name` varchar(255) NULL DEFAULT NULL COMMENT '文件名',
                                    `line_number` int(0) NULL DEFAULT NULL COMMENT '错误行数',
                                    `create_name` varchar(50) NULL DEFAULT NULL COMMENT '创建人',
                                    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间'
    );
