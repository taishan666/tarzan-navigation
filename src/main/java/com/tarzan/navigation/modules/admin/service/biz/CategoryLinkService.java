package com.tarzan.navigation.modules.admin.service.biz;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.navigation.modules.admin.mapper.biz.CategoryLinkMapper;
import com.tarzan.navigation.modules.admin.model.biz.Category;
import com.tarzan.navigation.modules.admin.model.biz.CategoryLink;
import com.tarzan.navigation.modules.admin.model.biz.Link;
import com.tarzan.navigation.modules.admin.vo.CategoryLinkVO;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author tarzan
 */
@Service
@AllArgsConstructor
public class CategoryLinkService extends ServiceImpl<CategoryLinkMapper, CategoryLink> {

   private final CategoryService categoryService;
   private final LinkService linkService;

    @Cacheable(value = "categoryLink", key = "'all'")
    public List<CategoryLinkVO> listAll() {
        List<CategoryLinkVO> result=new ArrayList<>(100);
        List<CategoryLink> list= super.list();
        Map<Integer,List<CategoryLink>> map=list.stream().collect(Collectors.groupingBy(CategoryLink::getCategoryId));
        if(!CollectionUtils.isEmpty(map)){
            List<Category> categories=categoryService.listByIds(map.keySet());
            Map<Integer,String> categoriesMap=categories.stream().collect(Collectors.toMap(Category::getId,Category::getName));
            map.forEach((k,v)->{
                CategoryLinkVO vo=new CategoryLinkVO();
                vo.setCategoryName(categoriesMap.get(k));
                Set<Integer> linkIds=v.stream().map(CategoryLink::getLinkId).collect(Collectors.toSet());
                List<Link> links=linkService.listByIds(linkIds);
                linkService.wrapper(links);
                vo.setLinks(links);
                result.add(vo);
            });
        }
        return result;
    }
}
