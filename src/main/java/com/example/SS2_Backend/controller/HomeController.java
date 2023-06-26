package com.example.SS2_Backend.controller;

import com.example.SS2_Backend.dto.request.GameTheoryProblemDTO;
import com.example.SS2_Backend.dto.response.Response;
import com.example.SS2_Backend.service.GameTheorySolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class HomeController {

    @Autowired
    private GameTheorySolver gameTheorySolver;

    @PostMapping("/game-theory-solver")
    public ResponseEntity<Response> solveGameTheory(@RequestBody GameTheoryProblemDTO gameTheoryProblem) {
        return gameTheorySolver.solveGameTheory(gameTheoryProblem);
    }

    @PostMapping("/problem-result-insights/{sessionCode}")
    public ResponseEntity<Response> getProblemResultInsights(@RequestBody GameTheoryProblemDTO gameTheoryProblem, @PathVariable String sessionCode) {
       return gameTheorySolver.getProblemResultInsights(gameTheoryProblem, sessionCode);
    }
}