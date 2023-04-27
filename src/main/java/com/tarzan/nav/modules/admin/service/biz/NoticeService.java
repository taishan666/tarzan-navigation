package com.tarzan.nav.modules.admin.service.biz;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.admin.mapper.biz.NoticeMapper;
import com.tarzan.nav.modules.admin.model.biz.Notice;
import com.tarzan.nav.utils.DateUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author TARZAN
 */
@Service
public class NoticeService extends ServiceImpl<NoticeMapper, Notice> {

    @Cacheable(value = "notice", key = "'simple'")
    public List<Notice> simpleList() {
        return super.lambdaQuery().select(Notice::getId,Notice::getTitle,Notice::getCreateTime).eq(Notice::getStatus, CoreConst.STATUS_VALID)
                .gt(Notice::getEndTime, DateUtil.now()).list();
    }
}
