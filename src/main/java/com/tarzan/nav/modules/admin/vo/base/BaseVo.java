package com.tarzan.nav.modules.admin.vo.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.tarzan.nav.utils.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.io.Serializable;
import java.util.Date;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
@SuperBuilder
@NoArgsConstructor
@ToString
@MappedSuperclass
@EqualsAndHashCode
public abstract class BaseVo implements Serializable {


    @TableField(fill = FieldFill.INSERT)
    @Column(name = "create_time")
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Column(name = "update_time")
    private Date updateTime;

    @PrePersist
    protected void prePersist() {
        Date now = DateUtil.now();
        if (createTime == null) {
            createTime = now;
        }

        if (updateTime == null) {
            updateTime = now;
        }
    }

}