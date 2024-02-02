package com.tarzan.nav.modules.admin.entity.biz;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author tarzan
 */
@Data
@Table(name="biz_image")
@Entity
@SuperBuilder
@NoArgsConstructor
public class BizImageEntity {

    @Id
    private String id;
    private String base64;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
