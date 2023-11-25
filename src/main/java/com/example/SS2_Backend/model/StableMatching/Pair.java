package com.example.SS2_Backend.model.StableMatching;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * Match Data Structure for Match:One to One Stable Matching Problem
 * [IndividualA] <=> [IndividualB]
 */

@Getter
@Data
@Builder
public class Pair implements MatchItem {
    private final int individual1Index;
    private final int individual2Index;

    public Pair(int individual1Index, int individual2Index){
        this.individual1Index = individual1Index;
        this.individual2Index = individual2Index;
    }

    @Override
    public List<Integer> getIndividualMatches() {
        return null;
    }
    @Override
    public int getCapacity() {
        return 0;
    }
    @Override
    public void addMatch(int target) {

    }
    @Override
    public void unMatch(int target) {

    }
    public String toString(){
        return "Individual1: " + individual1Index + " Individual2: " + individual2Index;
    }
}
