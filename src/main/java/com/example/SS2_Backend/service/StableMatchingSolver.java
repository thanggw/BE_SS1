package com.example.SS2_Backend.service;
import com.example.SS2_Backend.dto.request.GameTheoryProblemDTO;
import com.example.SS2_Backend.dto.request.StableMatchingProblemDTO;
import com.example.SS2_Backend.dto.response.Progress;
import com.example.SS2_Backend.dto.response.Response;
import com.example.SS2_Backend.model.GameSolution;
import com.example.SS2_Backend.model.GameTheoryProblem;
import com.example.SS2_Backend.model.StableMatching.StableMatchingProblem;
import org.moeaframework.core.NondominatedPopulation;
import org.springframework.http.ResponseEntity;

public class StableMatchingSolver {
    public ResponseEntity<Response> solveStableMatching(StableMatchingSolver request) {

        StableMatchingProblem problem = new StableMatchingProblem(
                request.
        );

        long startTime = System.currentTimeMillis();
        log.info("Running algorithm: " + request.getAlgorithm() + "...");

        // solve the problem
        NondominatedPopulation results = solveProblem(problem,
                request.getAlgorithm(),
                request.getGeneration(),
                request.getPopulationSize(),
                request.getDistributedCores(),
                request.getMaxTime()
        );
        long endTime = System.currentTimeMillis();
        double runtime = ((double) (endTime - startTime) / 1000 / 60);
        runtime = Math.round(runtime * 100.0) / 100.0;

        log.info("Algorithm: " + request.getAlgorithm() + " finished in " + runtime + " minutes");

        // format the output
        log.info("Preparing the solution ...");
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

    public static ResponseEntity<Response> getJson(StableMatchingProblemDTO request){

        System.out.println(request.toString());
        GameSolution gameSolution = new GameSolution();
        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .message("Solve game theory problem successfully!")
                        .data(gameSolution)
                        .build()
        );

    }

}
