package com.example.SS2_Backend.controller;

import com.example.SS2_Backend.dto.request.GameTheoryProblemDTO;
import com.example.SS2_Backend.dto.response.Response;
import com.example.SS2_Backend.service.GameTheorySolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {

    @Autowired
    private GameTheorySolver gameTheorySolver;

   @PostMapping("/game-theory-solver")
    public ResponseEntity<Response> solveGameTheory(@RequestBody GameTheoryProblemDTO gameTheoryProlem) {
        return gameTheorySolver.solveGameTheory(gameTheoryProlem);
    }
}
