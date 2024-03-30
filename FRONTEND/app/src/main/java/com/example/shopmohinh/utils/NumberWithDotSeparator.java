package com.example.shopmohinh.utils;

public class NumberWithDotSeparator {
    public static String formatNumberWithDotSeparator(int number) {
        String formattedNumber = String.format("%,d", number);
        return formattedNumber.replace(",", ".");
    }
}
