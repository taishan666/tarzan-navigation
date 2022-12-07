package com.tarzan.nacigation.utils;

import com.tarzan.nacigation.modules.admin.vo.base.PageResultVo;
import com.tarzan.nacigation.modules.admin.vo.base.ResponseVo;
import com.tarzan.nacigation.common.constant.CoreConst;
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

    public static ResponseVo success() {
        return vo(CoreConst.SUCCESS_CODE, null, null);
    }

    public static ResponseVo success(String msg) {
        return vo(CoreConst.SUCCESS_CODE, msg, null);
    }

    public static ResponseVo success(String msg, Object data) {
        return vo(CoreConst.SUCCESS_CODE, msg, data);
    }

    public static ResponseVo error() {
        return vo(CoreConst.FAIL_CODE, null, null);
    }

    public static ResponseVo error(String msg) {
        return vo(CoreConst.FAIL_CODE, msg, null);
    }

    public static ResponseVo error(String msg, Object data) {
        return vo(CoreConst.FAIL_CODE, msg, data);
    }

    public static PageResultVo table(List<?> list, Long total) {
        return new PageResultVo(list, total);
    }

    public static ResponseVo vo(Integer status, String message, Object data) {
        return new ResponseVo<>(status, message, data);
    }

    public static ResponseVo vo(Object data) {
        return vo(CoreConst.SUCCESS_CODE,null,data);
    }



}
