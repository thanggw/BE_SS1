package com.example.SS2_Backend.dto.request;

import com.example.SS2_Backend.model.StableMatching.Individual;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StableMatchingProblemDTO {
    private String problemName;
    private ArrayList<Individual> Individuals;
    private String[] allPropertyNames;
    private String compositeWeightFunction;
    private String fitnessFunction;
    private String specifiedAlgorithm;
    private int populationSize;
    private int evolutionRate;
    private int maximumExecutionTime;

    // Getter for individual list
    public Individual getIndividual(int index) {
        return Individuals.get(index);
    }

    public int getNumberOfIndividuals(){
        return Individuals.size();
    }


    public String toString() {
        return "Matching_Theory_Problem {" +
                ", ProblemName= " + problemName + '\'' +
                ", Population= " + Individuals.size() +
                ", PropertyName= " + java.util.Arrays.toString(allPropertyNames) +
                ", CompositeWeightFunction= '" + compositeWeightFunction + '\'' +
                ", fitnessFunction= '" + fitnessFunction + '\'' +
                ", SpecifiedAlgorithm= " + specifiedAlgorithm + '\'' +
                ", PopulationSize= " + populationSize +
                ", EvolutionRate= " +evolutionRate +
                ", MaximumExecutionTime" + maximumExecutionTime +
                "}";
    }
}