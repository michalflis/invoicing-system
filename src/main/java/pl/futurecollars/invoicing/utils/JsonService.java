package pl.futurecollars.invoicing.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;

public class JsonService<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonService() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public String convertToJson(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public T covertToObject(String line, Class<T> t) {
        try {
            return objectMapper.readValue(line, t);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

