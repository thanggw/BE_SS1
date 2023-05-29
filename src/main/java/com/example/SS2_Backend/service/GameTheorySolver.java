package com.example.SS2_Backend.service;

import com.example.SS2_Backend.dto.request.GameTheoryProblemDTO;
import com.example.SS2_Backend.dto.response.Response;
import com.example.SS2_Backend.model.GameSolution;
import com.example.SS2_Backend.model.GameSolutionInsights;
import com.example.SS2_Backend.model.GameTheoryProblem;
import com.example.SS2_Backend.model.NormalPlayer;
import lombok.extern.slf4j.Slf4j;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.BinaryIntegerVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GameTheorySolver {


    public ResponseEntity<Response> solveGameTheory(GameTheoryProblemDTO request) {

        log.info("Received request: " + request);
        GameTheoryProblem problem = new GameTheoryProblem();
        problem.setDefaultPayoffFunction(request.getDefaultPayoffFunction());
        problem.setFitnessFunction(request.getFitnessFunction());
        problem.setSpecialPlayer(request.getSpecialPlayer());
        problem.setNormalPlayers(request.getNormalPlayers());
        problem.setConflictSet(request.getConflictSet());

        long startTime = System.currentTimeMillis();
        log.info("Running algorithm: " + request.getAlgorithm() + "...");

        // solve using NSGA-II
        NondominatedPopulation results = new Executor()
                .withProblem(problem)
                .withAlgorithm(request.getAlgorithm())
                .withMaxEvaluations(1) //TODO: user can specify the max evaluations
                .distributeOnAllCores()
                .run();

        long endTime = System.currentTimeMillis();
        double runtime = ((double) (endTime - startTime) / 1000 / 60);
        runtime = Math.round(runtime * 100.0) / 100.0;
        log.info("Algorithm: " + request.getAlgorithm() + " finished in " + runtime + " minutes");
        // format the output
        GameSolution gameSolution = formatSolution(problem, results);
        gameSolution.setAlgorithm(request.getAlgorithm());
        gameSolution.setRuntime(runtime);
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

    public ResponseEntity<Response> getProblemResultInsights(GameTheoryProblemDTO gameTheoryProblem) {
        log.info("Received request: " + gameTheoryProblem);
        String[] algorithms = {"NSGAII", "eMOEA", "PESA2", "VEGA"};
        GameTheoryProblem problem = new GameTheoryProblem();
        problem.setSpecialPlayer(gameTheoryProblem.getSpecialPlayer());
        problem.setDefaultPayoffFunction(gameTheoryProblem.getDefaultPayoffFunction());
        problem.setNormalPlayers(gameTheoryProblem.getNormalPlayers());
        problem.setFitnessFunction(gameTheoryProblem.getFitnessFunction());
        problem.setConflictSet(gameTheoryProblem.getConflictSet());


        GameSolutionInsights gameSolutionInsights = initGameSolutionInsights(algorithms);

        // solve the problem with different algorithms and then evaluate the performance of the algorithms
        log.info("Start benchmarking the algorithms...");
        for (String algorithm : algorithms) {
            log.info("Running algorithm: " + algorithm + "...");
            // benchmark the algorithm, by running it 10 times and get the all fitness values and runtimes
            for (int i = 0; i < 10; i++) {
                System.out.println("Iteration: " + i);
                long start = System.currentTimeMillis();
                NondominatedPopulation results = new Executor()
                        .withProblem(problem)
                        .withAlgorithm(algorithm)
                        .withMaxEvaluations(1) //TODO: user can specify the max evaluations
                        .distributeOnAllCores()
                        .run();
                long end = System.currentTimeMillis();

                double runtime = (double) (end - start) / 1000;
                double fitnessValue = getFitnessValue(results);

//                System.out.println("Algorithm: " + algorithm + ", Iteration: #" + (i+1) +  ", Fitness value: " + fitnessValue + ", Runtime: " + runtime + "s");
                gameSolutionInsights.getFitnessValues().get(algorithm).add(fitnessValue);
                gameSolutionInsights.getRuntimes().get(algorithm).add(runtime);
            }

        }
        log.info("Benchmarking finished!");

        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .message("Get problem result insights successfully!")
                        .data(gameSolutionInsights)
                        .build()
        );
    }

    private static GameSolutionInsights initGameSolutionInsights(String[] algorithms) {
        GameSolutionInsights gameSolutionInsights = new GameSolutionInsights();
        Map<String, List<Double>> fitnessValueMap = new HashMap<>();
        Map<String, List<Double>> runtimeMap = new HashMap<>();

        gameSolutionInsights.setFitnessValues(fitnessValueMap);
        gameSolutionInsights.setRuntimes(runtimeMap);

        for (String algorithm : algorithms) {
            fitnessValueMap.put(algorithm, new ArrayList<>());
            runtimeMap.put(algorithm, new ArrayList<>());
        }

        return gameSolutionInsights;
    }
    private static double getFitnessValue(NondominatedPopulation result) {

        Solution solution = result.get(0);
        double fitnessValue = solution.getObjective(0);
        return fitnessValue;

    }

}
