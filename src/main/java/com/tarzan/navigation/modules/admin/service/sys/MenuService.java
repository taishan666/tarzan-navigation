package com.tarzan.navigation.modules.admin.service.sys;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.navigation.common.constant.CoreConst;
import com.tarzan.navigation.modules.admin.mapper.sys.MenuMapper;
import com.tarzan.navigation.modules.admin.model.sys.Menu;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class MenuService extends ServiceImpl<MenuMapper, Menu> {

    private static final Pattern SLASH_PATTERN = Pattern.compile(StringPool.SLASH);

    private  List<Menu> buildPermissionTree(List<Menu> permissionList) {
        Map<Integer, List<Menu>> parentIdToPermissionListMap = permissionList.stream().peek(p -> {
            if (StringUtils.startsWith(p.getUrl(), StringPool.SLASH)) {
                p.setUrl(SLASH_PATTERN.matcher(p.getUrl()).replaceFirst(StringPool.HASH));
            }
        }).collect(Collectors.groupingBy(Menu::getParentId));
        List<Menu> rootLevelPermissionList = parentIdToPermissionListMap.getOrDefault(CoreConst.TOP_MENU_ID, Collections.emptyList());
        fetchChildren(rootLevelPermissionList, parentIdToPermissionListMap);
        return rootLevelPermissionList;
    }

    private  void fetchChildren(List<Menu> menuListList, Map<Integer, List<Menu>> parentIdToPermissionListMap) {
        if (CollectionUtils.isEmpty(menuListList)) {
            return;
        }
        for (Menu menu : menuListList) {
            List<Menu> childrenList = parentIdToPermissionListMap.get(menu.getId());
            fetchChildren(childrenList, parentIdToPermissionListMap);
            menu.setChildren(childrenList);
        }
    }

    public Set<String> findPermsByUserId(Integer userId) {
        return baseMapper.findPermsByUserId(userId);
    }

    @Cacheable(value = "menu", key = "'all'")
    public List<Menu> selectAll(Integer status) {
        return  super.lambdaQuery().eq(Menu::getStatus,status).orderByAsc(Menu::getOrderNum).list();
    }

    @Cacheable(value = "menu", key = "'menus'")
    public List<Menu> selectAllMenuName(Integer status) {
        return  super.lambdaQuery().ne(Menu::getType,2).eq(Menu::getStatus,status).orderByAsc(Menu::getOrderNum).list();
    }

    public List<Menu> selectMenuByUserId(Integer userId) {
        return baseMapper.selectMenuByUserId(userId);
    }

    @Cacheable(value = "menu", key = "#userId")
    public List<Menu> selectMenuTreeByUserId(Integer userId) {
        return buildPermissionTree(selectMenuByUserId(userId));
    }


    public boolean insert(Menu menu) {
        menu.setStatus(CoreConst.STATUS_VALID);
        return save(menu);
    }

    public int updateStatus(Integer id, Integer status) {
        return baseMapper.updateById(new Menu().setId(id).setStatus(status));
    }

    public long selectSubPermsByPermissionId(Integer id) {
        return baseMapper.selectCount(Wrappers.lambdaQuery(new Menu().setParentId(id).setStatus(1)));
    }
}
