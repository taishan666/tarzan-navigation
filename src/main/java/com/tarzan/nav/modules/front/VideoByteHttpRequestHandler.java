package com.tarzan.nav.modules.front;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
@Component
public class VideoByteHttpRequestHandler extends ResourceHttpRequestHandler {
    public final static String ATTR_FILE = "NON-STATIC-FILE";

    @Override
    protected Resource getResource(HttpServletRequest request) {
        final byte[] bytes = (byte[]) request.getAttribute(ATTR_FILE);
        return new ByteArrayResource(bytes);
    }
}
