package com.tarzan.nav.modules.front;

import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;

/**
 * @description: 视频流和音频流加载设置
 * @return:
 * @author: Ming
 * @time: 2022/6/24
 */
@Component
public class VideoUrlHttpRequestHandler extends ResourceHttpRequestHandler {
    public final static String ATTR_FILE = "NON-STATIC-FILE";

    @Override
    protected Resource getResource(HttpServletRequest request) {
        final URL url = (URL) request.getAttribute(ATTR_FILE);
           return new FileUrlResource(url);
    }
}

