package com.example.SS2_Backend.util;

import java.util.ArrayList;
import com.example.SS2_Backend.model.Pair;

public class MergeSortPair {
    public static void mergeSort(ArrayList<Pair> list) {
        if (list == null || list.size() <= 1) {
            return; // Nothing to sort
        }

        int middle = list.size() / 2;
        ArrayList<Pair> left = new ArrayList<>(list.subList(0, middle));
        ArrayList<Pair> right = new ArrayList<>(list.subList(middle, list.size()));

        mergeSort(left);
        mergeSort(right);

        merge(list, left, right);
    }

    private static void merge(ArrayList<Pair> list, ArrayList<Pair> left, ArrayList<Pair> right) {
        int leftIndex = 0;
        int rightIndex = 0;
        int listIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (left.get(leftIndex).getIndividual2Index() >= right.get(rightIndex).getIndividual2Index()) {
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

    public static void main(String[] args) {
        // Sample usage
        ArrayList<Pair> pairList = new ArrayList<>();
        pairList.add(new Pair(1, 5));
        pairList.add(new Pair(2, 3));
        pairList.add(new Pair(3, 1));
        pairList.add(new Pair(4, 7));

        mergeSort(pairList);

        for (Pair pair : pairList) {
            System.out.println(pair);
        }
    }
}
