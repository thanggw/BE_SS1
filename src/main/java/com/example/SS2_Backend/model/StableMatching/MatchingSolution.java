package com.example.SS2_Backend.model.StableMatching;

import java.util.ArrayList;

import com.example.SS2_Backend.dto.response.ComputerSpecs;
import com.example.SS2_Backend.model.StableMatching.*;
import com.example.SS2_Backend.util.ComputerSpecsUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MatchingSolution {
    private Matches matches;
    private double fitnessValue;
    private double runtime;
    private ComputerSpecs computerSpecs;
    private String algorithm;
    public ComputerSpecs getComputerSpecs() {
        return ComputerSpecsUtil.getComputerSpecs();
    }

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
