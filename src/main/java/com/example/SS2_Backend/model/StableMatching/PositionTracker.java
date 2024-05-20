//package com.example.SS2_Backend.model.StableMatching;
//
//public class PositionTracker {
//    private int current = 0;
//    private final double[] scores;
//    private final int[] positions;
//
//    public PositionTracker(int size){
//            scores = new double[size];
//            positions = new int[size];
//    }
//
//
//    public void put(double value) {
//        this.scores[current] = value;
//        this.positions[current] = current;
//        this.current++;
//    }
//
//    /**
//     *
//     * @param key unique identifier of competitor instance
//     * @return score of the respective competitor
//     */
//    public double get(int key) throws NullPointerException {
//        try {
//            return scores[key];
//        } catch (NullPointerException e) {
//            System.err.println("Key " + key + " not found: " + e.getMessage());
//            return 0;
//        }
//    }
//
////    /**
////     * <i>THIS METHOD ONLY VALUABLE AFTER @method sortByValueDescending IS INVOKED </i>
////     * @param key unique identifier of competitor instance
////     * @return the position (rank) of the respective competitor in the preference list
////     *
////     */
////    public int getPositionOf(int key) {
////        return keys.indexOf(key);
////    }
//
//
//
//    public int getLeastPosition(int newNode, Integer[] currentNodes, int padding) {
//        int leastNode = newNode - padding;
//        for (int currentNode : currentNodes) {
//            if (this.scores[leastNode] > this.scores[currentNode - padding]) {
//                leastNode = currentNode - padding;
//            }
//        }
//        return leastNode + padding;
//    }
//
////    /**
////     * <i>THIS METHOD ONLY VALUABLE AFTER @method sortByValueDescending IS INVOKED </i>
////     * @param index position (rank best <-- 0, 1, 2, 3, ... --> worst) on the preference list
////     * @return score of the respective position on the preference list
////     */
////    public Double getPositionScore(int index) {
////        Integer key = keys.get(index);
////        return map.get(key);
////    }
//
//    // Size of the map
//    public int size() {
//        return positions.length;
//    }
//
//    public boolean isEmpty() {
//            return positions[this.positions.length - 1] == 0 && positions[0] == 0;
//    }
//
//    public void sortDescendingByScores() {
//        double[] cloneScores = scores.clone(); //copy to new array
//        int size = cloneScores.length;
//
//        // Build min heap
//        for (int i = size / 2 - 1; i >= 0; i--) {
//            heapify(cloneScores, size, i);
//        }
//
//        // Extract elements from heap one by one
//        for (int i = size - 1; i > 0; i--) {
//            // Move current root to end
//            double temp = cloneScores[0];
//            int tempPos = positions[0];
//
//            cloneScores[0] = cloneScores[i];
//            positions[0] = positions[i];
//
//            cloneScores[i] = temp;
//            positions[i] = tempPos;
//
//            // Call min heapify on the reduced heap
//            heapify(cloneScores, i, 0);
//        }
//    }
//
//    void heapify(double[] array, int heapSize, int rootIndex) {
//        int smallestIndex = rootIndex; // Initialize smallest as root
//        int leftChildIndex = 2 * rootIndex + 1; // left = 2*rootIndex + 1
//        int rightChildIndex = 2 * rootIndex + 2; // right = 2*rootIndex + 2
//
//        // If left child is smaller than root
//        if (leftChildIndex < heapSize && array[leftChildIndex] < array[smallestIndex]) {
//            smallestIndex = leftChildIndex;
//        }
//
//        // If right child is smaller than smallest so far
//        if (rightChildIndex < heapSize && array[rightChildIndex] < array[smallestIndex]) {
//            smallestIndex = rightChildIndex;
//        }
//
//        // If smallest is not root
//        if (smallestIndex != rootIndex) {
//            double swap = array[rootIndex];
//            int posSwap = positions[rootIndex];
//
//            array[rootIndex] = array[smallestIndex];
//            positions[rootIndex] = positions[smallestIndex];
//
//            array[smallestIndex] = swap;
//            positions[smallestIndex] = posSwap;
//
//            // Recursively heapify the affected sub-tree
//            heapify(array, heapSize, smallestIndex);
//        }
//    }
//
//
//
//    public static void main(String[] args) {
//        PositionTracker map = new PositionTracker(7);
//        map.put(5.0);
//        map.put(8.0);
//        map.put(2.0);
//        map.put(9.0);
//        map.put(1.0);
//        map.put(4.0);
//        map.put(9.1);
//        System.out.println(map);
//
//        map.sortDescendingByScores();
//
//        System.out.println("Sorted array is");
//        System.out.println(map);
//
//    }
//
//}
//
