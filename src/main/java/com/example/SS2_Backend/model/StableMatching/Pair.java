package com.example.SS2_Backend.model.StableMatching;

/**
 * Match Data Structure for Match:One to One Stable Matching Problem
 * [IndividualA] <=> [IndividualB]
 */
public class Pair implements MatchItem {
    private final int individual1Index;
    private final int individual2Index;

    public Pair(int individual1Index, int individual2Index){
        this.individual1Index = individual1Index;
        this.individual2Index = individual2Index;
    }

    public int getIndividual1Index() {
        return individual1Index;
    }

    public int getIndividual2Index() {
        return individual2Index;
    }

    @Override
    public int[] getIndividualMatches() {
        return null;
    }

    public String toString(){
        return "Individual1: " + individual1Index + " Individual2: " + individual2Index;
    }
}
