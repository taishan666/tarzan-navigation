package com.tarzan.nav.modules.network;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;

/**
 * @author tarzan
 */
public class LocationService {

    public static String getLocation(String ip){
      String result= HttpUtil.get("https://qifu-api.baidubce.com/ip/geo/v1/district?ip="+ip);
        JSONObject json=JSONObject.parseObject(result);
        if("Success".equals(json.getString("code"))){
            JSONObject data=json.getJSONObject("data");
            return data.getString("prov")+"*"+data.getString("city");
        }
        return "未知";
    }

}
