package com.linzi.daily.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

import java.math.BigInteger;
import java.util.Date;

public class DeviceUtils {
    private static final String ALPHANUMERIC = "23456789ABCDEFGHJKMNPQRSTUVWXYZ";
    private static final int BASE_31 = ALPHANUMERIC.length();
    private static final String SALT = "9vyNQmnC";

    public static String encode(long number, String salt) {
        BigInteger bigInt = BigInteger.valueOf(number);
        StringBuilder encoded = new StringBuilder();
        BigInteger base = BigInteger.valueOf(BASE_31);
        while (bigInt.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divmod = bigInt.divideAndRemainder(base);
            bigInt = divmod[0];
            int charIndex = divmod[1].intValue();
            encoded.append(shiftChar(ALPHANUMERIC.charAt(charIndex), salt));
        }
        return encoded.reverse().toString();
    }

    public static long decode(String encoded, String salt) {
        BigInteger number = BigInteger.ZERO;
        BigInteger base = BigInteger.valueOf(BASE_31);
        for (int i = encoded.length() - 1; i >= 0; i--) {
            char c = encoded.charAt(i);
            int charIndex = ALPHANUMERIC.indexOf(shiftCharBack(c, salt));
            number = number.add(BigInteger.valueOf(charIndex).multiply(base.pow(encoded.length() - 1 - i)));
        }
        return number.longValue();
    }

    private static char shiftChar(char c, String salt) {
        int shift = salt.hashCode() % BASE_31;
        int newIndex = (ALPHANUMERIC.indexOf(c) + shift) % BASE_31;
        return ALPHANUMERIC.charAt(newIndex);
    }

    private static char shiftCharBack(char c, String salt) {
        int shift = salt.hashCode() % BASE_31;
        int newIndex = (ALPHANUMERIC.indexOf(c) - shift + BASE_31) % BASE_31;
        return ALPHANUMERIC.charAt(newIndex);
    }

    public static void main(String[] args) {
        for(int i=0;i<100;i++) {
            String productCode = RandomUtil.randomString(ALPHANUMERIC, 4).toLowerCase();
            System.out.println("productCode：" + productCode);
            long input = Long.parseLong(DateUtil.format(new Date(), "yyMM") + RandomUtil.randomNumbers(7));
            System.out.println("Source：" + input);
            String encoded = encode(input, SALT);
            String deviceCode = (productCode + encoded).toLowerCase();
            System.out.println("deviceCode: " + deviceCode + " length：" + deviceCode.length());
            long decoded = decode(encoded, SALT);
            System.out.println("Decoded: " + decoded);
        }
    }
}
