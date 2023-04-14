package com.tarzan.navigation.common.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author tarzan
 */
@Data
@Component
@ConfigurationProperties(prefix = "tarzan")
public class TarzanProperties {
    private String shiroKey;
    private String backupDir;
    private Boolean previewEnabled=false;
}
