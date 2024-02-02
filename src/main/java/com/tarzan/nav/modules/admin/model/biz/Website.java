package com.tarzan.nav.modules.admin.model.biz;

import com.tarzan.nav.modules.admin.entity.biz.WebsiteEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class Website extends WebsiteEntity {

    private String flag;
    private BizImage img;
    private String categoryName;
    private String applyType;

}
