package com.tarzan.nav.modules.front;

import com.tarzan.nav.utils.IoUtil;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

/**
 * @author tarzan
 */
@Component
public class VideoByteHttpRequestHandler extends ResourceHttpRequestHandler {
    public final static String ATTR_FILE = "NON-STATIC-FILE";

    @Override
    protected Resource getResource(HttpServletRequest request) {
        InputStream is = (InputStream) request.getAttribute(ATTR_FILE);
        byte[] bytes = IoUtil.readToByteArray(is);
        return new ByteArrayResource(bytes);
    }

    @Override
    public boolean isUseLastModified(){
        return false;
    }
}
