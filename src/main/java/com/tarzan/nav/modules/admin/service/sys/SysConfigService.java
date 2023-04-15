package com.tarzan.nav.modules.admin.service.sys;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nav.modules.admin.mapper.sys.SysConfigMapper;
import com.tarzan.nav.modules.admin.model.sys.SysConfig;
import com.tarzan.nav.modules.admin.vo.SiteInfoVO;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class SysConfigService extends ServiceImpl<SysConfigMapper, SysConfig> {


    private Map<String, String> selectAll() {
        List<SysConfig> sysConfigs =list();
        Map<String, String> map = new HashMap<>(sysConfigs.size());
        for (SysConfig config : sysConfigs) {
            map.put(config.getSysKey(), config.getSysValue());
        }
        return map;
    }

    @Cacheable(value = "site", key = "'config'")
    public SiteInfoVO getInfo() {
        Map<String, String> map = this.selectAll();
        String jsonStr = JSON.toJSONString(map);
        return JSON.parseObject(jsonStr, SiteInfoVO.class);
    }

    @CacheEvict(value = "site", key = "'config'", allEntries = true)
    public boolean updateByKey(String key, String value) {
        if (getOne(Wrappers.<SysConfig>lambdaQuery().eq(SysConfig::getSysKey, key)) != null) {
            return update(Wrappers.<SysConfig>lambdaUpdate().eq(SysConfig::getSysKey, key).set(SysConfig::getSysValue, value));
        } else {
            return save(new SysConfig().setSysKey(key).setSysValue(value));
        }
    }

    @CacheEvict(value = "site", key = "'config'", allEntries = true)
    public void updateAll(Map<String, String> map) {
        map.forEach(this::updateByKey);
    }
}
