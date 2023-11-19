package com.example.SS2_Backend.util;

import com.example.SS2_Backend.model.StableMatching.Individual;
import com.example.SS2_Backend.model.StableMatching.Matches;
import com.example.SS2_Backend.model.StableMatching.Requirement.OneBoundRequirement;
import com.example.SS2_Backend.model.StableMatching.Requirement.Requirement;
import com.example.SS2_Backend.model.StableMatching.Requirement.TwoBoundRequirement;
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
<<<<<<< HEAD
        // Generate Individuals data Randomly
        ArrayList<Individual> individuals = generateSampleIndividuals(12,4);

        String[] propNames = {"Prop1", "Prop2", "Prop3", "Prop4"};

        // Create an Instance of StableMatchingProblem class with randomly generated data
        StableMatchingProblem problem = new StableMatchingProblem(individuals, propNames,
                "Default");

        // Print the whole Population
        System.out.println(
                "\n[ Randomly Generated Population ]\n"
        );
        problem.printIndividuals();

        // Run algorithm:
        long startTime = System.currentTimeMillis();
=======
        ArrayList<Individual> individuals = generateSampleIndividuals(12);

        // Create a StableMatchingProblem object with the generated data
        StableMatchingProblem problem = new StableMatchingProblem(individuals, "compositeWeightFunction", "fitnessFunction");
        System.out.println(problem);
        System.out.println(problem.getPropertyValueOf(10, 1)); //success
        System.out.println(problem.getPropertyWeightOf(10, 1)); // success
        if(problem.isPreferenceEmpty()){
            System.out.println("Preference failed to generate");
        }else{
            System.out.println("success");
            System.out.println(problem.printPreferenceLists());
        }
        System.out.println(problem.getNumberOfIndividual()); //success
        System.out.println(problem.printPreferenceLists()); //failed
        System.out.println(problem.isPreferenceEmpty()); // true -- Preference initializing failed

>>>>>>> Le-Thanh
        NondominatedPopulation result = new Executor()
                .withProblem(problem)
                .withAlgorithm("NSGAII")
                .withMaxEvaluations(5)
                .withProperty("populationSize", 5)
                .distributeOnAllCores()
                .run();
<<<<<<< HEAD
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
=======
        for (Solution solution : result) {
//            System.out.print(solution.getVariable(0).toString() + "\t\t|");
            System.out.print(-solution.getObjective(0) + "\t"); // Negate to show maximized objective
//            System.out.print(solution.getObjective(1));
            System.out.println();
        }
//        Solution solution = problem.newSolution();
//        System.out.println(solution.getVariable(0).toString());
//        Matches matches = problem.stableMatching(solution);
//        System.out.println(matches);
>>>>>>> Le-Thanh
    }

    public static ArrayList<Individual> generateSampleIndividuals(int numIndividuals, int numProps) {
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
<<<<<<< HEAD
            for (int j = 0; j < numProps; j++) {
                double propertyValue = new Random().nextDouble() * (70.0 - 20.0) + 20.0;
                // Random property Value (20 -> 50)
                int propertyWeight = new Random().nextInt(10) + 1; // Random property Weight (1 -> 10)
                String[] expression = {"","--", "++"};
                double propertyBound = new Random().nextDouble() * (70.0 - 20.0) + 20.0;
                double propertyBound2 = new Random().nextDouble() * (70.0 - 20.0) + 20.0;
                int randomType = new Random().nextInt(2) + 1;
                int randomExpression = new Random().nextInt(2) + 1;

                if(randomType == 1){
                    String[] requirement = {String.valueOf(propertyBound), expression[randomExpression]};
                    individual.setProperty(propertyValue, propertyWeight, requirement);
                }else{
                    String[] requirement = {String.valueOf(propertyBound), String.valueOf(propertyBound2)};
                    individual.setProperty(propertyValue, propertyWeight, requirement);
                }
=======
            for (int j = 0; j < 5; j++) { // Adding 5 sample properties for each individual
                String propertyName = "Property" + j;
                int propertyValue = new Random().nextInt(20) + 1;
                int propertyWeight = new Random().nextInt(10) + 1; // Random weight between 1 and 10
                individual.setProperty(propertyName, propertyValue, propertyWeight);
>>>>>>> Le-Thanh
            }

            individuals.add(individual);
        }

        return individuals;
    }
}

