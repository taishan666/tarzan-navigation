package com.tarzan.nacigation.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.AesCipherService;
import java.io.*;
import java.security.Key;
import java.util.Base64;

/**
 * @author tarzan
 */
@Slf4j
public class ShiroAESKeyUtil {


    public static String getKey(String path){
        String key=null;
        try {
            File file =  new File(path);
            if (!file.exists()) {
                if(!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                key=genKey();
                IoUtil.write(key,new FileOutputStream(file));
            } else {
                FileInputStream fis=new FileInputStream(file);
                key=IoUtil.readToString(fis);
            }
        } catch (IOException e) {
            log.error("创建或读取shiroKey文件异常...");
        }
        return key;

    }


    public static String genKey(){
        AesCipherService aesCipherService = new AesCipherService();
        Key key = aesCipherService.generateNewKey();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

}
