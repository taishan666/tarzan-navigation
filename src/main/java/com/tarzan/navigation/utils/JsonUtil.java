package com.tarzan.navigation.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

@Slf4j
public class JsonUtil {

    public JsonUtil() {
    }

    public static <T> String toJson(T value) {
        try {
            return getInstance().writeValueAsString(value);
        } catch (Exception var2) {
            log.error(var2.getMessage(), var2);
            return null;
        }
    }


    public static <T> T parse(String content, Class<T> valueType) {
        try {
            return getInstance().readValue(content, valueType);
        } catch (Exception var3) {
            log.error(var3.getMessage(), var3);
            return null;
        }
    }



    public static boolean canSerialize(@Nullable Object value) {
        return value == null ? true : getInstance().canSerialize(value.getClass());
    }

    public static Map<String, Object> toMap(String content) {
        try {
            return (Map)getInstance().readValue(content, Map.class);
        } catch (IOException var2) {
            log.error(var2.getMessage(), var2);
            return null;
        }
    }

    public static <T> Map<String, T> toMap(String content, Class<T> valueTypeRef) {
        try {
            Map<String, Map<String, Object>> map = (Map)getInstance().readValue(content, new TypeReference<Map<String, T>>() {
            });
            Map<String, T> result = new HashMap(16);
            Iterator var4 = map.entrySet().iterator();

            while(var4.hasNext()) {
                Entry<String, Map<String, Object>> entry = (Entry)var4.next();
                result.put(entry.getKey(), toPojo((Map)entry.getValue(), valueTypeRef));
            }

            return result;
        } catch (IOException var6) {
            log.error(var6.getMessage(), var6);
            return null;
        }
    }

    public static <T> T toPojo(Map fromValue, Class<T> toValueType) {
        return getInstance().convertValue(fromValue, toValueType);
    }

    public static ObjectMapper getInstance() {
        return JsonUtil.JacksonHolder.INSTANCE;
    }

    private static class JacksonObjectMapper extends ObjectMapper {
        private static final long serialVersionUID = 4288193147502386170L;
        private static final Locale CHINA;

        public JacksonObjectMapper(ObjectMapper src) {
            super(src);
        }

        public JacksonObjectMapper() {

        }


        public ObjectMapper copy() {
            return new JsonUtil.JacksonObjectMapper(this);
        }

        static {
            CHINA = Locale.CHINA;
        }
    }

    private static class JacksonHolder {
        private static final ObjectMapper INSTANCE = new JsonUtil.JacksonObjectMapper();

        private JacksonHolder() {
        }
    }
}
