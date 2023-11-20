package com.example.SS2_Backend.model.StableMatching;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

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
    public int[] getIndividualMatches() {
        return null;
    }

    public String toString(){
        return "Individual1: " + individual1Index + " Individual2: " + individual2Index;
    }
}
