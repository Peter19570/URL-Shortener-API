package com.peter.shortener.features.url.utils;

public final class Base62 {

    private static final String ALPHABET =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private Base62() {}

    public static String encode(long value) {
        if (value == 0) return "0";
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.append(ALPHABET.charAt((int) (value % 62)));
            value /= 62;
        }
        return sb.reverse().toString();
    }

    public static long decode(String code) {
        try {
            long result = 0;
            for (char c : code.toCharArray()) {
                int idx = ALPHABET.indexOf(c);
                if (idx < 0) throw new IllegalArgumentException("Invalid code");
                result = Math.addExact(Math.multiplyExact(result, 62L), idx);
            }
            return result;
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("Invalid code");
        }
    }
}
