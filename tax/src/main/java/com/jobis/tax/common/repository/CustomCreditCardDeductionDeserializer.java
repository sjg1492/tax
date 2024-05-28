package com.jobis.tax.common.repository;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CustomCreditCardDeductionDeserializer extends JsonDeserializer<Map<String, BigDecimal>> {

    @Override
    public Map<String, BigDecimal> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Map<String, BigDecimal> result = new HashMap<>();
        JsonNode node = p.getCodec().readTree(p);

        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String key = field.getKey();
            String value = field.getValue().asText().replace(",", "");
            result.put(key, new BigDecimal(value));
        }

        return result;
    }
}