package com.tarzan.nav.modules.admin.entity.biz;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tarzan
 */
@Data
@Accessors(chain = true)
@TableName("biz_image")
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
