package com.example.SS2_Backend.model;

import java.util.*;

import com.example.SS2_Backend.util.MergeSortPair;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;

import static com.example.SS2_Backend.util.MergeSortPair.mergeSort;
import static com.example.SS2_Backend.util.StringExpressionEvaluator.*;
public class StableMatchingProblem implements Problem {
    private final ArrayList<Individual> Individuals;
    private final int numberOfIndividual;
    private final int numberOfProperties;
    private final ArrayList<ArrayList<Pair>> preferenceLists;
    private final String compositeWeightFunction;
    private final String fitnessFunction;

    public StableMatchingProblem(ArrayList<Individual> Individuals, String compositeWeightFunction, String fitnessFunction) {
        this.Individuals = Individuals;
        this.compositeWeightFunction = compositeWeightFunction;
        this.fitnessFunction = fitnessFunction;
        this.preferenceLists = getPreferences();
        this.numberOfProperties = Individuals.get(0).getNumberOfProperties();
        this.numberOfIndividual = Individuals.size();
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(1, 1);
        solution.setVariable(0, EncodingUtils.newPermutation(Individuals.size()));
        return solution;
    }

    @Override
    public String getName() {
        return "Stable Matching Problem";
    }

    public String getCompositeWeightFunction() {
        return compositeWeightFunction;
    }

    public String getFitnessFunction() {
        return fitnessFunction;
    }
    public ArrayList<Pair> getPreferenceOfIndividual(int index) {
        ArrayList<Pair> a = new ArrayList<>();
        int set = Individuals.get(index).getIndividualSet();
        for (int i = 0; i < numberOfIndividual; i++) {
            if(Individuals.get(i).getIndividualSet() != set){
                int totalScore = 0;
                for (int j = 0; j < numberOfProperties; j++) {
                    int PropertyValue = Individuals.get(i).getPropertyValue(j);
                    int PropertyWeight = Individuals.get(index).getPropertyWeight(j);
                    totalScore += PropertyValue*PropertyWeight;
                }
                a.add(new Pair(i, totalScore));
            }
        }
        mergeSort(a);
        return a;
    }
    //the problem lies here
    public ArrayList<ArrayList<Pair>> getPreferences() {
        ArrayList<ArrayList<Pair>> fullList = new ArrayList<>();
        for (int i = 0; i < numberOfIndividual; i++) {
//          ArrayList<Pair> list = getPreferenceOfIndividual(i);
//
          System.out.println("Adding preference for Individual " + i );
          fullList.add(getPreferenceOfIndividual(i));
        }

//        ArrayList<Pair> pair = new ArrayList<>();
//        pair.add(new Pair(6,9));
//        pair.add(new Pair(4, 4));
//        pair.add(new Pair(4, 5));
//        fullList.add(pair);
//        fullList.add(pair);
//        fullList.add(pair);
//        fullList.add(pair);
        return fullList;
    }
    public int getNumberOfIndividual(){
        return numberOfIndividual;
    }
    public boolean isPreferenceEmpty(){
        return preferenceLists.isEmpty();
    }

    public int getNumberOfVariables() {
        return 1;
    }

    @Override
    public int getNumberOfObjectives() {
        return 1;
    }

    @Override
    public int getNumberOfConstraints() {
        return 0;
    }

    public ArrayList<ArrayList<Pair>> getPreferenceLists(){
        return preferenceLists;
    }

    public int getPropertyValueOf(int index, int jdex){
        return Individuals.get(index).getPropertyValue(jdex);
    }

    public int getPropertyWeightOf(int index, int jdex){
        return Individuals.get(index).getPropertyWeight(jdex);
    }

    private static Matches stableMatching(ArrayList<Individual> individuals, ArrayList<ArrayList<Pair>> preferenceLists, Solution solution) {
        Matches matches = new Matches();
        Queue<Integer> unmatchedMales = new LinkedList<>();
        LinkedList<Integer> engagedFemale = new LinkedList<>();
        String s = solution.getVariable(0).toString();

        String[] decodedSolution = s.split(",");
        for (String token : decodedSolution) {
            try {
                // Convert each token to an Integer and add it to the queue
                int i = Integer.parseInt(token);
                if (individuals.get(i).getIndividualSet() == 1) {
                    unmatchedMales.add(i);
                }
            } catch (NumberFormatException e) {
                // Handle invalid tokens (non-integer values)
                System.err.println("Skipping invalid token: " + token);
                return null;
            }
        }

        while (!unmatchedMales.isEmpty()) {
            int male = unmatchedMales.poll();
//            int female = findBestFemale(male, preferenceLists[male]);
            //int[] preferenceList = preferenceLists[male];
            ArrayList<Pair> preferenceList = preferenceLists.get(male);
            for (int i = 0; i < preferenceList.size(); i++) {
                int female = preferenceList.get(i).getIndividual1Index();

                if (!engagedFemale.contains(female)) {
                    engagedFemale.add(female);
                    matches.add(new Pair(male, female));
                } else {
                    if (matches.findCompany(female) != null) {
                        int currentMale = Integer.parseInt(matches.findCompany(female));
                        if (isPreferredOver(male, currentMale, female, preferenceLists)) {
                            matches.disMatch(currentMale);
                            unmatchedMales.add(currentMale);
                            matches.add(new Pair(male, female));
                        } else {
                            unmatchedMales.add(male);
                        }
                    }
                }
            }
        }

        return matches;
    }


    private static boolean isPreferredOver(int male1, int male2, int female, ArrayList<ArrayList<Pair>> preferenceLists) {
        ArrayList<Pair> preference = preferenceLists.get(female);
        for (int i = 0; i < preference.size(); i++) {
            if (preference.get(i).getIndividual1Index() == male1) {
                return true;
            } else if (preference.get(i).getIndividual1Index() == male2) {
                return false;
            }
        }
        return false;
    }


    private static int calculatePairSatisfactory(Pair pair, ArrayList<ArrayList<Pair>> preferenceLists) {
        int a = pair.getIndividual1Index();
        int b = pair.getIndividual2Index();
        int aScore = preferenceLists.get(a).get(b).getIndividual2Index();
        int bScore = preferenceLists.get(b).get(a).getIndividual2Index();
        return aScore + bScore;
    }

    public void evaluate(Solution solution) {
        System.out.println("Evaluating...");
        Matches result = stableMatching(Individuals, preferenceLists, solution);
        int fitnessScore = 0;
        if (result != null) {
            for (int i = 0; i < result.size(); i++) {
                fitnessScore += calculatePairSatisfactory(result.getPair(i), preferenceLists);
            }
        }
        solution.setObjective(0, -fitnessScore);
    }
    public void close() {
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < Individuals.size(); i++){
            sb.append(Individuals.get(i).toString()).append("\n");
        }
        return numberOfProperties + "\n" + fitnessFunction + "\n" + compositeWeightFunction + "\n" + sb;
    }

//    public String printPreferenceLists(){
//        StringBuilder sb = new StringBuilder();
//        for(int i=0;i<numberOfIndividual;i++){
//            ArrayList<Pair> list = preferenceLists.get(i);
//            sb.append("Individual ").append(i);
//            sb.append(" [");
//            for(int j=0;j<list.size();j++) {
//                sb.append("C").append(j+1).append(": ");
//                sb.append(list.get(j).getIndividual1Index()).append("\t");
//                sb.append("Tt: ");
//                sb.append(list.get(j).getIndividual2Index()).append("\t").append("|");
//            }
//            sb.append("]\n");
//        }
//        return sb.toString();
//    }
    public String printPreferenceLists() {
        StringBuilder sb = new StringBuilder();
        //sb.append("first").append(preferenceLists.size());
        for (int i = 0; i < preferenceLists.size(); i++) {
            sb.append("Individual ").append(i).append(" [");
            for (Pair pair : preferenceLists.get(i)) {
                sb.append("Individual ").append(pair.getIndividual1Index()).append(" Tt: ");
                sb.append(pair.getIndividual2Index()).append(" | ");
            }
            sb.append("]\n");
        }
        return sb.toString();
    }

}
