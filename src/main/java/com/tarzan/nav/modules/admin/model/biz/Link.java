package com.tarzan.nav.modules.admin.model.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.nav.modules.admin.entity.biz.LinkEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
@TableName("biz_link")
@SuperBuilder
@NoArgsConstructor
public class Link extends LinkEntity {

}
