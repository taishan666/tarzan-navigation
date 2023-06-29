package com.tarzan.nav.utils;

import com.tarzan.nav.modules.admin.vo.base.PageResultVo;
import com.tarzan.nav.modules.admin.vo.base.ResponseVo;
import com.tarzan.nav.common.constant.CoreConst;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * 返回结果封装对象
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@UtilityClass
public class ResultUtil {

    public static <T> ResponseVo<T> success() {
        return vo(CoreConst.SUCCESS_CODE, null, null);
    }

    public static <T> ResponseVo<T> success(String msg) {
        return vo(CoreConst.SUCCESS_CODE, msg, null);
    }

    public static <T> ResponseVo<T> success(String msg, T data) {
        return vo(CoreConst.SUCCESS_CODE, msg, data);
    }

    public static <T> ResponseVo<T> error() {
        return vo(CoreConst.FAIL_CODE, null, null);
    }

    public static <T> ResponseVo<T> error(String msg) {
        return vo(CoreConst.FAIL_CODE, msg, null);
    }

    public static <T> ResponseVo<T> error(String msg, T data) {
        return vo(CoreConst.FAIL_CODE, msg, data);
    }

    public static <T> ResponseVo<T> status(Integer status, String message) {
        return vo(status, message, null);
    }

    public static <T> PageResultVo<T> table(List<T> list, Long total) {
        return new PageResultVo<>(list, total);
    }

    public static <T> ResponseVo<T> vo(Integer status, String message, T data) {
        return new ResponseVo<>(status, message, data);
    }

    public static <T> ResponseVo<T> vo(T data) {
        return vo(CoreConst.SUCCESS_CODE,null,data);
    }



}
