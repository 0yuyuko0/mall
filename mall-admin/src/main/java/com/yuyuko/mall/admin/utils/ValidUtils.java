package com.yuyuko.mall.admin.utils;

public class ValidUtils {
    public static boolean isValidUsername(String username) {
        int[] codePoints = username.codePoints().toArray();
        if (codePoints.length > 14)
            return false;
        int chCnt = 0;
        for (int codePoint : codePoints)
            if (Character.isDigit(codePoint) || Character.isAlphabetic(codePoint) || codePoint == '_')
                ++chCnt;
            else if (codePoint >= 0x4e00 && codePoint <= 0x9f65)
                chCnt += 2;
            else
                return false;
        return chCnt > 0 && chCnt <= 14;
    }

    public static boolean isValidPassword(String password) {
        int[] codePoints = password.codePoints().toArray();
        if (codePoints.length > 14 || codePoints.length < 8)
            return false;
        for (int codePoint : codePoints)
            if (Character.isWhitespace(codePoint))
                return false;
            else if (codePoint >= 0x7f || codePoint <= 0x20)
                return false;
        return true;
    }

}
