package com.tarzan.nav.modules.admin.service.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.admin.entity.biz.LinkEntity;
import com.tarzan.nav.modules.admin.model.biz.Link;
import com.tarzan.nav.modules.admin.mapper.biz.LinkMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Service
public class LinkService extends ServiceImpl<LinkMapper, LinkEntity> {

    public IPage<LinkEntity> pageLinks(Link link, Integer pageNumber, Integer pageSize) {
        IPage<LinkEntity> page = new Page<>(pageNumber, pageSize);
        return page(page,Wrappers.<LinkEntity>lambdaQuery()
                .like(StringUtils.isNotBlank(link.getName()), LinkEntity::getName, link.getName())
                .like(StringUtils.isNotBlank(link.getUrl()), LinkEntity::getUrl, link.getUrl())
                .eq(Objects.nonNull(link.getStatus()), LinkEntity::getStatus, link.getStatus())
                .orderByDesc(LinkEntity::getCreateTime));
    }

    @CacheEvict(value = "link", allEntries = true)
    public boolean deleteBatch(List<Integer> ids) {
        return removeByIds(ids);
    }

    @Cacheable(value = "link", key = "'list'")
    public List<LinkEntity>  simpleList() {
        return super.lambdaQuery().select(LinkEntity::getName,LinkEntity::getUrl).eq(LinkEntity::getStatus, CoreConst.STATUS_VALID).list();
    }

    public Long toAuditNum() {
        return super.lambdaQuery().eq(LinkEntity::getStatus,CoreConst.ZERO).count();
    }
}
