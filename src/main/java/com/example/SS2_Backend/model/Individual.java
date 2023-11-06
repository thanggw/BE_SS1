package com.example.SS2_Backend.model;
import java.util.*;

public class Individual {
    private String IndividualName;
    private int IndividualSet;
    private List<Property> Properties = new ArrayList<>();

    public Individual(String IndividualName, int IndividualSet){
        this.IndividualName = IndividualName;
        this.IndividualSet = IndividualSet;
    }

    public void setProperty(String propertyName, int propertyValue, int propertyWeight) {
        Property property = new Property(propertyName, propertyValue, propertyWeight);
        this.Properties.add(property);
    }

    public int getIndividualSet() {
        return IndividualSet;
    }

    public int getNumberOfProperties(){
        return Properties.size();
    }

    public String getPropertyName(int index){
        if(index >= 0 && index < this.Properties.size()){
            return Properties.get(index).getName();
        }else{
            return null;
        }
    }

    public int getPropertyValue(int index){
        if(index >= 0 && index < this.Properties.size()){
            return Properties.get(index).getValue();
        }else{
            return 0;
        }
    }

    public int getPropertyWeight(int index) {
        if(index >= 0 && index < this.Properties.size()){
            return Properties.get(index).getWeight();
        }else{
            return 0;
        }
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
