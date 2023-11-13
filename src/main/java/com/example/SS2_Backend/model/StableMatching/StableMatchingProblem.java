package com.example.SS2_Backend.model.StableMatching;

import java.util.*;

import lombok.Getter;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variable;
import org.moeaframework.core.variable.EncodingUtils;

import static com.example.SS2_Backend.util.MergeSortPair.mergeSort;
import com.example.SS2_Backend.util.StringExpressionEvaluator;

/**
Base case of Stable Matching Problem (One to One) : Number of Individuals in two sets are Equal (n1 = n2)
                                                  : Every Individual inside the Population have equal number of Properties
                                                  : Every Individual inside the Population have the same way to evaluate Partner
Wish to test this Class? Run "src.main.java.com.example.SS2_Backend.util.SampleDataGenerator.java"
 **/

public class StableMatchingProblem implements Problem {
    private final ArrayList<Individual> Individuals; // Storing Data of the Whole population
    @Getter
    private final int numberOfIndividual;
    @Getter
    private final int numberOfProperties;
    private final PreferenceLists preferenceLists; // Preference List of each Individual
    @Getter
    private final String compositeWeightFunction; // Function for Individual to Evaluate others based on her/his Weights
    @Getter
    private final String fitnessFunction; // Evaluate total Score of each Solution set

    //Constructor
    public StableMatchingProblem(ArrayList<Individual> Individuals, String compositeWeightFunction, String fitnessFunction) {
        this.Individuals = Individuals;
        this.numberOfIndividual = Individuals.size();
        this.numberOfProperties = Individuals.get(0).getNumberOfProperties();
        this.compositeWeightFunction = compositeWeightFunction;
        this.fitnessFunction = fitnessFunction;
        this.preferenceLists = getPreferences(); // Construct Preference List based on the given above Individuals data
    }

    //MOEA Solution Definition
    @Override
    public Solution newSolution() {
        Solution solution = new Solution(1, 1);
        // Randomize the order (from 0 to this.NumberOfIndividual)
        solution.setVariable(0, EncodingUtils.newPermutation(this.numberOfIndividual));
        return solution;
    }

    // Need to edit to evaluate each Individual based on the Composite Weight function (Expression Evaluator Utility) provided for each set (more complex problem)
    public List<Pair> getPreferenceOfIndividual(int index) {
        List<Pair> a = new ArrayList<>();
        int set = Individuals.get(index).getIndividualSet();
        for (int i = 0; i < numberOfIndividual; i++) {
            if(Individuals.get(i).getIndividualSet() != set){
                int totalScore = 0;
                for (int j = 0; j < numberOfProperties; j++) {
                    int PropertyValue = Individuals.get(i).getPropertyValue(j);
                    int PropertyWeight = Individuals.get(index).getPropertyWeight(j);
                    totalScore += PropertyValue*PropertyWeight;
                }
                // Reuse "Pair" Data Structure for Conveniency
                a.add(new Pair(i, totalScore));
            }
        }
        // Sort: Individuals with higher score than others sit on the top of the List
        mergeSort(a);
        // return Sorted list
        return a;
    }
    // Add to a complete List
    public PreferenceLists getPreferences() {
        PreferenceLists fullList = new PreferenceLists();
        for (int i = 0; i < numberOfIndividual; i++) {
            //System.out.println("Adding preference for Individual " + i );
            List<Pair> a = getPreferenceOfIndividual(i);
            //System.out.println(a.toString());
            fullList.add(a);
        }
        return fullList;
    }
    // Gale Shapley: Stable Matching Algorithm
    public Matches stableMatching(Variable var) {
        Matches matches = new Matches();
        Queue<Integer> unmatchedMales = new LinkedList<>();
        LinkedList<Integer> engagedFemale = new LinkedList<>();
        String s = var.toString();

        String[] decodedSolution = s.split(",");
        for (String token : decodedSolution) {
            try {
                // Convert each token to an Integer and add it to the queue
                int i = Integer.parseInt(token);
                if (Individuals.get(i).getIndividualSet() == 1) {
                    unmatchedMales.add(i);
                }
            } catch (NumberFormatException e) {
                // Handle invalid tokens (non-integer values)
                System.err.println("Skipping invalid token: " + token);
                return null;
            }
            //System.out.println("Solution: " + java.util.Arrays.toString(decodedSolution));
        }

        while (!unmatchedMales.isEmpty()) {
            int male = unmatchedMales.poll();
            //System.out.println("working on Individual:" + male);
            List<Pair> preferenceList = preferenceLists.getIndividualPreferenceList(male);
            //System.out.print("Hmm ... He prefer Individual ");
            for (int i = 0; i < preferenceList.size(); i++) {
                int female = preferenceList.get(i).getIndividual1Index();
                //System.out.println(female);
                if (!engagedFemale.contains(female)) {
                    engagedFemale.add(female);
                    matches.add(new Pair(male, female));
                    //System.out.println(male + female + " is now together");
                    break;
                } else {
                    int currentMale = Integer.parseInt(matches.findCompany(female));
                    //System.out.println("Oh no, she is with " + currentMale + " let see if she prefers " + male + " than " + currentMale );
                    if (isPreferredOver(male, currentMale, female, preferenceLists)) {
                        matches.disMatch(currentMale);
                        unmatchedMales.add(currentMale);
                        matches.add(new Pair(male, female));
                        //System.out.println("Hell yeah! " + female + " ditch the guy " + currentMale + " to be with " + male + "!");
                        break;
                    }
                    //else {
//                        unmatchedMales.add(male);
                        //System.out.println(male + " lost the game, back to the hood...");
                    //}
                }
            }
        }
        //System.out.println("Matching Complete!!");

        return matches;
    }

    // Stable Matching Algorithm Component: isPreferredOver
    private static boolean isPreferredOver(int male1, int male2, int female, PreferenceLists preferenceLists) {
        List<Pair> preference = preferenceLists.getIndividualPreferenceList(female);
        for (int i = 0; i < preference.size(); i++) {
            if (preference.get(i).getIndividual1Index() == male1) {
                return true;
            } else if (preference.get(i).getIndividual1Index() == male2) {
                return false;
            }
        }
        return false;
    }

    // Calculate each pair Satisfactory of the result produced By Stable Matching Algorithm
    private static int calculatePairSatisfactory(MatchItem pair, PreferenceLists preferenceLists) {
        int a = pair.getIndividual1Index();
        int b = pair.getIndividual2Index();
        int aScore=0;
        int bScore=0;
        for (Pair i:preferenceLists.getIndividualPreferenceList(a)) {
            if(i.getIndividual1Index()==b){
                aScore=i.getIndividual2Index();
            }
        }
        for (Pair i:preferenceLists.getIndividualPreferenceList(a)) {
            if(i.getIndividual1Index()==b){
                aScore=i.getIndividual2Index();
            }
        }
        return aScore + bScore;
    }

    // Evaluate
    public void evaluate(Solution solution) {
        //System.out.println("Evaluating...");
        Matches result = stableMatching(solution.getVariable(0));
        //System.out.println(solution.getVariable(1).toString());
        int fitnessScore = 0;
        if (result != null) {
            for (int i = 0; i < result.size(); i++) {
                fitnessScore += calculatePairSatisfactory(result.getPair(i), preferenceLists);
            }
        }
        solution.setAttribute("matches", result);
        solution.setObjective(0, -fitnessScore);

    }
    @Override
    public String getName() {
        return "Two Sided Stable Matching Problem";
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
        return 1;
    }

    private List<List<Pair>> getPreferenceLists(){
        return preferenceLists.getPreferenceList();
    }

    private String getPropertyNameOfIndex(int index){
        return Individuals.get(0).getPropertyName(index);
    }

    public int getPropertyValueOf(int index, int jndex){
        return Individuals.get(index).getPropertyValue(jndex);
    }

    public int getPropertyWeightOf(int index, int jndex){
        return Individuals.get(index).getPropertyWeight(jndex);
    }
    private static String fillWithChar(String character, int width) {
        if (character.length() != 1) {
            throw new IllegalArgumentException("Character must be a single character.");
        }

        String format = "%" + width + "s";
        return String.format(format, "").replace(' ', character.charAt(0));
    }

    public void printIndividuals(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i< this.numberOfProperties; i++){
                sb.append(String.format("%-16s| ", this.getPropertyNameOfIndex(i)));
        }
        String propName = sb.toString();
        sb.delete(0, sb.length());
        System.out.println("No | Set | Name                | " + propName );
        int width = this.numberOfProperties * 18 + 32;
        String filledString = fillWithChar("-", width);
        sb.append(filledString).append("\n");
        for (int i = 0; i<this.numberOfIndividual; i++){
            sb.append(String.format("%-3d| ", i));
            sb.append(String.format("%-4d| ", Individuals.get(i).getIndividualSet()));
            sb.append(String.format("%-20s| ", Individuals.get(i).getIndividualName()));
            StringBuilder ss = new StringBuilder();
            for (int j = 0; j< this.numberOfProperties; j++){
                ss.append(String.format("%-16s| ", this.getPropertyValueOf(i,j)));
            }
            sb.append(ss).append("\n");
            ss.delete(0, sb.length());
            ss.append(String.format("%33s", "Weight: | "));
            for (int j = 0; j< this.numberOfProperties; j++){
                ss.append(String.format("%-16s| ", this.getPropertyWeightOf(i,j)));
            }
            sb.append(ss).append("\n");
        }
        sb.append(filledString).append("\n");
        System.out.print(sb);
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

    public String printPreferenceLists() {
        return this.preferenceLists.toString();
    }
}
