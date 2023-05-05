package com.tarzan.nav.modules.front;


import cn.hutool.http.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/video")
public class VideoPlayerController {

  //  String url = "C:\\Users\\liuya\\Desktop\\下载.mp4";

    @Autowired
    private VideoHttpRequestHandler videoHttpRequestHandler;

    @Autowired
    private VideoUrlHttpRequestHandler videoUrlHttpRequestHandler;
    @Autowired
    private VideoByteHttpRequestHandler videoByteHttpRequestHandler;
    @Autowired
    private VideoIOHttpRequestHandler videoIOHttpRequestHandler;

    @GetMapping("/player")
    public void getPlayResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
       // Path path = Paths.get(url);
        URL url=new URL("https://aweme.snssdk.com/aweme/v1/play/?video_id=v0200fg10000ch5ld8rc77uclj45sk70&ratio=1080p&line=0");
        Path path = Paths.get(url.toURI());
        if (Files.exists(path)) {
     /*       String mimeType = Files.probeContentType(path);
            System.out.println(mimeType);
            if (!StringUtils.isEmpty(mimeType)) {
                response.setContentType("video/mp4");
            }*/
            response.setContentType("video/mp4");
            request.setAttribute(VideoHttpRequestHandler.ATTR_FILE, HttpUtil.downloadBytes("https://aweme.snssdk.com/aweme/v1/play/?video_id=v0200fg10000ch5ld8rc77uclj45sk70&ratio=1080p&line=0"));
            videoHttpRequestHandler.handleRequest(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }
    @GetMapping("/url/player")
    public void getUrlPlayResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
        URL url=new URL("https://aweme.snssdk.com/aweme/v1/play/?video_id=v0200fg10000ch5ld8rc77uclj45sk70&ratio=1080p&line=0");
        //获取链接
        if (true) {
            response.setContentType("video/mp4");
            request.setAttribute(VideoHttpRequestHandler.ATTR_FILE, url);
            videoUrlHttpRequestHandler.handleRequest(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }

    @GetMapping("/byte/player")
    public void getBytePlayResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String url="https://aweme.snssdk.com/aweme/v1/play/?video_id=v0200fg10000ch5ld8rc77uclj45sk70&ratio=1080p&line=0";
        //获取链接
        if (true) {
        //    response.setContentType("video/mp4");
            request.setAttribute(VideoHttpRequestHandler.ATTR_FILE,  HttpUtil.downloadBytes(url));
            videoByteHttpRequestHandler.handleRequest(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }

    @GetMapping("/io/player")
    public void getIOPlayResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String url="https://aweme.snssdk.com/aweme/v1/play/?video_id=v0200fg10000ch5ld8rc77uclj45sk70&ratio=1080p&line=0";
        //获取链接
        if (true) {
            response.setContentType("video/mp4");
           // request.setAttribute(VideoHttpRequestHandler.ATTR_FILE,  HttpUtil.(url));
            videoByteHttpRequestHandler.handleRequest(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }


    public static void main(String[] args) {

        String url="https://aweme.snssdk.com/aweme/v1/play/?video_id=v0200fg10000ch5ld8rc77uclj45sk70&ratio=1080p&line=0";
        HttpUtil.downloadFile(url,"C:\\Users\\liuya\\Desktop\\aa.mp4");
    }
}
