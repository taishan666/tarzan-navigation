DROP TABLE
    IF
        EXISTS biz_category;
CREATE TABLE
    IF
    NOT EXISTS biz_category (
                                ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
                                PID INT UNSIGNED NULL DEFAULT NULL,
                                NAME VARCHAR ( 50 ) NULL DEFAULT NULL COMMENT '文章类型名',
                                DESCRIPTION VARCHAR ( 200 ) NULL DEFAULT NULL COMMENT '类型介绍',
                                SORT INT NULL DEFAULT NULL COMMENT '排序',
                                ICON VARCHAR ( 100 ) NULL DEFAULT NULL COMMENT '图标',
                                STATUS tinyint UNSIGNED NULL DEFAULT 1 COMMENT '是否可用',
                                CREATE_TIME datetime ( 0 ) NULL DEFAULT CURRENT_TIMESTAMP ( 0 ) COMMENT '添加时间',
                                UPDATE_TIME datetime ( 0 ) NULL DEFAULT CURRENT_TIMESTAMP ( 0 ) COMMENT '更新时间'
);
DROP TABLE
    IF
        EXISTS biz_website;
CREATE TABLE
    IF
    NOT EXISTS biz_website (
                               ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
                               NAME VARCHAR ( 50 ) NOT NULL COMMENT '链接名',
                               URL VARCHAR ( 200 ) NOT NULL COMMENT '链接地址',
                               DESCRIPTION VARCHAR ( 255 ) NULL DEFAULT NULL COMMENT '链接介绍',
                               IMAGE_ID VARCHAR ( 50 ) NULL DEFAULT NULL COMMENT 'logo图片id',
                               EMAIL VARCHAR ( 100 ) NULL DEFAULT NULL COMMENT '站长邮箱',
                               QQ VARCHAR ( 50 ) NULL DEFAULT NULL COMMENT '站长qq',
                               STATUS INT UNSIGNED NULL DEFAULT NULL COMMENT '状态',
                               ORIGIN INT UNSIGNED DEFAULT NULL COMMENT '1-管理员添加 2-自助申请',
                               REMARK VARCHAR ( 255 ) NULL DEFAULT NULL COMMENT '备注',
                               CATEGORY_ID INT NULL DEFAULT NULL COMMENT '分类id',
                               CREATE_TIME datetime ( 0 ) NULL DEFAULT CURRENT_TIMESTAMP ( 0 ) COMMENT '添加时间',
                               UPDATE_TIME datetime ( 0 ) NULL DEFAULT CURRENT_TIMESTAMP ( 0 ) COMMENT '更新时间'
);
DROP TABLE
    IF
        EXISTS biz_link;
CREATE TABLE
    IF
    NOT EXISTS biz_link (
                            ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
                            NAME VARCHAR ( 50 ) NOT NULL COMMENT '链接名',
                            URL VARCHAR ( 200 ) NOT NULL COMMENT '链接地址',
                            DESCRIPTION VARCHAR ( 255 ) NULL DEFAULT NULL COMMENT '链接介绍',
                            IMAGE_ID VARCHAR ( 50 ) NULL DEFAULT NULL COMMENT 'logo图片id',
                            EMAIL VARCHAR ( 100 ) NULL DEFAULT NULL COMMENT '友链站长邮箱',
                            QQ VARCHAR ( 50 ) NULL DEFAULT NULL COMMENT '友链站长qq',
                            STATUS INT UNSIGNED NULL DEFAULT NULL COMMENT '状态',
                            ORIGIN INT NULL DEFAULT NULL COMMENT '1-管理员添加 2-自助申请',
                            REMARK VARCHAR ( 255 ) NULL DEFAULT NULL COMMENT '备注',
                            CREATE_TIME datetime ( 0 ) NULL DEFAULT CURRENT_TIMESTAMP ( 0 ) COMMENT '添加时间',
                            UPDATE_TIME datetime ( 0 ) NULL DEFAULT CURRENT_TIMESTAMP ( 0 ) COMMENT '更新时间'
);
DROP TABLE
    IF
        EXISTS biz_image;
CREATE TABLE
    IF
    NOT EXISTS biz_image (
                             ID VARCHAR ( 50 ) NOT NULL DEFAULT '' COMMENT 'MD5主键',
                             BASE64 TEXT NULL DEFAULT NULL COMMENT '图片base64码',
                             CREATE_TIME datetime ( 0 ) NULL DEFAULT CURRENT_TIMESTAMP ( 0 ) COMMENT '添加时间'
);
DROP TABLE
    IF
        EXISTS biz_notice;
CREATE TABLE
    IF
    NOT EXISTS biz_notice (
                              ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
                              TITLE VARCHAR ( 50 ) NOT NULL COMMENT '链接名',
                              CONTENT TEXT NULL DEFAULT NULL COMMENT '链接介绍',
                              END_TIME datetime ( 0 ) NULL DEFAULT CURRENT_TIMESTAMP ( 0 ) COMMENT '结束时间',
                              STATUS INT UNSIGNED NULL DEFAULT NULL COMMENT '状态',
                              CREATE_TIME datetime ( 0 ) NULL DEFAULT CURRENT_TIMESTAMP ( 0 ) COMMENT '添加时间',
                              UPDATE_TIME datetime ( 0 ) NULL DEFAULT CURRENT_TIMESTAMP ( 0 ) COMMENT '更新时间'
);
DROP TABLE
    IF
        EXISTS biz_comment;
CREATE TABLE
    IF
    NOT EXISTS biz_comment (
                               ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
                               SID INT NULL DEFAULT NULL COMMENT '被评论的文章或者页面的ID(-1:留言板)',
                               USER_ID VARCHAR ( 20 ) NULL DEFAULT NULL COMMENT '评论人的ID',
                               PID INT UNSIGNED NULL DEFAULT NULL COMMENT '父级评论的id',
                               REPLY_ID INT UNSIGNED NULL DEFAULT NULL COMMENT '回复评论的id',
                               QQ VARCHAR ( 13 ) NULL DEFAULT NULL COMMENT '评论人的QQ（未登录用户）',
                               NICKNAME VARCHAR ( 13 ) NULL DEFAULT NULL COMMENT '评论人的昵称（未登录用户）',
                               AVATAR VARCHAR ( 255 ) NULL DEFAULT NULL COMMENT '评论人的头像地址',
                               EMAIL VARCHAR ( 100 ) NULL DEFAULT NULL COMMENT '评论人的邮箱地址（未登录用户）',
                               URL VARCHAR ( 200 ) NULL DEFAULT NULL COMMENT '评论人的网站地址（未登录用户）',
                               STATUS tinyint ( 1 ) NULL DEFAULT 0 COMMENT '评论的状态',
                               IP VARCHAR ( 64 ) NULL DEFAULT NULL COMMENT '评论时的ip',
                               LNG VARCHAR ( 50 ) NULL DEFAULT NULL COMMENT '经度',
                               LAT VARCHAR ( 50 ) NULL DEFAULT NULL COMMENT '纬度',
                               ADDRESS VARCHAR ( 100 ) NULL DEFAULT NULL COMMENT '评论时的地址',
                               OS VARCHAR ( 64 ) NULL DEFAULT NULL COMMENT '评论时的系统类型',
                               OS_SHORT_NAME VARCHAR ( 10 ) NULL DEFAULT NULL COMMENT '评论时的系统的简称',
                               BROWSER VARCHAR ( 64 ) NULL DEFAULT NULL COMMENT '评论时的浏览器类型',
                               BROWSER_SHORT_NAME VARCHAR ( 10 ) NULL DEFAULT NULL COMMENT '评论时的浏览器的简称',
                               CONTENT VARCHAR ( 2000 ) NULL DEFAULT NULL COMMENT '评论的内容',
                               LOCATION VARCHAR ( 100 ) NULL DEFAULT NULL COMMENT '位置',
                               SUPPORT INT UNSIGNED NULL DEFAULT 0 COMMENT '支持（赞）',
                               OPPOSE INT UNSIGNED NULL DEFAULT 0 COMMENT '反对（踩）',
                               CREATE_TIME datetime ( 0 ) NULL DEFAULT CURRENT_TIMESTAMP ( 0 ) COMMENT '添加时间',
                               UPDATE_TIME datetime ( 0 ) NULL DEFAULT CURRENT_TIMESTAMP ( 0 ) COMMENT '更新时间'
);
DROP TABLE
    IF
        EXISTS sys_config;
CREATE TABLE
    IF
    NOT EXISTS sys_config (
                              ID BIGINT NOT NULL AUTO_INCREMENT,
                              SYS_KEY VARCHAR ( 50 ) NULL DEFAULT NULL COMMENT 'key',
                              SYS_VALUE VARCHAR ( 2000 ) NULL DEFAULT NULL COMMENT 'value',
                              STATUS tinyint NULL DEFAULT 1 COMMENT '状态   0：隐藏   1：显示',
                              REMARK VARCHAR ( 500 ) NULL DEFAULT NULL COMMENT '备注'
);
DROP TABLE
    IF
        EXISTS sys_menu;
CREATE TABLE
    IF
    NOT EXISTS sys_menu (
                            ID INT NOT NULL AUTO_INCREMENT,
                            PID INT NULL DEFAULT NULL COMMENT '父级权限id',
                            NAME VARCHAR ( 100 ) NOT NULL COMMENT '权限名称',
                            DESCRIPTION VARCHAR ( 255 ) NULL DEFAULT NULL COMMENT '权限描述',
                            URL VARCHAR ( 255 ) NULL DEFAULT NULL COMMENT '权限访问路径',
                            PERMS VARCHAR ( 255 ) NULL DEFAULT NULL COMMENT '权限标识',
                            TYPE INT NULL DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
                            SORT INT NULL DEFAULT 0 COMMENT '排序',
                            ICON VARCHAR ( 50 ) NULL DEFAULT NULL COMMENT '图标',
                            STATUS INT NOT NULL COMMENT '状态：1有效；2删除',
                            CREATE_TIME datetime ( 0 ) NULL DEFAULT NULL,
                            UPDATE_TIME datetime ( 0 ) NULL DEFAULT NULL
);
DROP TABLE
    IF
        EXISTS sys_user;
CREATE TABLE
    IF
    NOT EXISTS sys_user (
                            ID INT NOT NULL AUTO_INCREMENT COMMENT '用户id',
                            USERNAME VARCHAR ( 50 ) NOT NULL COMMENT '用户名',
                            PASSWORD VARCHAR ( 50 ) NOT NULL COMMENT '密码',
                            SALT VARCHAR ( 128 ) NULL DEFAULT NULL COMMENT '加密盐值',
                            NICKNAME VARCHAR ( 50 ) NULL DEFAULT NULL COMMENT '昵称',
                            EMAIL VARCHAR ( 50 ) NULL DEFAULT NULL COMMENT '邮箱',
                            PHONE VARCHAR ( 50 ) NULL DEFAULT NULL COMMENT '联系方式',
                            SEX INT NULL DEFAULT NULL COMMENT '年龄：1男2女',
                            AGE INT NULL DEFAULT NULL COMMENT '年龄',
                            IMAGE_ID VARCHAR ( 50 ) NULL DEFAULT NULL COMMENT '头像地址(图片id)',
                            STATUS INT NOT NULL COMMENT '用户状态：1有效; 2删除',
                            CREATE_TIME datetime ( 0 ) NULL DEFAULT NULL COMMENT '创建时间',
                            UPDATE_TIME datetime ( 0 ) NULL DEFAULT NULL COMMENT '更新时间',
                            LAST_LOGIN_TIME datetime ( 0 ) NULL DEFAULT NULL COMMENT '最后登录时间'
);
DROP TABLE
    IF
        EXISTS sys_login_log;
CREATE TABLE
    IF
    NOT EXISTS sys_login_log (
                                 ID INT ( 0 ) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
                                 NAME VARCHAR ( 50 ) NULL DEFAULT NULL COMMENT '姓名',
                                 PHONE VARCHAR ( 15 ) NULL DEFAULT NULL COMMENT '手机号',
                                 LOGIN_NAME VARCHAR ( 50 ) NULL DEFAULT NULL COMMENT '登录名',
                                 SOURCE VARCHAR ( 20 ) NULL DEFAULT NULL COMMENT '来源',
                                 SOURCE_IP VARCHAR ( 128 ) NULL DEFAULT NULL COMMENT '来源',
                                 START_TIME datetime ( 0 ) NULL DEFAULT NULL COMMENT '登录时间',
                                 END_TIME datetime ( 0 ) NULL DEFAULT NULL COMMENT '登出时间',
                                 CREATE_TIME datetime ( 0 ) NULL DEFAULT NULL COMMENT '创建时间'
);
DROP TABLE
    IF
        EXISTS sys_log_error;
CREATE TABLE
    IF
    NOT EXISTS sys_log_error (
                                 ID INT ( 0 ) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
                                 REMOTE_IP VARCHAR ( 25 ) NULL DEFAULT NULL COMMENT '操作IP地址',
                                 USER_AGENT VARCHAR ( 1000 ) NULL DEFAULT NULL COMMENT '用户代理',
                                 REQUEST_URI VARCHAR ( 50 ) NULL DEFAULT NULL COMMENT '请求URI',
                                 METHOD VARCHAR ( 20 ) NULL DEFAULT NULL COMMENT '来源',
                                 METHOD_CLASS VARCHAR ( 128 ) NULL DEFAULT NULL COMMENT '方法类',
                                 METHOD_NAME VARCHAR ( 255 ) NULL DEFAULT NULL COMMENT '方法名',
                                 PARAMS TEXT NULL DEFAULT NULL COMMENT '操作提交的数据',
                                 STACK_TRACE TEXT NULL COMMENT '堆栈跟踪',
                                 EXCEPTION_NAME VARCHAR ( 255 ) NULL DEFAULT NULL COMMENT '异常名称',
                                 MESSAGE TEXT NULL COMMENT '信息',
                                 FILE_NAME VARCHAR ( 255 ) NULL DEFAULT NULL COMMENT '文件名',
                                 LINE_NUMBER INT ( 0 ) NULL DEFAULT NULL COMMENT '错误行数',
                                 CREATE_NAME VARCHAR ( 50 ) NULL DEFAULT NULL COMMENT '创建人',
                                 CREATE_TIME datetime ( 0 ) NULL DEFAULT NULL COMMENT '创建时间'
);
DROP TABLE IF EXISTS biz_site_look;
CREATE TABLE IF NOT EXISTS biz_site_look (
                                                  ID int UNSIGNED NOT NULL AUTO_INCREMENT,
                                                  SITE_ID int UNSIGNED NOT NULL COMMENT '站点ID',
                                                  USER_IP varchar(50) NULL DEFAULT NULL COMMENT '用户IP',
                                                  PROVINCE VARCHAR ( 50 ) NULL DEFAULT NULL COMMENT '省份',
                                                  CREATE_TIME datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间'
);