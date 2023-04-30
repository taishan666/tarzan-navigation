package com.tarzan.nav.modules.network;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.tarzan.nav.utils.StringUtil;

/**
 * @author tarzan
 */
public class LocationService {

    public static String getLocation(String ip){
        String location="未知";
        String result= HttpUtil.get("https://qifu-api.baidubce.com/ip/geo/v1/district?ip="+ip);
        JSONObject json=JSONObject.parseObject(result);
        if("Success".equals(json.getString("code"))){
            JSONObject data=json.getJSONObject("data");
            String prov_city= data.getString("prov")+data.getString("city");
            if(StringUtil.isNotBlank(prov_city)){
                location=prov_city;
            }
        }
        return location;
    }

}
