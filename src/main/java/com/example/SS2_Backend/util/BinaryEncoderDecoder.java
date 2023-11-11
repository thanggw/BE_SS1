package com.example.SS2_Backend.util;

import java.util.Arrays;

public class BinaryEncoderDecoder {
    // Encode array of integers to a binary string
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

    public static void main(String[] args) {
        int[] originalArray = {3, 10, 2, 6, 8, 1, 11, 0, 4, 7, 5, 9};

        // Encode array to binary string
        String encodedString = binEncode(originalArray);
        System.out.println("Encoded Binary String: " + encodedString);

        // Decode binary string back to array
        int[] decodedArray = binDecode(encodedString);
        System.out.println("Decoded Array: " + Arrays.toString(decodedArray));
    }
}
