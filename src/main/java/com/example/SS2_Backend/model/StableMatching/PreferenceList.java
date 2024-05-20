package com.example.SS2_Backend.model.StableMatching;

import lombok.Getter;
<<<<<<< Updated upstream

import java.util.*;

import static com.example.SS2_Backend.model.StableMatching.PreferenceList.MergeSortPair.mergeSort;
=======
>>>>>>> Stashed changes
import static com.example.SS2_Backend.util.Utils.formatDouble;

/**
 * {rank,  score}, {r, s}, {r, s}, ...
 * access by indices
 */
@Getter
public class PreferenceList {
<<<<<<< Updated upstream
    private final List<IndexScore> preferenceList = new ArrayList<>();
    private double[] scores;

    public PreferenceList() {
    }

    public void transfer(int numberOfIndividual) {
        if (this.preferenceList.isEmpty()) return;
        this.scores = new double[numberOfIndividual];
        for (IndexScore value : this.preferenceList) {
            int index = value.getIndividualIndex();
            this.scores[index] = value.getScore();
        }
=======
    private final double[] scores;
    private final int[] positions;
    private int current;
    private final int padding;
    public PreferenceList(int size, int padding) {
            scores = new double[size];
            positions = new int[size];
            current = 0;
            this.padding = padding;
>>>>>>> Stashed changes
    }


    public int size() {
<<<<<<< Updated upstream
        return this.preferenceList.size();
    }

    public IndexScore getByIndex(int indexOnPreferenceList) {
        return this.preferenceList.get(indexOnPreferenceList);
=======
        return this.positions.length;
>>>>>>> Stashed changes
    }

    //public boolean isEmpty() {return this.preferenceList.isEmpty();}

<<<<<<< Updated upstream
    public IndexScore getIndexValueByKey(int indexOnIndividualList) {
            for (IndexScore indexScore : this.preferenceList) {
            if (indexScore.getIndividualIndex() == indexOnIndividualList) {
                return indexScore;
            }
        }
        return null;
    }

    public int getLeastNode(int newNode, Set<Integer> currentNodes) {
        int leastNode = newNode;
=======
    public double getScoreByIndex(int index) {
        try {
            return scores[index - this.padding];
        } catch (NullPointerException e) {
            System.err.println("Key " + index + " not found: " + e.getMessage());
            return 0;
        }
    }

    public int getLeastNode(int newNode, Integer[] currentNodes) {
        int leastNode = newNode - this.padding;
>>>>>>> Stashed changes
        for (int currentNode : currentNodes) {
            if (this.scores[leastNode] > this.scores[currentNode - this.padding]) {
                leastNode = currentNode - this.padding;
            }
        }
        return leastNode + this.padding;
    }

<<<<<<< Updated upstream
    public void add(IndexScore indexScore) {
        this.preferenceList.add(indexScore);
    }

    public void sort() {
        mergeSort(this.preferenceList);
=======
    public boolean isScoreGreater(int node, int nodeToCompare) {
        return this.scores[node - this.padding] > this.scores[nodeToCompare - this.padding];
    }

    /**
     * <i>THIS METHOD ONLY VALUABLE AFTER @method sortByValueDescending IS INVOKED </i>
     * @param position position (rank best <-- 0, 1, 2, 3, ... --> worst) on the preference list
     * @return unique identifier of the competitor instance that holds the respective position on the list
     */
    public int getIndexByPosition(int position) throws ArrayIndexOutOfBoundsException{
        try{
            return positions[position] + this.padding;
        }catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Position " + position + " not found: " + e.getMessage());
            return -1;
        }
    }


    /**
     *
     * @param score score of the respective competitor
     *
     * this method registers new competitor instance to the preference list (OrderedMap)
     */
    public void add(double score) {
        this.scores[current] = score;
        this.positions[current] = current;
        this.current++;
    }

    public void sort() {
        sortDescendingByScores();
>>>>>>> Stashed changes
    }

    public void sortDescendingByScores() {
        double[] cloneScores = scores.clone(); //copy to new array
        int size = cloneScores.length;

        // Build min heap
        for (int i = size / 2 - 1; i >= 0; i--) {
            heapify(cloneScores, size, i);
        }

        // Extract elements from heap one by one
        for (int i = size - 1; i > 0; i--) {
            // Move current root to end
            double temp = cloneScores[0];
            int tempPos = positions[0];

            cloneScores[0] = cloneScores[i];
            positions[0] = positions[i];

            cloneScores[i] = temp;
            positions[i] = tempPos;

            // Call min heapify on the reduced heap
            heapify(cloneScores, i, 0);
        }
    }

    void heapify(double[] array, int heapSize, int rootIndex) {
        int smallestIndex = rootIndex; // Initialize smallest as root
        int leftChildIndex = 2 * rootIndex + 1; // left = 2*rootIndex + 1
        int rightChildIndex = 2 * rootIndex + 2; // right = 2*rootIndex + 2

        // If left child is smaller than root
        if (leftChildIndex < heapSize && array[leftChildIndex] < array[smallestIndex]) {
            smallestIndex = leftChildIndex;
        }

        // If right child is smaller than smallest so far
        if (rightChildIndex < heapSize && array[rightChildIndex] < array[smallestIndex]) {
            smallestIndex = rightChildIndex;
        }

        // If smallest is not root
        if (smallestIndex != rootIndex) {
            double swap = array[rootIndex];
            int posSwap = positions[rootIndex];

            array[rootIndex] = array[smallestIndex];
            positions[rootIndex] = positions[smallestIndex];

            array[smallestIndex] = swap;
            positions[smallestIndex] = posSwap;

            // Recursively heapify the affected sub-tree
            heapify(array, heapSize, smallestIndex);
        }
    }

    @Override
    public String toString() {
<<<<<<< Updated upstream
        StringBuilder sb = new StringBuilder();
        sb.append(" [");
        for (int i = 0; i < preferenceList.size(); i++) {
            sb.append("Rank ").append(i + 1).append(": ");
            sb.append(preferenceList.get(i).getIndividualIndex()).append("\t");
            sb.append("Score: ");
            sb.append(formatDouble(preferenceList.get(i).getScore())).append(" |");
=======
        StringBuilder result = new StringBuilder("{");
        for (int i = 0; i < scores.length; i++) {
            int pos = positions[i];
            result.append("[").append(pos).append(" -> ").append(formatDouble(scores[pos])).append("]");
            if(i < scores.length - 1) result.append(", ");
>>>>>>> Stashed changes
        }
        result.append("}");
        return result.toString();
    }


<<<<<<< Updated upstream
        public IndexScore(int IndividualIndex, double score) {
            this.individualIndex = IndividualIndex;
            this.score = score;
        }

        public String toString() {
            return "Index: " + individualIndex + " Score: " + score;
        }
    }

    public static class MergeSortPair {
        public static void mergeSort(List<IndexScore> list) {
            if (list == null || list.size() <= 1) {
                return; // Nothing to sort
            }

            int middle = list.size() / 2;
            List<IndexScore> left = new ArrayList<>(list.subList(0, middle));
            List<IndexScore> right = new ArrayList<>(list.subList(middle, list.size()));

            mergeSort(left);
            mergeSort(right);

            merge(list, left, right);
        }

        private static void merge(List<IndexScore> list, List<IndexScore> left, List<IndexScore> right) {
            int leftIndex = 0;
            int rightIndex = 0;
            int listIndex = 0;

            while (leftIndex < left.size() && rightIndex < right.size()) {
                if (left.get(leftIndex).getScore() >= right.get(rightIndex).getScore()) {
                    list.set(listIndex, left.get(leftIndex));
                    leftIndex++;
                } else {
                    list.set(listIndex, right.get(rightIndex));
                    rightIndex++;
                }
                listIndex++;
            }

            while (leftIndex < left.size()) {
                list.set(listIndex, left.get(leftIndex));
                leftIndex++;
                listIndex++;
            }

            while (rightIndex < right.size()) {
                list.set(listIndex, right.get(rightIndex));
                rightIndex++;
                listIndex++;
            }
        }
    }

    public static void main(String[] args) {
        PreferenceList pref = new PreferenceList();
        pref.add(new IndexScore(1, 12.4));
        pref.add(new IndexScore(2, 62.4));
        pref.add(new IndexScore(3, 45.9));
        System.out.println(pref);
        pref.sort();
        pref.transfer(100);
        System.out.println(pref);
        //get leastNode
        IndexScore newNode = new IndexScore(6, 12.4);
        pref.add(newNode);
        System.out.println(pref);
        System.out.println(pref.getLeastNode(2, Set.of(1,2,3)));
    }
=======
//    public static void main(String[] args) {
//        PreferenceList pref = new PreferenceList(3);
//        pref.add(12.4);
//        pref.add(100.4);
//        pref.add(8.4);
//        System.out.println(pref);
//        pref.sort();
//        System.out.println(pref);
//        //get leastNode
////        IndexScore newNode = new IndexScore(6, 12.4);
////        pref.add(newNode);
//        System.out.println(pref);
//        Integer[] currentNodes = {1, 2, 3};
//        System.out.println(pref.getLeastNode(2, currentNodes, 0));
//        System.out.println(pref.getScoreByIndex(1, 0));
//        System.out.println(pref.getScoreByIndex(1, 0));
//        System.out.println(pref.getScoreByIndex(2, 0));
//        System.out.println(pref.getScoreByIndex(3, 0));
//    }
>>>>>>> Stashed changes
}
