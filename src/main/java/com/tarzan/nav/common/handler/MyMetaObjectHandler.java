package com.tarzan.nav.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.tarzan.nav.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", DateUtil.now(), metaObject);
        this.setFieldValByName("updateTime", DateUtil.now(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", DateUtil.now(), metaObject);
    }
}


