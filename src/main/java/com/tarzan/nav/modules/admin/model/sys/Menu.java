package com.tarzan.nav.modules.admin.model.sys;

import com.tarzan.nav.modules.admin.entity.sys.MenuEntity;
import lombok.Data;

import java.util.List;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
public class Menu extends MenuEntity {
    private List<Menu> children;
}