package com.example.SS2_Backend.service;
import com.example.SS2_Backend.dto.request.StableMatchingProblemDTO;
import com.example.SS2_Backend.dto.response.Progress;
import com.example.SS2_Backend.dto.response.Response;
import com.example.SS2_Backend.model.GameSolution;
import org.moeaframework.core.NondominatedPopulation;
import org.springframework.http.ResponseEntity;

public class StableMatchingSolver {
    public ResponseEntity<Response> solveStableMatching(StableMatchingProblemDTO request) {

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
