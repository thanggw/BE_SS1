package com.example.SS2_Backend.dto.request;
import com.example.SS2_Backend.model.StableMatching.Individual;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IndividualDeserializer extends StdDeserializer<Individual> {

    public IndividualDeserializer() {
        this(null);
    }

    public IndividualDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Individual deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String individualName = node.get("IndividualName").asText();
        int individualSet = node.get("IndividualSet").asInt();

        Individual individual = new Individual();
        individual.setIndividualName(individualName);
        individual.setIndividualSet(individualSet);

        JsonNode propertiesNode = node.get("Properties");
        if (propertiesNode != null && propertiesNode.isArray()) {
            Iterator<JsonNode> propertiesIterator = propertiesNode.elements();
            while (propertiesIterator.hasNext()) {
                JsonNode propertyNode = propertiesIterator.next();
                Double propertyValue = propertyNode.get(0).asDouble();
                int propertyWeight = propertyNode.get(1).asInt();
                String inputRequirement = propertyNode.get(2).asText();

                individual.setProperty(propertyValue, propertyWeight, inputRequirement);
            }
        }

        return individual;
    }

}
