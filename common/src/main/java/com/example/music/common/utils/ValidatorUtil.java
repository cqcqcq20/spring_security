package com.example.music.common.utils;

import org.springframework.web.multipart.MultipartFile;

public class ValidatorUtil {

    /**
     * 判断value != null
     * @param value       字段值
     * @param express 这里不需要，只是为了参数统一
     * @return true or false
     */
    public static Boolean isNotNull(Object value, String express) {
        if (null == value) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public static Boolean isContentType(Object value,String express) {
        if (!(value instanceof MultipartFile)) {
            return Boolean.FALSE;
        }
        MultipartFile file = (MultipartFile) value;
        String[] types = express.split(",");
        String contentType = file.getContentType();
        for (String type : types) {
            if (type.equals(contentType)) {
                return true;
            }
        }
        return false;
    }
}
