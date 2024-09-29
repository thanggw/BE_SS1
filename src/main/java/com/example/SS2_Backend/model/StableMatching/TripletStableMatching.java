package com.example.SS2_Backend.model.StableMatching;

import java.util.*;

public class TripletStableMatching {

    // Number of elements in each set
    int n;

    // Preference lists for each set
    Map<Integer, List<int[]>> prefA;
    Map<Integer, List<int[]>> prefB;
    Map<Integer, List<int[]>> prefC;

    // Matching results
    Map<Integer, int[]> matches; // Maps element in A -> [B, C]

    // Constructor to initialize preference lists and other structures
    public TripletStableMatching(int n, Map<Integer, List<int[]>> prefA, Map<Integer, List<int[]>> prefB, Map<Integer, List<int[]>> prefC) {
        this.n = n;
        this.prefA = prefA;
        this.prefB = prefB;
        this.prefC = prefC;
        this.matches = new HashMap<>();
    }

    // Kiểm tra xem A có thích B hơn ghép nối hiện tại không
    private boolean preferAOverCurrent(int a, int b) {
        // Lấy danh sách ưu tiên của A
        List<int[]> preferences = prefA.get(a);
        // Giả định matches.get(a) trả về một cặp (b hiện tại, c hiện tại)
        int currentB = matches.get(a)[0]; // Lấy B hiện tại mà A đang ghép nối

        // Duyệt qua danh sách ưu tiên của A
        for (int[] pair : preferences) {
            if (pair[0] == b) {
                return true; // A thích B mới hơn so với B hiện tại
            }
            if (pair[0] == currentB) {
                return false; // A thích B hiện tại hơn, không cần đổi
            }
        }
        return false; // Nếu không tìm thấy cả B và currentB, mặc định là không đổi
    }


    // Kiểm tra xem B có thích A hơn ghép nối hiện tại không
    private boolean preferBOverCurrent(int b, int a) {
        // Lấy danh sách ưu tiên của B
        List<int[]> preferences = prefB.get(b);
        int currentA = matches.get(b)[0];

        // Tìm vị trí của A và currentA trong danh sách ưu tiên của B
        for (int[] pair : preferences) {
            if (pair[0] == a) {
                return true; // B thích A hơn
            }
            if (pair[0] == currentA) {
                return false; // B thích currentA hơn
            }
        }
        return false; // Mặc định nếu không tìm thấy A hoặc currentA
    }

    // Tương tự với preferCOverCurrent
    private boolean preferCOverCurrent(int c, int a) {
        List<int[]> preferences = prefC.get(c);
        int currentA = matches.get(c)[1]; // Ghép nối hiện tại của C

        for (int[] pair : preferences) {
            if (pair[0] == a) {
                return true; // C thích A hơn
            }
            if (pair[0] == currentA) {
                return false; // C thích currentA hơn
            }
        }
        return false;
    }


    // Function to check if a triplet (A, B, C) is stable
    private boolean isStable(int a, int b, int c) {
        // Check if A prefers another pair (B', C') more than (B, C)
        for (int[] pair : prefA.get(a)) {
            if (pair[0] == b && pair[1] == c) break; // Current match is acceptable for A
            int bPrime = pair[0];
            int cPrime = pair[1];

            // Kiểm tra xem B' và C' có thích A hơn ghép nối hiện tại hay không
            if (matches.containsKey(bPrime) && preferBOverCurrent(bPrime, a) &&
                    matches.containsKey(cPrime) && preferCOverCurrent(cPrime, a)) {
                return false; // Không ổn định, vì cả B' và C' đều đồng ý thay đổi
            }
        }

        // Check if B prefers another pair (A', C') more than (A, C)
        for (int[] pair : prefB.get(b)) {
            if (pair[0] == a && pair[1] == c) break; // Current match is acceptable for B
            int aPrime = pair[0];
            int cPrime = pair[1];

            // Kiểm tra xem A' hoặc C' có thích B hơn ghép nối hiện tại không
            if (matches.containsKey(aPrime) && preferAOverCurrent(aPrime, b)) {
                return false; // Không ổn định, vì A' đồng ý thay đổi
            }
        }

        // Trường hợp không có bất ổn, trả về true
        return true;
    }


}
