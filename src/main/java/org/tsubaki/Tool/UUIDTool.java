package org.tsubaki.Tool;

import java.util.UUID;

public class UUIDTool {

    /**
     * 生成标准格式的随机 UUID 字符串
     * 例如: 550e8400-e29b-41d4-a716-446655440000
     * @return UUID字符串
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成无连字符的随机 UUID 字符串
     * 例如: 550e8400e29b41d4a716446655440000
     * @return 无连字符的UUID字符串
     */
    public static String generateUUIDWithoutHyphen() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}