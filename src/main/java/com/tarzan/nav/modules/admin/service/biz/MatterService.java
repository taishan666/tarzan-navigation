package com.tarzan.nav.modules.admin.service.biz;

import com.tarzan.nav.modules.admin.vo.NotificationNumVO;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author tarzan
 */
@Service
@AllArgsConstructor
public class MatterService {

    private final CommentService commentService;
    private final WebsiteService websiteService;
    private final LinkService linkService;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationNumVO todoItems(){
        NotificationNumVO vo=new NotificationNumVO();
        vo.setToAuditComments(commentService.toAuditNum());
        vo.setToAuditWebsites(websiteService.toAuditNum());
        vo.setToAuditLinks(linkService.toAuditNum());
        vo.setTotal(vo.getToAuditLinks()+vo.getToAuditComments()+vo.getToAuditWebsites());
        return vo;
    }

    @Async
    public void sendNotification(){
        messagingTemplate.convertAndSend("/topic/notification",this.todoItems());
    }

}
