package com.example.SS2_Backend.model.StableMatching;

import lombok.Getter;

import java.util.Arrays;

/**
 * Match Data Structure for Match:One to Many Stable Matching Problem
 * [individual1] => [individual2, individual3, individual4, ...]
 */

public class MatchSet implements MatchItem {
    @Getter
    private final int Individual1Index;
    private final int[] IndividualMatches;
    public MatchSet(int Individual, int[] IndividualMatches){
        this.Individual1Index = Individual;
        this.IndividualMatches = IndividualMatches;
    }
    public int[] getIndividualMatches(){
        return this.IndividualMatches;
    }
    @Override
    public int getIndividual2Index(){
        return 0;
    }
    public String toString(){
        return "[" + Individual1Index + "] => " + Arrays.toString(IndividualMatches);
    }
}
