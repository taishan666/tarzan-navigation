package com.tarzan.navigation.modules.admin.vo.base;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
public abstract class BaseVo implements Serializable {

    private Integer id;

    private Date createTime;
    private Date updateTime;

}