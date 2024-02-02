package com.tarzan.nav.modules.admin.service.biz;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.admin.entity.biz.NoticeEntity;
import com.tarzan.nav.modules.admin.mapper.biz.NoticeMapper;
import com.tarzan.nav.utils.DateUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author TARZAN
 */
@Service
public class NoticeService extends ServiceImpl<NoticeMapper, NoticeEntity> {

    @Cacheable(value = "notice", key = "'simple'")
    public List<NoticeEntity> simpleList() {
        return super.lambdaQuery().select(NoticeEntity::getId,NoticeEntity::getTitle,NoticeEntity::getCreateTime).eq(NoticeEntity::getStatus, CoreConst.STATUS_VALID)
                .gt(NoticeEntity::getEndTime, DateUtil.now()).list();
    }
}
