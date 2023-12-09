package com.example.SS2_Backend.dto.request;

import com.example.SS2_Backend.model.StableMatching.Individual;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StableMatchingUIResult {
    @JsonDeserialize(contentUsing = IndividualDeserializer.class)
    private ArrayList<Individual> Individuals;
    private ArrayList<Double> coupleFitness;

    @JsonProperty("IndividualsList")
    public void setIndividuals(ArrayList<Individual> individuals) {
        this.Individuals = individuals;
    }
    @JsonProperty("coupleFitness")
    public void setCoupleFitness(ArrayList<Double> coupleFitness) {
        this.coupleFitness = coupleFitness;
    }
}
