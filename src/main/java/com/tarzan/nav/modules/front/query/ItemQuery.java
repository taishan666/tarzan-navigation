package com.tarzan.nav.modules.front.query;

import lombok.Data;

/**
 * @author TARZAN
 */
@Data
public class ItemQuery {
    private Integer id;
    private String action;
    private Integer postId;
    private String taxonomy;
    private Integer sidebar;

}
