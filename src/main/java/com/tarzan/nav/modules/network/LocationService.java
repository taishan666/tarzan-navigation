package com.tarzan.nav.modules.network;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.tarzan.nav.utils.StringUtil;

/**
 * @author tarzan
 */
public class LocationService {

    private static final String BAIDU_API ="https://qifu-api.baidubce.com/ip/geo/v1/district?ip=";

    public static String getLocation(String ip){
        String location="未知";
        String result= HttpUtil.get(BAIDU_API+ip);
        JSONObject json=JSONObject.parseObject(result);
        if("Success".equals(json.getString("code"))){
            JSONObject data=json.getJSONObject("data");
            String provCity= data.getString("prov")+ StringPool.DOT+data.getString("city");
            if(StringUtil.isNotBlank(provCity)){
                location=provCity;
            }
        }
        return location;
    }

    public static String getProvince(String ip){
        String result= HttpUtil.get(BAIDU_API+ip);
        JSONObject json=JSONObject.parseObject(result);
        if("Success".equals(json.getString("code"))){
            JSONObject data=json.getJSONObject("data");
            String prov= data.getString("prov");
            if(StringUtil.isNotBlank(prov)){
                return prov;
            }
        }
        return "未知";
    }

}
