package com.tarzan.navigation.modules.admin.service.biz;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.navigation.common.constant.CoreConst;
import com.tarzan.navigation.modules.admin.mapper.biz.CategoryLinkMapper;
import com.tarzan.navigation.modules.admin.model.biz.Category;
import com.tarzan.navigation.modules.admin.model.biz.CategoryLink;
import com.tarzan.navigation.modules.admin.model.biz.Link;
import com.tarzan.navigation.modules.admin.vo.CategoryLinkVO;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
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
        List<Category> categories=categoryService.selectCategories(CoreConst.ONE);
        if(!CollectionUtils.isEmpty(categories)){
            List<CategoryLink> list= super.list();
            Map<Integer,List<CategoryLink>> map=list.stream().collect(Collectors.groupingBy(CategoryLink::getCategoryId));
            categories.forEach(e->{
                List<CategoryLink> categoryLinks=map.get(e.getId());
                if(!CollectionUtils.isEmpty(categoryLinks)){
                    CategoryLinkVO vo=new CategoryLinkVO();
                    vo.setCategoryName(e.getName());
                    Set<Integer> linkIds=categoryLinks.stream().map(CategoryLink::getLinkId).collect(Collectors.toSet());
                    List<Link> links=linkService.listByIds(linkIds);
                    linkService.wrapper(links);
                    vo.setLinks(links);
                    result.add(vo);
                }
            });
        }
        return result;
    }
}
