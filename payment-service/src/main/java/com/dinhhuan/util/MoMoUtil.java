package com.dinhhuan.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

public class MoMoUtil {

    public static String hashAllFields(Map<String, String> fields, String secretKey) {
        // Tạo raw signature string
        TreeMap<String, String> sortedFields = new TreeMap<>(fields);
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : sortedFields.entrySet()) {
            if (!sb.isEmpty()) sb.append("&");
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }

        return MoMoConfig.hmacSHA256(secretKey, sb.toString());
    }
}