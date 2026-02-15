package com.elaina.MySpringSource.utils;

/**
 * ClassName: StringUtil
 * Package: com.elaina.MySpringSource.utils
 * Description:
 *
 * @author 灰之魔女-伊蕾娜
 * @version 1.0
 * @create 2026/2/15 22:46
 */
public class StringUtil {
    public static String firstToLowerCase(String str) {
        return substringBefore(str, 1).toLowerCase().concat(substringAfter(str, 1));
    }

    public static String firstToUpperCase(String str) {
        return substringBefore(str, 1).toUpperCase().concat(substringAfter(str, 1));
    }

    public static String substringBefore(String str, int i) {
        return str.substring(0, i);
    }

    public static String substringAfter(String str, int i) {
        return str.substring(i, str.length());
    }
}
