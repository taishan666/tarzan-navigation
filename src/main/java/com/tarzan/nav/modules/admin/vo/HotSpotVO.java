package com.tarzan.nav.modules.admin.vo;

import lombok.Data;

import java.util.List;

/**
 * @author TARZAN
 */
@Data
public class HotSpotVO {
    private List<HotNewsVO> baiduHot;
    private List<HotNewsVO> weiboHot;
    private List<HotNewsVO> douYinHot;
    private List<HotNewsVO> jueJinHot;
    private List<HotNewsVO> cSDNHot;
}
