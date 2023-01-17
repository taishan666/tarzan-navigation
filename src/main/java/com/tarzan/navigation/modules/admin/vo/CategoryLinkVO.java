package com.tarzan.navigation.modules.admin.vo;

import com.tarzan.navigation.modules.admin.model.biz.Link;
import lombok.Data;

import java.util.List;

/**
 * @author TARZAN
 */
@Data
public class CategoryLinkVO {

    private String categoryName;

    private List<Link> links;
}
