package com.tarzan.nacigation.common.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author tarzan
 */
@Data
@Component
@ConfigurationProperties(prefix = "cms")
public class CmsProperties {
    private String shiroKey;
    private String themeDir;
    private String useTheme;
    private String backupDir;
    private Boolean embeddedRedisEnabled=true;
    private Integer embeddedRedisPort=6379;
    private String embeddedRedisPassword="123456";
    private Boolean previewEnabled=false;
}
