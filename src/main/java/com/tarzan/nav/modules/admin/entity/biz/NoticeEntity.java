package com.tarzan.nav.modules.admin.entity.biz;

import com.tarzan.nav.modules.admin.vo.base.BaseVo;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * @author TARZAN
 */
@Data
@Table(name="biz_notice")
@Entity
public class NoticeEntity extends BaseVo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private Integer status;
}
