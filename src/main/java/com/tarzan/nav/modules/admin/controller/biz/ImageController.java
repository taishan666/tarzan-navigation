package com.tarzan.nav.modules.admin.controller.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.admin.model.biz.BizImage;
import com.tarzan.nav.modules.admin.service.biz.ImageService;
import com.tarzan.nav.modules.admin.vo.ImageResponse;
import lombok.AllArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

/**
 * @author tarzan
 */
@RestController
@RequestMapping("image")
@AllArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ImageResponse upload(@RequestParam(value = "file", required = false) MultipartFile file)  {
        try {
            //获取文件md5，查找数据库，如果有，则不需要上传了
            String md5 = DigestUtils.md5Hex(file.getInputStream());
            String base64;
            BizImage image=imageService.getById(md5);
            String originalFilename = file.getOriginalFilename();
            assert originalFilename != null;
            String suffix = originalFilename.substring(originalFilename.lastIndexOf('.')+1).toLowerCase();
            if(Objects.isNull(image)){
                InputStream is=file.getInputStream();
                ByteArrayOutputStream os=new  ByteArrayOutputStream();
                Thumbnails.of(is).scale(1).toOutputStream(os);
                base64 = "data:image/"+suffix+";base64,"+ Base64.getEncoder().encodeToString(os.toByteArray());
                os.close();
                boolean flag=imageService.save(BizImage.builder().id(md5).base64(base64).build());
                if(flag){
                    return ImageResponse.success(md5, suffix, base64, file.getSize());
                }
            }else {
                base64=image.getBase64();
                return ImageResponse.success(md5, suffix, base64, file.getSize());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ImageResponse.failed("图片上传失败！");
    }


    @ResponseBody
    @PostMapping("/editor/upload")
    public JSONArray editorUpload(@RequestParam(value = "file[]", required = false) MultipartFile[] files) {
        JSONArray  array=new JSONArray();
        Arrays.asList(files).forEach(file->{
            ImageResponse responseVo = upload(file);
            JSONObject object=new JSONObject();
            if (CoreConst.SUCCESS_CODE.equals(responseVo.getStatus())) {
                object.put("error", 0);
                object.put("url", responseVo.getBase64());
                object.put("name",file.getOriginalFilename());
            }else{
                object.put("error", "上传失败！");
                object.put("url", responseVo.getBase64());
                object.put("name",file.getOriginalFilename());
            }
            array.add(object);
        });
        return array;
    }

}
