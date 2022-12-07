package com.tarzan.nacigation.common.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author tarzan
 */
@Data
@ConfigurationProperties(prefix = "static")
public class StaticHtmlProperties {

    private String accessPathPattern;

    private String folder;
}
