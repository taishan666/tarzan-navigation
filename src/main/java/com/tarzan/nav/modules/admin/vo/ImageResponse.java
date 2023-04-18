package com.tarzan.nav.modules.admin.vo;

import com.tarzan.nav.common.constant.CoreConst;
import lombok.Data;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
public class ImageResponse {
    private String imageId;
    private String base64;
    private Long size;
    private String type;
    private Integer status;
    private String msg;

    private ImageResponse(String imageId, String type, String base64,Long size,Integer status,String msg) {
        this.imageId = imageId;
        this.type = type;
        this.size=size;
        this.base64 = base64;
        this.status = status;
        this.msg=msg;
    }

    private ImageResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public static ImageResponse success(String imageId, String type, String base64, Long size) {
        return new ImageResponse( imageId, type, base64, size, CoreConst.SUCCESS_CODE,"成功");
    }

    public static ImageResponse failed(String msg) {
        return new ImageResponse( CoreConst.FAIL_CODE, msg);
    }

}
