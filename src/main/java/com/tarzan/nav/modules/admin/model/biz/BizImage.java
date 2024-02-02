package com.tarzan.nav.modules.admin.model.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.nav.modules.admin.entity.biz.BizImageEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author tarzan
 */
@Data
@TableName("biz_image")
@SuperBuilder
@NoArgsConstructor
public class BizImage extends BizImageEntity {

}
