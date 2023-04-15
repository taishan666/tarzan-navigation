package com.tarzan.nav.modules.admin.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tarzan.nav.modules.admin.model.sys.Menu;

import java.util.List;
import java.util.Set;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据用户id查询权限集合
     *
     * @param userId 状态
     * @return set
     */
    Set<String> findPermsByUserId(Integer userId);


    /**
     * 根据用户id查询菜单
     *
     * @param userId 用户id
     * @return the list
     */
    List<Menu> selectMenuByUserId(Integer userId);

}