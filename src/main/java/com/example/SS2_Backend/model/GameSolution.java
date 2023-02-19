package com.example.SS2_Backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameSolution {
    private double fitnessValue;
    private List<Player> players;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Player {
        private String playerName;
        private String strategyName;
        private double payoff;
    }
}
