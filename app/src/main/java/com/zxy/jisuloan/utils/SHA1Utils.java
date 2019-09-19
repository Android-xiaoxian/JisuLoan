package com.zxy.jisuloan.utils;

import java.security.MessageDigest;

/**
 * Create by Fang ShiXian
 * on 2019/8/22
 */
public class SHA1Utils {

    /*
     * 此方法主要用于生成一个字符串的sha1哈希值.
     *
     * @param str, 待哈希的字符串。 ×@return 输入字符串的哈希值。以十六进制字符串显示。
     */
    public static String generateHash(String str) {
        if (null == str || str.isEmpty()) {
            return null;
        }
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            // Generate sha1 value
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();

            // Convert to hex decimal format
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }

            return new String(buf);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

}
