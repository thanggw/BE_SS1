package com.example.SS2_Backend.model.StableMatching;
import com.example.SS2_Backend.dto.request.IndividualDeserializer;
import com.example.SS2_Backend.model.StableMatching.Requirement.Requirement;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@JsonDeserialize(using = IndividualDeserializer.class)
public class Individual {
    @Setter
    private String IndividualName;
    @Setter
    private int IndividualSet;
    private final List<Property> Properties = new ArrayList<>();


    public Individual(){

    }

    @JsonProperty("Properties")
    public void setProperty(Double propertyValue, int propertyWeight, String inputRequirement) {
        String[] decodedRequirement = decodeInputRequirement(inputRequirement);
        Property property = new Property(propertyValue, propertyWeight, decodedRequirement);
        this.Properties.add(property);
    }
    public void setProperty(Double propertyValue, int propertyWeight, String[] inputRequirement) {
        Property property = new Property(propertyValue, propertyWeight, inputRequirement);
        this.Properties.add(property);
    }
    public String[] decodeInputRequirement(String item){
        item = item.trim();
        String[] result = new String[2];
        if (item.contains(",")) {
            String[] parts = item.split(",");
            result[0] = parts[0].trim();
            result[1] = parts[1].trim();
        } else {
            // If no comma, extract the number and expression
            int index = findFirstNonNumericIndex(item);
            result[0] = item.substring(0, index).trim();
            result[1] = item.substring(index).trim();
        }
        return result;
    }
    private static int findFirstNonNumericIndex(String s) {
        int index = 0;
        while (index < s.length() && (Character.isDigit(s.charAt(index)) || s.charAt(index) == '.')) {
            index++;
        }
        return index;
    }
    @JsonProperty("IndividualName")
    public void setIndividualName(String individualName) {
        IndividualName = individualName;
    }

    @JsonProperty("IndividualSet")
    public void setIndividualSet(int individualSet) {
        IndividualSet = individualSet;
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
