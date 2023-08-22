package com.tarzan.nav.modules.admin.vo;

import com.tarzan.nav.modules.admin.model.biz.Website;
import lombok.Data;

import java.util.List;

/**
 * @author TARZAN
 */
@Data
public class TopWebsiteVO {
    private List<Website> hotList;
    private List<Website> randomList;
    private List<Website> newestList;
}
