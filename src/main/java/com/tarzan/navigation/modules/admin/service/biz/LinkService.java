package com.tarzan.navigation.modules.admin.service.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.navigation.modules.admin.mapper.biz.ImageMapper;
import com.tarzan.navigation.modules.admin.mapper.biz.LinkMapper;
import com.tarzan.navigation.modules.admin.model.biz.BizImage;
import com.tarzan.navigation.modules.admin.model.biz.Link;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class LinkService extends ServiceImpl<LinkMapper, Link> {

    private final ImageMapper imageMapper;

    @Cacheable(value = "link", key = "'list'")
    public List<Link> selectLinks(Link link) {
        return list(Wrappers.<Link>lambdaQuery()
                .like(StringUtils.isNotBlank(link.getName()), Link::getName, link.getName())
                .like(StringUtils.isNotBlank(link.getUrl()), Link::getUrl, link.getUrl())
                .eq(Objects.nonNull(link.getStatus()), Link::getStatus, link.getStatus()));
    }

    public IPage<Link> pageLinks(Link link, Integer pageNumber, Integer pageSize) {
        IPage<Link> page = new Page<>(pageNumber, pageSize);
        return page(page,Wrappers.<Link>lambdaQuery()
                .like(StringUtils.isNotBlank(link.getName()), Link::getName, link.getName())
                .like(StringUtils.isNotBlank(link.getUrl()), Link::getUrl, link.getUrl())
                .eq(Objects.nonNull(link.getStatus()), Link::getStatus, link.getStatus())
                .orderByDesc(Link::getCreateTime));
    }

    @CacheEvict(value = {"link", "categoryLink"}, allEntries = true)
    public boolean deleteBatch(List<Integer> ids) {
        return removeByIds(ids);
    }

    public void wrapper(Link link){
        BizImage img=imageMapper.selectById(link.getImageId());
        if(img!=null){
            link.setImg(img);
        }
    }

    public void wrapper(List<Link> links){
        Set<Integer> imageIds=links.stream().map(Link::getImageId).collect(Collectors.toSet());
        if(CollectionUtils.isNotEmpty(imageIds)){
            List<BizImage> images= imageMapper.selectBatchIds(imageIds);
            Map<Integer,BizImage> map=images.stream().collect(Collectors.toMap(BizImage::getId, e->e));
            links.forEach(e->e.setImg(map.get(e.getImageId())));
        }
    }

}
