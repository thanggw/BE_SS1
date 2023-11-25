package com.example.SS2_Backend.model.StableMatching.Requirement;

import lombok.Getter;

@Getter
public class ScaleTargetRequirement implements Requirement{
    private final int TargetValue;

    public ScaleTargetRequirement(int targetValue) {
        TargetValue = targetValue;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public Double getBound() {
        return null;
    }

    @Override
    public String getExpression() {
        return null;
    }

    @Override
    public Double getLowerBound() {
        return null;
    }
    @Override
    public Double getUpperBound() {
        return null;
    }

    @Override
    public String toString() {
        return "[" + this.TargetValue + "]";
    }
}
