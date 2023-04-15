package com.tarzan.nav.modules.admin.controller.biz;

import com.tarzan.nav.modules.admin.model.biz.BizImage;
import com.tarzan.nav.modules.admin.service.biz.ImageService;
import com.tarzan.nav.modules.admin.vo.ImageResponse;
import lombok.AllArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

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
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.')+1).toLowerCase();
        String base64;
        try {
            InputStream is=file.getInputStream();
            ByteArrayOutputStream os=new  ByteArrayOutputStream();
            Thumbnails.of(is).size(40, 40).toOutputStream(os);
            base64 = "data:image/"+suffix+";base64,"+ Base64.getEncoder().encodeToString(os.toByteArray());
            os.close();
            BizImage image=new BizImage();
            image.setBase64(base64);
            boolean flag=imageService.save(image);
            if(flag){
                return ImageResponse.success(image.getId(), suffix, base64, file.getSize());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ImageResponse.failed("图片上传失败！");
    }

}
