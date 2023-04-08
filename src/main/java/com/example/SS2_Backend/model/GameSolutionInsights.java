package com.example.SS2_Backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameSolutionInsights {
    Map<String, List<Double>> fitnessValues;
    Map<String, List<Double>> runtimes;
}
