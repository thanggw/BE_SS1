package com.example.SS2_Backend.model.StableMatching;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.SS2_Backend.model.StableMatching.PreferenceList.MergeSortPair.mergeSort;
import static com.example.SS2_Backend.util.StringExpressionEvaluator.AfterTokenLength;
import static com.example.SS2_Backend.util.StringExpressionEvaluator.eval;
import static com.example.SS2_Backend.util.Utils.formatDouble;

/**
 * {rank,  score}, {r, s}, {r, s}, ...
 * access by indexes
 */
@Getter
public class PreferenceList {
    private final List<IndexValue> preferenceList = new ArrayList<>();
    private double[] scores;

    public PreferenceList() {
    }

    public IndexValue getByIndex(int indexOnPreferenceList) {
        return this.preferenceList.get(indexOnPreferenceList);
    }

    public void transform(int numberOfIndividual) {
        if (this.preferenceList.isEmpty()) return;
        this.scores = new double[numberOfIndividual];
        for (IndexValue value : this.preferenceList) {
            int index = value.getIndividualIndex();
            this.scores[index] = value.getValue();
        }
    }

    public int size() {
        return this.preferenceList.size();
    }

    //public boolean isEmpty() {return this.preferenceList.isEmpty();}

    public IndexValue getIndexValueByKey(int indexOnIndividualList) {
        for (IndexValue indexValue : this.preferenceList) {
            if (indexValue.getIndividualIndex() == indexOnIndividualList) {
                return indexValue;
            }
        }
        return null;
    }

    public int getLeastNode(int newNode, Set<Integer> currentNodes) {
        int leastNode = newNode;
        for (int currentNode : currentNodes) {
            if (this.scores[leastNode] > this.scores[currentNode]) {
                leastNode = currentNode;
            }
        }
        return leastNode;
    }

    public void add(IndexValue indexValue) {
        this.preferenceList.add(indexValue);
    }

    public void sort() {
        mergeSort(this.preferenceList);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" [");
        for (int i = 0; i < preferenceList.size(); i++) {
            sb.append("Rank ").append(i + 1).append(": ");
            sb.append(preferenceList.get(i).getIndividualIndex()).append("\t");
            sb.append("Score: ");
            sb.append(formatDouble(preferenceList.get(i).getValue())).append(" |");
        }
        sb.append("]\n");
        return sb.toString();
    }

    @Getter
    public static class IndexValue {
        private final int IndividualIndex;
        private final double Value;

        public IndexValue(int IndividualIndex, double Value) {
            this.IndividualIndex = IndividualIndex;
            this.Value = Value;
        }

        public String toString() {
            return "Index: " + IndividualIndex + " Score: " + Value;
        }
    }

    public static class MergeSortPair {
        public static void mergeSort(List<IndexValue> list) {
            if (list == null || list.size() <= 1) {
                return; // Nothing to sort
            }

            int middle = list.size() / 2;
            List<IndexValue> left = new ArrayList<>(list.subList(0, middle));
            List<IndexValue> right = new ArrayList<>(list.subList(middle, list.size()));

            mergeSort(left);
            mergeSort(right);

            merge(list, left, right);
        }

        private static void merge(List<IndexValue> list, List<IndexValue> left, List<IndexValue> right) {
            int leftIndex = 0;
            int rightIndex = 0;
            int listIndex = 0;

            while (leftIndex < left.size() && rightIndex < right.size()) {
                if (left.get(leftIndex).getValue() >= right.get(rightIndex).getValue()) {
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

    public static PreferenceList getPreferenceListByFunction(List<Individual> Individuals, int index, String function) {
        PreferenceList a = new PreferenceList();
        int set = Individuals.get(index).getIndividualSet();
        int numberOfIndividual = Individuals.size();
        for (int i = 0; i < numberOfIndividual; i++) {
            if (Individuals.get(i).getIndividualSet() != set) {
                StringBuilder tmpSB = new StringBuilder();
                for (int c = 0; c < function.length(); c++) {
                    char ch = function.charAt(c);
                    if (ch == 'P' || ch == 'p') {
                        // read next char then parse to int (index)
                        int subStringLength = AfterTokenLength(function, c);
                        int indexOfP = Integer.parseInt(function.substring(c + 1, c + 1 + subStringLength)) - 1;
                        double propertyValue = Individuals.get(i).getPropertyValue(indexOfP);
                        tmpSB.append(propertyValue);
                        c += subStringLength;
                    } else if (ch == 'W' || ch == 'w') {
                        //read next char then parse to int (index)
                        int subStringLength = AfterTokenLength(function, c);
                        int indexOfW = Integer.parseInt(function.substring(c + 1, c + 1 + subStringLength)) - 1;
                        int weight = Individuals.get(index).getPropertyWeight(indexOfW);
                        tmpSB.append(weight);
                        c += subStringLength;
                    } else if (ch == 'R' || ch == 'r') {
                        int subStringLength = AfterTokenLength(function, c);
                        int indexOfR = Integer.parseInt(function.substring(c + 1, c + 1 + subStringLength)) - 1;
                        int requirement = Individuals.get(index).getRequirement(indexOfR).getTargetValue();
                        tmpSB.append(requirement);
                        c += subStringLength;
                    } else {
                        //No occurrence of W/w/P/w
                        tmpSB.append(ch);
                    }
                }
                double totalScore = eval(tmpSB.toString());
                // Add
                a.add(new PreferenceList.IndexValue(i, totalScore));
            }
        }
        return a;
    }

    public static void main(String[] args) {
        PreferenceList pref = new PreferenceList();
        pref.add(new IndexValue(1, 12.4));
        pref.add(new IndexValue(2, 62.4));
        pref.add(new IndexValue(3, 45.9));
        System.out.println(pref);
        pref.sort();
        pref.transform(100);
        System.out.println(pref);
        //get leastNode
        IndexValue newNode = new IndexValue(6, 12.4);
        pref.add(newNode);
        System.out.println(pref);
        System.out.println(pref.getLeastNode(2, Set.of(1,2,3)));
    }
}
