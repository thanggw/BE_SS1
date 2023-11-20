package com.example.SS2_Backend.model.StableMatching;

import java.util.ArrayList;
import java.util.List;

import com.example.SS2_Backend.dto.response.ComputerSpecs;
import com.example.SS2_Backend.util.ComputerSpecsUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingSolution {
    private List<List<PreferenceLists.IndexValue>> preferenceLists;
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
