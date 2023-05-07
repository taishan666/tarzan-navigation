package com.tarzan.nav.modules.front;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author tarzan
 */
@RestController
@RequestMapping("/video")
@AllArgsConstructor
@Slf4j
public class VideoPlayerController {

    private final VideoHttpRequestHandler videoHttpRequestHandler;
    private final VideoByteHttpRequestHandler videoByteHttpRequestHandler;

    @GetMapping("/player")
    public void getPlayResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Path path = Paths.get("C:\\Users\\liuya\\Desktop\\player.mp4");
        if (Files.exists(path)) {
            String mimeType = Files.probeContentType(path);
            if (!StringUtils.isEmpty(mimeType)) {
                response.setContentType(mimeType);
            }
            response.setContentLength((int) path.toFile().length());
            request.setAttribute(VideoHttpRequestHandler.ATTR_FILE, path);
            videoHttpRequestHandler.handleRequest(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }

    @GetMapping("/byte/player")
    public void getBytePlayResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String urlPath="https://aweme.snssdk.com/aweme/v1/play/?video_id=v0200fg10000ch5ld8rc77uclj45sk70&ratio=1080p&line=0";
        URL url=new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (connection.getResponseCode()==200) {
            response.setContentType("video/mp4");
            response.setCharacterEncoding("UTF-8");
            request.setAttribute(VideoByteHttpRequestHandler.ATTR_FILE,url.openStream());
            try {
                videoByteHttpRequestHandler.handleRequest(request, response);
            }catch (Exception e){
              log.error(e.getMessage());
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }


}
