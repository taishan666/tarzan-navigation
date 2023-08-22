package com.tarzan.nav.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tarzan
 */
public class MapUtil {

    /**
     * 根据map中的value大小进行排序【由大到小】
     */
    public static <K extends Comparable,V extends Comparable> Map<K, V> sortByValue(Map<K, V> map){
        return topNByValue(map, map.size());
    }

    public static <K extends Comparable,V extends Comparable> Map<K, V> topNByValue(Map<K, V> map,int num){
        //需要用LinkedHashMap排序
        HashMap<K, V> finalMap = new LinkedHashMap<K, V>();
        //取出map键值对Entry<K,V>，然后按照值排序，最后组成一个新的列表集合
        List<Map.Entry<K, V>> list = map.entrySet()
                .stream()
                .sorted((p2,p1)->p1.getValue().compareTo(p2.getValue()))
                .collect(Collectors.toList());
        if(list.size()<num){
            num=list.size();
        }
        //遍历集合，将排好序的键值对Entry<K,V>放入新的map并返回。
        list.subList(0,num).forEach(ele->finalMap.put(ele.getKey(), ele.getValue()));
        return finalMap;
    }

}
