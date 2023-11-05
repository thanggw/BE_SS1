package com.example.SS2_Backend.dto.request;

import com.example.SS2_Backend.model.Conflict;
import com.example.SS2_Backend.model.NormalPlayer;
import com.example.SS2_Backend.model.SpecialPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StableMatchingProblem {
    private String problemName;
    private ArrayList<Object> individual;
    private String[] allPropertyNames;
    private String compositeWeightFunction;
    private String fitnessFunction;
    private String specifiedAlgorithm;
    private int populationSize;
    private int evolutionRate;
    private int maximumExecutionTime;

    // Getter for individual list
    public List<Object> getIndividual() {
        return individual;
    }


    public String toString() {
        return "Matching_Theory_Problem {" +
                ", ProblemName= " + problemName + '\'' +
                ", Individual= " + individual +
                ", PropertyName= " + allPropertyNames +
                ", CompositeWeightFunction= '" + compositeWeightFunction + '\'' +
                ", fitnessFunction= '" + fitnessFunction + '\'' +
                ", SpecifiedAlgorithm= " + specifiedAlgorithm + '\'' +
                ", PopulationSize= " + populationSize +
                ", EvolutionRate= " +evolutionRate +
                ", MaximumExecutionTime" + maximumExecutionTime +
                "}";
    }
}