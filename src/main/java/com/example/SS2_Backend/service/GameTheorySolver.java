package com.example.SS2_Backend.service;

import com.example.SS2_Backend.dto.request.GameTheoryProblemDTO;
import com.example.SS2_Backend.dto.response.Response;
import com.example.SS2_Backend.model.GameSolution;
import com.example.SS2_Backend.model.GameTheoryProblem;
import com.example.SS2_Backend.model.NormalPlayer;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.BinaryIntegerVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameTheorySolver {

    public ResponseEntity<Response> solveGameTheory(GameTheoryProblemDTO gameTheoryProlem) {

        GameTheoryProblem problem = new GameTheoryProblem();
        problem.setSpecialPlayer(gameTheoryProlem.getSpecialPlayer());
        problem.setNormalPlayers(gameTheoryProlem.getNormalPlayers());
        problem.setFitnessFunction(gameTheoryProlem.getFitnessFunction());
        problem.setDefaultPayoffFunction(gameTheoryProlem.getDefaultPayoffFunction());
        problem.setConflictSet(gameTheoryProlem.getConflictSet());

        // solve using NSGA-II
        NondominatedPopulation results = new Executor()
                .withProblem(problem)
                .withAlgorithm("NSGAII")
                .withMaxEvaluations(5000)
                .distributeOnAllCores()
                .run();

        // format the output
        GameSolution gameSolution = formatSolution(problem, results);

        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .message("Solve game theory problem successfully!")
                        .data(gameSolution)
                        .build()
        );
    }

    private GameSolution formatSolution(GameTheoryProblem problem, NondominatedPopulation result) {
        Solution solution = result.get(0);
        GameSolution gameSolution = new GameSolution();

        double fitnessValue = solution.getObjective(0);
        gameSolution.setFitnessValue(fitnessValue);


        List<NormalPlayer> players = problem.getNormalPlayers();
        List<GameSolution.Player> gameSolutionPlayers = new ArrayList<>();

        // loop through all players and get the strategy chosen by each player
        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            NormalPlayer normalPlayer = players.get(i);

            //get the index of the strategy chosen by the player
            BinaryIntegerVariable chosenStrategyIndex = (BinaryIntegerVariable) solution.getVariable(i);
            double strategyPayoff = normalPlayer.getStrategyAt(chosenStrategyIndex.getValue()).getPayoff();

            String playerName = getPlayerName(normalPlayer, i);
            String strategyName = getStrategyName(chosenStrategyIndex.getValue(), normalPlayer, i);

            GameSolution.Player gameSolutionPlayer = GameSolution.Player.builder()
                    .playerName(playerName)
                    .strategyName(strategyName)
                    .payoff(strategyPayoff)
                    .build();

            gameSolutionPlayers.add(gameSolutionPlayer);

        }

        gameSolution.setPlayers(gameSolutionPlayers);
        return gameSolution;
    }

    public  String getPlayerName(NormalPlayer normalPlayer, int index) {
        String playerName = normalPlayer.getName();
        if (playerName == null) {
            playerName = String.format("Player %d", index);
        }

        return playerName;
    }

    public String getStrategyName(int chosenStrategyIndex, NormalPlayer normalPlayer, int index) {
        String strategyName = normalPlayer.getStrategies().get(chosenStrategyIndex).getName();
        if (strategyName == null) {
            strategyName = String.format("Strategy %d", index);
        }

        return strategyName;
    }
}
