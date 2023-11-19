package com.example.SS2_Backend.model.StableMatching.Requirement;

import lombok.Getter;
import static com.example.SS2_Backend.util.Utils.formatDouble;

@Getter
public class TwoBoundRequirement implements Requirement {
    private final Double lowerBound;
    private final Double upperBound;
    public TwoBoundRequirement(Double lowerBound, Double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }
    public int getType(){
        return 2;
    }
    @Override
    public Double getBound() {
        return null;
    }
    @Override
    public String getExpression() {
        return null;
    }

    public String toString(){
        return "[" + formatDouble(lowerBound) + ", " + formatDouble(upperBound) + "]";
    }
}
