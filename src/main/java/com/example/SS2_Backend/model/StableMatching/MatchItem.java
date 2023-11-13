package com.example.SS2_Backend.model.StableMatching;

public interface MatchItem {
    public int getIndividual1Index();
    public int getIndividual2Index();
    public int[] getIndividualMatches();
    public String toString();
}
