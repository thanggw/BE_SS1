package com.example.SS2_Backend.util;

import com.example.SS2_Backend.model.StableMatching.Individual;
import com.example.SS2_Backend.model.StableMatching.Matches;
import com.example.SS2_Backend.model.StableMatching.StableMatchingProblem;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;

import java.util.ArrayList;
import java.util.Random;

/**
 * Stable Matching Problem Testing Space:
 */

public class SampleDataGenerator {
    public static void main(String[] args) {
        // Generate Individuals data Randomly
        ArrayList<Individual> individuals = generateSampleIndividuals(12);

        // Create an Instance of StableMatchingProblem class with randomly generated data
        StableMatchingProblem problem = new StableMatchingProblem(individuals,
                "Default",
                "Default");

        // Print the whole Population
        System.out.println(
                "\n[ Randomly Generated Population ]\n"
        );
        problem.printIndividuals();

        // Run algorithm:
        long startTime = System.currentTimeMillis();
        NondominatedPopulation result = new Executor()
                .withProblem(problem)
                .withAlgorithm("NSGAII")
                .withMaxEvaluations(1000)
                .withProperty("populationSize", 200)
                .distributeOnAllCores()
                .run();
        long endTime = System.currentTimeMillis();
        double runtime = ((double) (endTime - startTime) / 1000);
        runtime = Math.round(runtime * 100.0) / 100.0;

        // Number of Individuals inside this problem
        System.out.println("Number Of Individual: " + problem.getNumberOfIndividual());

        // Preference List Produced by Algorithm
        System.out.println(
                "\n[ Preference List Produced By the Program ]\n"
        );
        System.out.println(problem.printPreferenceLists());

        System.out.println(
                "\n[ Algorithm Output Solution ]\n"
        );
        for (Solution solution : result) {
            System.out.println("Randomized Individuals Input Order (by MOEA): " + solution.getVariable(0).toString());
            // Turn Solution:Attribute(Serializable Object) to Matches:"matches"(Instance of Matches Class)
            Matches matches = (Matches) solution.getAttribute("matches");
            // Prints matches
            System.out.println("Output Matches (by Gale Shapley):\n" + matches.toString());
            // Prints fitness score of this Solution
            System.out.println("Fitness Score: " + -solution.getObjective(0));
        }
        System.out.println("\nExecution time: " + runtime + " Second(s) with Algorithm: " + "NSGAII");
    }

    public static ArrayList<Individual> generateSampleIndividuals(int numIndividuals) {
        ArrayList<Individual> individuals = new ArrayList<>();

        for (int i = 1; i <= numIndividuals; i++) {
            String individualName = "Individual Name" + i;
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
                int propertyValue = new Random().nextInt(50) + 20; // Random property Value (20 -> 50)
                int propertyWeight = new Random().nextInt(10) + 1; // Random property Weight (1 -> 10)
                individual.setProperty(propertyName, propertyValue, propertyWeight);
            }

            individuals.add(individual);
        }

        return individuals;
    }
}

