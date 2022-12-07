package com.tarzan.nacigation.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tarzan liu
 * @date 2020/4/18 12:28 下午
 */
@Slf4j
@UtilityClass
public class FileUploadUtil {

    private final Pattern PATTERN = Pattern.compile("\\\\", Pattern.LITERAL);

    public String uploadLocal(MultipartFile mf, String uploadPath) {
        String res = "";
        try {
            String fileName = null;
            String nowdayStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String uploadFullPath = uploadPath + File.separator + nowdayStr + File.separator;
            File fileDir = new File(uploadFullPath);
            // 创建文件根目录
            if (!fileDir.exists() && !fileDir.mkdirs()) {
                log.error("创建文件夹失败: {}", uploadFullPath);
                return null;
            }
            // 获取文件名
            String orgName = mf.getOriginalFilename();
            fileName = orgName.substring(0, orgName.lastIndexOf('.')) + '_' + System.currentTimeMillis() + orgName.substring(orgName.indexOf('.'));

            String savePath = fileDir.getPath() + File.separator + fileName;
            File savefile = new File(savePath);
            FileCopyUtils.copy(mf.getBytes(), savefile);
            res = nowdayStr + File.separator + fileName;
            if (res.contains("\\")) {
                res = PATTERN.matcher(res).replaceAll(Matcher.quoteReplacement("/"));
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return res;
    }
}
