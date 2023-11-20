package com.example.SS2_Backend.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Utils {
    public static Double formatDouble(double val){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.##", symbols);
        String formattedValue = df.format(val);
        return Double.parseDouble(formattedValue);
    }
    public static String binEncode(int[] array) {
        StringBuilder binaryString = new StringBuilder();
        for (int num : array) {
            String binaryRepresentation = Integer.toBinaryString(num);
            // Pad with leading zeros if needed
            String paddedBinary = String.format("%32s", binaryRepresentation).replace(' ', '0');
            binaryString.append(paddedBinary);
        }
        return binaryString.toString();
    }

    // Decode binary string to an array of integers
    public static int[] binDecode(String binaryString) {
        int[] result = new int[binaryString.length() / 32];
        for (int i = 0; i < binaryString.length(); i += 32) {
            String binarySubstr = binaryString.substring(i, i + 32);
            int num = Integer.parseInt(binarySubstr, 2);
            result[i / 32] = num;
        }
        return result;
    }
}
