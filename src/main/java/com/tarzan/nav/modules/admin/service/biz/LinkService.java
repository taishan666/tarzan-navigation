package com.tarzan.nav.modules.admin.service.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nav.common.constant.CoreConst;
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
public class LinkService extends ServiceImpl<LinkMapper, Link> {

    public IPage<Link> pageLinks(Link link, Integer pageNumber, Integer pageSize) {
        IPage<Link> page = new Page<>(pageNumber, pageSize);
        return page(page,Wrappers.<Link>lambdaQuery()
                .like(StringUtils.isNotBlank(link.getName()), Link::getName, link.getName())
                .like(StringUtils.isNotBlank(link.getUrl()), Link::getUrl, link.getUrl())
                .eq(Objects.nonNull(link.getStatus()), Link::getStatus, link.getStatus())
                .orderByDesc(Link::getCreateTime));
    }

    @CacheEvict(value = "link", allEntries = true)
    public boolean deleteBatch(List<Integer> ids) {
        return removeByIds(ids);
    }

    @Cacheable(value = "link", key = "'list'")
    public List<Link>  simpleList() {
        return super.lambdaQuery().select(Link::getName,Link::getUrl).eq(Link::getStatus, CoreConst.STATUS_VALID).list();
    }

    public Long toAuditNum() {
        return super.lambdaQuery().eq(Link::getStatus,CoreConst.ZERO).count();
    }
}
