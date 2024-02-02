package com.tarzan.nav.modules.admin.service.biz;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nav.modules.admin.entity.biz.BizImageEntity;
import com.tarzan.nav.modules.admin.mapper.biz.ImageMapper;
import com.tarzan.nav.modules.admin.model.biz.BizImage;
import com.tarzan.nav.utils.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

/**
 * @author tarzan
 */
@Service
public class ImageService extends ServiceImpl<ImageMapper, BizImageEntity> {

    @Value("${server.port}")
    private Integer port;

    public BizImageEntity upload(String src){
        if(StringUtil.isBlank(src)){
            src="http://localhost:"+port+"/favicon.ico,image/ico";
        }
        byte[] bytes=HttpUtil.downloadBytes(src.split(",")[0]);
        String base64="data:"+src.split(",")[1]+";base64,"+ Base64.getEncoder().encodeToString(bytes);
       return uploadBase64(base64);
    }

    public BizImageEntity letterAvatar(String text){
        char letter=text.toCharArray()[0];
        String bg = toRGB(Color.BLUE.getRGB());
        if(Character.isDigit(letter)){
            bg = toRGB(Color.BLACK.getRGB());
        }
        if(Character.isLetter(letter)){
            bg = toRGB(Color.GREEN.getRGB());
        }
        if(isChineseChar(letter)){
            bg = toRGB(Color.ORANGE.getRGB());
        }
        String color = "#ffffff";
        String svg="<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" height=\"100\" width=\"100\"><rect fill=\""+bg+"\" x=\"0\" y=\"0\" width=\"100\" height=\"100\"></rect><text x=\"50\" y=\"50\" font-size=\"50\" text-copy=\"fast\" fill=\""+color+"\" text-anchor=\"middle\" text-rights=\"admin\" dominant-baseline=\"central\">"+letter+"</text></svg>";
        String base64= "data:image/svg+xml;base64,"+Base64.getEncoder().encodeToString(svg.getBytes(StandardCharsets.UTF_8));
        return uploadBase64(base64);
    }

    public static boolean isChineseChar(char c) {
        return String.valueOf(c).matches("[\u4e00-\u9fa5]");
    }

    /**
     * 方法描述: argb 转rgb
     *
     * @param color BufferedImage对象的rgb值
     * @return {@link String}
     */
    public static String toRGB(int color) {
        // 获取color(RGB)中R位
        int red = (color & 0xff0000) >> 16;
        // 获取color(RGB)中G位
        int green = (color & 0x00ff00) >> 8;
        // 获取color(RGB)中B位
        int blue = (color & 0x0000ff);
        return "rgb("+red + "," + green + "," + blue+")";
    }


    public BizImageEntity uploadBase64(String base64){
        String md5 = DigestUtils.md5Hex(base64);
        BizImageEntity oldImage=baseMapper.selectById(md5);
        if(Objects.isNull(oldImage)){
            BizImage newImage=BizImage.builder().id(md5).base64(base64).build();
            baseMapper.insert(newImage);
            return newImage;
        }else {
            return oldImage;
        }
    }

}
