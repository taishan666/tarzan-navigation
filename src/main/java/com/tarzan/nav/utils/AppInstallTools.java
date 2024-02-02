package com.tarzan.nav.utils;

import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.admin.service.sys.SysConfigService;
import com.tarzan.nav.modules.admin.service.sys.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author tarzan
 */
@Slf4j
@Component
@AllArgsConstructor
public class AppInstallTools {

    private final UserService userService;
    private final SysConfigService sysConfigService;
    private final JdbcTemplate jdbcTemplate;

    public void initData() {
        if(sysConfigService.count()==0L){
            log.info("初始化数据开始");
            InputStream dbIos = this.getClass().getResourceAsStream("/db/data.sql");
            try {
                assert dbIos != null;
                InputStreamReader reader = new InputStreamReader(dbIos, StandardCharsets.UTF_8);
                BufferedReader in = new BufferedReader(reader);
                String txt = FileCopyUtils.copyToString(in);
                jdbcTemplate.execute(txt);
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.info("初始化数据完毕");
        }
        if (userService.count()>0L) {
            CoreConst.IS_REGISTERED.set(true);
        }
    }


}
