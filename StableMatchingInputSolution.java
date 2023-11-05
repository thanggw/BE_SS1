package com.example.SS2_Backend.model;

import java.util.Vector;

public class StableMatchingInputSolution {
    private Vector<Integer> solutionVector;

    public StableMatchingInputSolution(int vectorLength) {
        solutionVector = new Vector<>();
        for (int i = 0; i < vectorLength; i++) {
            solutionVector.add(i);
        }
    }

    // Getters and setters
    public Vector<Integer> getSolutionVector() {
        return solutionVector;
    }

    public void setSolutionVector(Vector<Integer> solutionVector) {
        this.solutionVector = solutionVector;
    }

    StableMatchingProblem problem = new StableMatchingProblem();
    int n = problem.getIndividual().size();
    StableMatchingInputSolution inputSolution = new StableMatchingInputSolution(n);

    Vector<Integer> solutionVector = inputSolution.getSolutionVector();

}