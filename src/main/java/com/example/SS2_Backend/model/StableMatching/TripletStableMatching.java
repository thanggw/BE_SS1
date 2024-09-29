package com.example.SS2_Backend.model.StableMatching;

import java.util.*;

public class TripletStableMatching {

    // Số lượng các cá thể trong mỗi set
    int n;

    // Danh sách ưu tiên cho mỗi set
    Map<Integer, List<int[]>> prefA;
    Map<Integer, List<int[]>> prefB;
    Map<Integer, List<int[]>> prefC;

    // Kết quả ghép nối
    Map<Integer, int[]> matches; // Mỗi phần tử trong A -> [B, C]

    // Constructor để khởi tạo các danh sách ưu tiên và các cấu trúc khác
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

    // Hàm để kiểm tra nếu bộ ba (A, B, C) có ổn định không
    private boolean isStable(int a, int b, int c) {
        // Kiểm tra xem A có thích cặp khác (B', C') hơn cặp (B, C) hiện tại hay không
        for (int[] pair : prefA.get(a)) {
            if (pair[0] == b && pair[1] == c) break; // Cặp hiện tại phù hợp với A
            int bPrime = pair[0];
            int cPrime = pair[1];

            // Kiểm tra xem B' và C' có thích A hơn ghép nối hiện tại hay không
            if (matches.containsKey(bPrime) && preferBOverCurrent(bPrime, a) &&
                    matches.containsKey(cPrime) && preferCOverCurrent(cPrime, a)) {
                return false; // Không ổn định, vì cả B' và C' đều đồng ý thay đổi
            }
        }

        // Kiểm tra xem B có thích cặp khác (A', C') hơn cặp (A, C) không
        for (int[] pair : prefB.get(b)) {
            if (pair[0] == a && pair[1] == c) break; // Cặp hiện tại phù hợp với B
            int aPrime = pair[0];
            int cPrime = pair[1];

            // Kiểm tra xem A' có thích B hơn ghép nối hiện tại không
            if (matches.containsKey(aPrime) && preferAOverCurrent(aPrime, b)) {
                return false; // Không ổn định, vì A' đồng ý thay đổi
            }
        }

        // Nếu không tìm thấy bất ổn nào, cặp này là ổn định
        return true;
    }

    // Hàm ghép cặp bộ ba
    public void matchTriplets() {
        // Tạo danh sách các cá thể chưa ghép cặp từ tập A
        Queue<Integer> freeA = new LinkedList<>();
        for (int a = 0; a < n; a++) {
            freeA.add(a);
        }

        // Tiến hành ghép nối cho đến khi không còn cá thể tự do trong A
        while (!freeA.isEmpty()) {
            int a = freeA.poll(); // Lấy cá thể A đang tự do
            List<int[]> preferences = prefA.get(a); // Lấy danh sách ưu tiên của A

            // Duyệt qua danh sách ưu tiên của A để tìm cặp (B, C)
            for (int[] pair : preferences) {
                int b = pair[0];
                int c = pair[1];

                // Kiểm tra nếu (a, b, c) là một bộ ba ổn định
                if (!matches.containsKey(a)) { // Nếu A chưa ghép cặp
                    if (isStable(a, b, c)) { // Kiểm tra tính ổn định
                        matches.put(a, new int[]{b, c}); // Lưu cặp ghép
                        matches.put(b, new int[]{a, c}); // Ghép B với A và C
                        matches.put(c, new int[]{a, b}); // Ghép C với A và B
                        break; // Kết thúc vòng lặp nếu tìm được bộ ba
                    }
                } else {
                    // Nếu A đã có ghép cặp, kiểm tra xem có thể thay thế với cặp mới không
                    int[] currentPair = matches.get(a);
                    int currentB = currentPair[0];
                    int currentC = currentPair[1];

                    // Nếu cặp mới (b, c) tốt hơn cặp hiện tại, thay thế
                    if (preferAOverCurrent(a, b)) {
                        freeA.add(currentB); // Đưa B hiện tại trở lại danh sách tự do
                        matches.put(a, new int[]{b, c});
                        matches.put(b, new int[]{a, c});
                        matches.put(c, new int[]{a, b});
                        break;
                    }
                }
            }
        }
    }

    // Hàm hiển thị kết quả ghép cặp
    public void printMatches() {
        for (Map.Entry<Integer, int[]> entry : matches.entrySet()) {
            int a = entry.getKey();
            int b = entry.getValue()[0];
            int c = entry.getValue()[1];
            System.out.println("A: " + a + ", B: " + b + ", C: " + c);
        }
    }
}
