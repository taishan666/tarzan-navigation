package com.tarzan.nacigation.modules.admin.model.sys;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
@Accessors(chain = true)
public class SysConfig implements Serializable {


    private Integer id;

    /**
     * key
     */
    private String sysKey;

    /**
     * value
     */
    private String sysValue;

    /**
     * 状态  1：有效 0：无效
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

}