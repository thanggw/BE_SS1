package com.example.SS2_Backend.model.StableMatching;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Match Data Structure for Match:One to Many Stable Matching Problem
 * [individual1] => [individual2, individual3, individual4, ...]
 */

@Getter
public class MatchSet implements MatchItem {
    private final int Individual1Index;
    @Getter
    private final int Capacity;
    private final List<Integer> IndividualMatches = new ArrayList<>();
    public MatchSet(int Individual, int Capacity){
        this.Individual1Index = Individual;
        this.Capacity = Capacity;
    }

    @Override
    public int getIndividual2Index(){
        return 0;
    }
    public void addMatch(int target){
        if(!IndividualMatches.contains(target)){
            IndividualMatches.add(target);
        }
    }
    public void unMatch(int target){
        IndividualMatches.remove((Integer) target);
    }
    public String toString(){
        return "[" + Individual1Index + "] => " + IndividualMatches.toString();
    }
}
