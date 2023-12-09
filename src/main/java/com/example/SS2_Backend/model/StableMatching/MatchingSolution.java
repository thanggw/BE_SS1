package com.example.SS2_Backend.model.StableMatching;

import com.example.SS2_Backend.dto.request.IndividualDeserializer;
import com.example.SS2_Backend.dto.response.ComputerSpecs;
import com.example.SS2_Backend.util.ComputerSpecsUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingSolution {
    private Matches matches;
    private double fitnessValue;
    private double runtime;
    private ComputerSpecs computerSpecs;
    private String algorithm;
    public ComputerSpecs getComputerSpecs() {
        return ComputerSpecsUtil.getComputerSpecs();
    }
    @JsonDeserialize(contentUsing = IndividualDeserializer.class)
    private ArrayList<Individual> Individuals;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Pair {
        private String Individual1Name;
        private String Individual2Name;
        private double PairScore;
    }
}
