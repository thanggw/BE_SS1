package com.example.SS2_Backend.util;

import com.example.SS2_Backend.model.StableMatching.Individual;
import com.example.SS2_Backend.model.StableMatching.Matches;
import com.example.SS2_Backend.model.StableMatching.StableMatchingProblem;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;

import java.util.ArrayList;
import java.util.Random;

public class SampleDataGenerator {
    public static void main(String[] args) {
        // Generate Individuals data Randomly
        ArrayList<Individual> individuals = generateSampleIndividuals(12);

        // Create an Instance of StableMatchingProblem class with randomly generated data
        StableMatchingProblem problem = new StableMatchingProblem(individuals, "compositeWeightFunction", "fitnessFunction");
        // Print the whole Population
        problem.printIndividuals();
        // Run algorithm:
        NondominatedPopulation result = new Executor()
                .withProblem(problem)
                .withAlgorithm("NSGAII")
                .withMaxEvaluations(1000)
                .withProperty("populationSize", 200)
                .distributeOnAllCores()
                .run();
        // Number of Individuals inside this problem
        System.out.println("Number Of Individual: " + problem.getNumberOfIndividual());
        // Preference List Produced by Algorithm
        System.out.println("Preference List of All: \n" + problem.printPreferenceLists());
        for (Solution solution : result) {
            System.out.println("Randomized Individuals Order: " + solution.getVariable(0).toString());
            // Turn Solution:Attribute(Serializable Object) to Matches:"matches"(Instance of Matches Class)
            Matches matches = (Matches) solution.getAttribute("matches");
            // Prints matches
            System.out.println("Processed Matches" + matches.toString());
            // Prints fitness score of this Solution
            System.out.println("TotalScore: " + -solution.getObjective(0));
        }
    }

    public static ArrayList<Individual> generateSampleIndividuals(int numIndividuals) {
        ArrayList<Individual> individuals = new ArrayList<>();

        for (int i = 1; i <= numIndividuals; i++) {
            String individualName = "Individual" + i;
            int individualSet;
            if(i <= numIndividuals/2){
                individualSet = 0;
            }else{
                individualSet = 1;
            }
            Individual individual = new Individual(individualName, individualSet);

            // Add some sample properties (you can customize this part)
            for (int j = 0; j < 6; j++) {
                String propertyName = "Property" + j;
                int propertyValue = new Random().nextInt(20) + 1;
                int propertyWeight = new Random().nextInt(10) + 1; // Random weight between 1 and 10
                individual.setProperty(propertyName, propertyValue, propertyWeight);
            }

            individuals.add(individual);
        }


        return individuals;
    }
}

