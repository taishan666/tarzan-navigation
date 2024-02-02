package com.tarzan.nav.modules.admin.model.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.nav.common.constant.TableConst;
import com.tarzan.nav.modules.admin.entity.sys.MenuEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
@TableName(TableConst.MENU)
@SuperBuilder
@NoArgsConstructor
public class Menu extends MenuEntity {
    @TableField(exist = false)
    private List<Menu> children;
}