package com.tarzan.navigation.common.constant;

import lombok.experimental.UtilityClass;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 常量工具类
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@UtilityClass
public class CoreConst {

    public static final Integer SUCCESS_CODE = 200;
    public static final Integer FAIL_CODE = 500;
    public static final Integer STATUS_VALID = 1;
    public static final Integer STATUS_INVALID = 0;
    public static final Integer ZERO = 0;
    public static final Integer ONE = 1;
    public static final Integer PAGE_SIZE = 10;
    public static final Integer ADMINISTRATOR_ID = 1;
    public static final Integer TOP_MENU_ID = 0;
    public static final String ADMIN_NAME = "管理员";
    public static final String TOP_MENU_NAME = "顶层菜单";
    public static final String TARZAN_REDIS_CMS_PREFIX = "tarzan:cms:";
    public static final String SHIRO_REDIS_SESSION_PREFIX = "tarzan:cms:session:";
    public static final String SHIRO_REDIS_CACHE_NAME = "shiro_tarzan_cms";
    public final static String USER_TYPE_HEADER_KEY = "User-Type";

    public final static String SYSTEM_REGISTER = "/system/register";

    public static final String ADMIN_PREFIX = "admin/";

    public static final String ERROR_PREFIX = "error/";

    public static final String THEME_PREFIX = "theme/";

    public static final String USE_THEME = "source";

    /**
     * h2默认table数
     */
    public static final int TABLE_NUM = 15;
    /**
     * 文件存储服务类型
     * 1：七牛  2：阿里云  3：腾讯云  4：本地
     */
    public static final int UPLOAD_TYPE_QINIUYUN = 1;
    public static final int UPLOAD_TYPE_ALIYUN = 2;
    public static final int UPLOAD_TYPE_TXYUN = 3;
    public static final int UPLOAD_TYPE_LOCAL = 4;

    /**
     * 是否安装
     */
    public static final AtomicBoolean IS_INSTALLED = new AtomicBoolean(false);
    /**
     * 是否注册
     */
    public static final AtomicBoolean IS_REGISTERED = new AtomicBoolean(false);
    /**
     * 网站是否静态化
     */
    public static final String SITE_STATIC_KEY = "SITE_STATIC";
    public static final AtomicBoolean SITE_STATIC = new AtomicBoolean(false);

    public static final String JAR ="jar";


}
