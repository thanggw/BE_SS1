package com.example.SS2_Backend.model.StableMatching;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Ví dụ khởi tạo và gọi hàm matchTriplets
public class Main {
    public static void main(String[] args) {
        // Khởi tạo danh sách ưu tiên cho A, B, C
        Map<Integer, List<int[]>> prefA = new HashMap<>();
        Map<Integer, List<int[]>> prefB = new HashMap<>();
        Map<Integer, List<int[]>> prefC = new HashMap<>();

        // Khởi tạo dữ liệu ví dụ cho A
        // A0 ưu tiên B0-C0, sau đó B1-C1
        prefA.put(0, Arrays.asList(new int[]{0, 0}, new int[]{1, 1}));
        // A1 ưu tiên B1-C1, sau đó B0-C0
        prefA.put(1, Arrays.asList(new int[]{1, 1}, new int[]{0, 0}));
        // A2 ưu tiên B0-C1, sau đó B1-C0
        prefA.put(2, Arrays.asList(new int[]{0, 1}, new int[]{1, 0}));

        // Khởi tạo dữ liệu ví dụ cho B
        // B0 ưu tiên A0-C0, sau đó A1-C1
        prefB.put(0, Arrays.asList(new int[]{0, 0}, new int[]{1, 1}));
        // B1 ưu tiên A1-C1, sau đó A0-C0
        prefB.put(1, Arrays.asList(new int[]{1, 1}, new int[]{0, 0}));

        // Khởi tạo dữ liệu ví dụ cho C
        // C0 ưu tiên A0-B0, sau đó A1-B1
        prefC.put(0, Arrays.asList(new int[]{0, 0}, new int[]{1, 1}));
        // C1 ưu tiên A1-B1, sau đó A0-B0
        prefC.put(1, Arrays.asList(new int[]{1, 1}, new int[]{0, 0}));

        // Khởi tạo đối tượng TripletStableMatching
        TripletStableMatching stableMatching = new TripletStableMatching(3, prefA, prefB, prefC);

        // Gọi hàm ghép cặp
        stableMatching.matchTriplets();

        // In kết quả ghép cặp
        stableMatching.printMatches();
    }
}


