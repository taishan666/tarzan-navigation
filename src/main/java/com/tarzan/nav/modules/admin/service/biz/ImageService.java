package com.tarzan.nav.modules.admin.service.biz;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nav.modules.admin.mapper.biz.ImageMapper;
import com.tarzan.nav.modules.admin.model.biz.BizImage;
import com.tarzan.nav.utils.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Objects;

/**
 * @author tarzan
 */
@Service
public class ImageService extends ServiceImpl<ImageMapper, BizImage> {


    public BizImage upload(String src){
        if(StringUtil.isNotBlank(src)){
            byte[] bytes=HttpUtil.downloadBytes(src.split(",")[0]);
            String md5 = DigestUtils.md5Hex(bytes);
            BizImage oldImage=baseMapper.selectById(md5);
            if(Objects.isNull(oldImage)){
                String base64="data:"+src.split(",")[1]+";base64,"+ Base64.getEncoder().encodeToString(bytes);
                BizImage newImage=BizImage.builder().id(md5).base64(base64).build();
                baseMapper.insert(newImage);
                return newImage;
            }else {
                return oldImage;
            }
        }
        return null;
    }
}
