package com.tarzan.nacigation.modules.admin.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
public class DbBackupVO {

    private String fileName;
    private Long size;
    private String type;
    private String url;
    private Integer status;
    private Date createTime;

}
