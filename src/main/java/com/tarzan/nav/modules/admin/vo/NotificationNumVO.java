package com.tarzan.nav.modules.admin.vo;

import lombok.Data;

@Data
public class NotificationNumVO {
    private Long total;
    private Long toAuditWebsites;
    private Long toAuditComments;
    private Long toAuditLinks;

}
