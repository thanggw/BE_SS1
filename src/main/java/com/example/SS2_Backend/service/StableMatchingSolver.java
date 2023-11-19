package com.example.SS2_Backend.service;

import com.example.SS2_Backend.dto.request.StableMatchingProblemDTO;
import com.example.SS2_Backend.dto.response.Progress;
import com.example.SS2_Backend.dto.response.Response;
import com.example.SS2_Backend.model.GameSolutionInsights;
import com.example.SS2_Backend.model.MatchingSolution;
import com.example.SS2_Backend.model.StableMatching.Individual;
import com.example.SS2_Backend.model.StableMatching.StableMatchingProblem;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class StableMatchingSolver {
    SimpMessageSendingOperations simpMessagingTemplate;
    private static final int RUN_COUNT_PER_ALGORITHM = 10; // for insight running, each algorithm will be run for 10 times


    public ResponseEntity<Response> solveStableMatching(StableMatchingProblemDTO request) {


        StableMatchingProblem problem = new StableMatchingProblem(new ArrayList<Individual>(), "BCA", "ABC");

        problem.setFitnessFunction(request.getFitnessFunction());
        problem.setSpecifiedAlgorithm(request.getSpecifiedAlgorithm());
        problem.setPopulationSize(request.getPopulationSize());     // number of individual

        problem.setCompositeWeightFunction(request.getCompositeWeightFunction());
        problem.setEvolutionRate(request.getEvolutionRate());
        problem.setMaximumExecutionTime(request.getMaximumExecutionTime());

        long startTime = System.currentTimeMillis();
//        log.info("Running algorithm: " + request.getSpecifiedAlgorithm() + "...");
        // solve the problem
        NondominatedPopulation results = solveProblem(
                problem,
                request.getSpecifiedAlgorithm(),
                request.getPopulationSize(),
                request.getFitnessFunction(),

                request.getCompositeWeightFunction(),
                request.getEvolutionRate(),
                request.getMaximumExecutionTime()
        );
//        problem.setFitnessFunction(request.getFitnessFunction());
//        problem.setSpecifiedAlgorithm(request.getSpecifiedAlgorithm());
//        problem.setPopulationSize(request.getPopulationSize());     // number of individual
//
//        problem.setCompositeWeightFunction(request.getCompositeWeightFunction());
        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .message("Solve game theory problem successfully!")
                        .data(problem)
                        .build()
        );
    }

    private NondominatedPopulation solveProblem(StableMatchingProblem problem,
                                                String specifiedAlgorithm,
                                                int populationSize,
                                                String fitnessFunction,
                                                String compositeWeightFunction,
                                                int evolutionRate,
                                                int maximumExecutionTime) {
        NondominatedPopulation results = null;
        return results;
    }



    public ResponseEntity<Response> getProblemResultInsights(StableMatchingProblemDTO request, String sessionCode) {
//        log.info("Received request: " + request);
        String[] algorithms = {"NSGAII", "NSGAIII", "eMOEA", "PESA2", "VEGA"};



        simpMessagingTemplate.convertAndSendToUser(sessionCode, "/progress", createProgressMessage("Initializing the problem..."));
        StableMatchingProblem problem = new StableMatchingProblem(new ArrayList<Individual>(), "BCA", "ABC");

        problem.setCompositeWeightFunction(request.getCompositeWeightFunction());
        problem.setFitnessFunction(request.getFitnessFunction());
        problem.setSpecifiedAlgorithm(request.getSpecifiedAlgorithm());

        problem.setPopulationSize(request.getPopulationSize());
        problem.setEvolutionRate(request.getEvolutionRate());
        problem.setMaximumExecutionTime(request.getMaximumExecutionTime());

        GameSolutionInsights gameSolutionInsights = initGameSolutionInsights(algorithms);

        int runCount = 1;
        int maxRunCount = algorithms.length * RUN_COUNT_PER_ALGORITHM;
        // solve the problem with different algorithms and then evaluate the performance of the algorithms
//        log.info("Start benchmarking the algorithms...");
        simpMessagingTemplate.convertAndSendToUser(sessionCode, "/progress", createProgressMessage("Start benchmarking the algorithms..."));

        for (String algorithm : algorithms) {
//            log.info("Running algorithm: " + algorithm + "...");
            for (int i = 0; i < RUN_COUNT_PER_ALGORITHM; i++) {
                System.out.println("Iteration: " + i);
                long start = System.currentTimeMillis();

                NondominatedPopulation results = solveProblem(
                        problem,
                        request.getSpecifiedAlgorithm(),
                        request.getPopulationSize(),
                        request.getFitnessFunction(),

                        request.getEvolutionRate(),
                        request.getCompositeWeightFunction(),
                        request.getMaximumExecutionTime()
                );

                long end = System.currentTimeMillis();

                double runtime = (double) (end - start) / 1000;
                double fitnessValue = getFitnessValue(results);

                // send the progress to the client
                String message = "Algorithm " + algorithm + " finished iteration: #" + (i + 1) + "/" + RUN_COUNT_PER_ALGORITHM;
                Progress progress = createProgress(message, runtime, runCount, maxRunCount);
                System.out.println(progress);
                simpMessagingTemplate.convertAndSendToUser(sessionCode, "/progress", progress);
                runCount++;

                // add the fitness value and runtime to the insights
                gameSolutionInsights.getFitnessValues().get(algorithm).add(fitnessValue);
                gameSolutionInsights.getRuntimes().get(algorithm).add(runtime);


            }

        }
//        log.info("Benchmarking finished!");
        simpMessagingTemplate.convertAndSendToUser(sessionCode, "/progress", createProgressMessage("Benchmarking finished!"));

        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .message("Get problem result insights successfully!")
                        .data(gameSolutionInsights)
                        .build()
        );
    }

    private GameSolutionInsights initGameSolutionInsights(String[] algorithms) {
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

    private Progress createProgressMessage(String message) {
        return Progress.builder()
                .inProgress(false) // this object is just to send a message to the client, not to show the progress
                .message(message)
                .build();
    }

    private Progress createProgress(String message, Double runtime, Integer runCount, int maxRunCount) {
        int percent = runCount * 100 / maxRunCount;
        int minuteLeff = (int) Math.ceil(((maxRunCount - runCount) * runtime) / 60); // runtime is in seconds
        return Progress.builder()
                .inProgress(true) // this object is just to send to the client to show the progress
                .message(message)
                .runtime(runtime)
                .minuteLeft(minuteLeff)
                .percentage(percent)
                .build();
    }

    private static double getFitnessValue(NondominatedPopulation result) {

        Solution solution = result.get(0);
        double fitnessValue = solution.getObjective(0);
        return fitnessValue;

    }


}
