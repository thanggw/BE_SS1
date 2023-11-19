package com.example.SS2_Backend.model.StableMatching;
import com.example.SS2_Backend.model.StableMatching.Requirement.Requirement;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public class Individual {
    @Setter
    private String IndividualName;
    @Setter
    private int IndividualSet;
    private final List<Property> Properties = new ArrayList<>();

    public Individual(String IndividualName, int IndividualSet){
        this.IndividualName = IndividualName;
        this.IndividualSet = IndividualSet;
    }

    public void setProperty(Double propertyValue, int propertyWeight, String[] inputRequirement) {
        Property property = new Property(propertyValue, propertyWeight, inputRequirement);
        this.Properties.add(property);
    }

    public int getNumberOfProperties(){
        return Properties.size();
    }

//    public String getPropertyName(int index){
//        if(index >= 0 && index < this.Properties.size()){
//            return Properties.get(index).getName();
//        }else{
//            return null;
//        }
//    }

    public Double getPropertyValue(int index){
        if(index >= 0 && index < this.Properties.size()){
            return Properties.get(index).getValue();
        }else{
            return null;
        }
    }

    public int getPropertyWeight(int index) {
        if(index >= 0 && index < this.Properties.size()){
            return Properties.get(index).getWeight();
        }else{
            return 0;
        }
    }

    public Requirement getRequirement(int index){
        return Properties.get(index).getRequirement();
    }

    public String toString(){
        System.out.println("Name: " + IndividualName);
        System.out.println("Belong to set: " + IndividualSet);
        System.out.println("Properties:");
        System.out.println("---------------------------------");
        for (Property property : Properties) {
            System.out.println(property.toString());
        }
        return "\n";
    }

}
