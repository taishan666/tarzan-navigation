package com.tarzan.nav.common.listener;

import com.tarzan.nav.common.props.TarzanProperties;
import com.tarzan.nav.modules.aichat.service.ChatItemService;
import com.tarzan.nav.shiro.ShiroService;
import com.tarzan.nav.utils.AppInstallTools;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author tarzan
 * @date 2021-10-05
 */
@Slf4j
@Component
@AllArgsConstructor
@Order
public class StartedListener implements ApplicationListener<ApplicationStartedEvent> {

    private final AppInstallTools appInstallTools;
    private final ShiroService shiroService;
    private final TarzanProperties tarzanProperties;
    private final ChatItemService chatItemService;

    @Override
    public void onApplicationEvent(@NonNull ApplicationStartedEvent event) {
        appInstallTools.initData();
        shiroService.updatePermission();
        File backupDir=new File(tarzanProperties.getBackupDir());
        if(!backupDir.exists()){
            boolean flag=backupDir.mkdirs();
            if(!flag){
                log.error("系统文件夹生成失败");
            }
        }
      //  chatItemService.remove(null);
        printStartInfo(event);
    }

    /**
     * 打印信息
     */
    private void printStartInfo(ApplicationStartedEvent event) {
        ConfigurableApplicationContext context=event.getApplicationContext();
        Environment env = context.getEnvironment();
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String port = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (contextPath == null) {
            contextPath = "";
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Application is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + contextPath + "\n\t" +
                "External: \thttp://" + ip + ':' + port + contextPath + '\n' +
                "----------------------------------------------------------");
    }


}
