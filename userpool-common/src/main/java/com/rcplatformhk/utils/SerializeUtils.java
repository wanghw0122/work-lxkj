package com.rcplatformhk.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.fasterxml.jackson.databind.PropertyNamingStrategy.*;

public class SerializeUtils {

    private static final PropertyNamingStrategy DEFAULT_STRATEGY = SNAKE_CASE;

    private static final Map<PropertyNamingStrategy, ObjectMapper> MAPPER_MAP;

    static {
        MAPPER_MAP = new HashMap<>();
        ObjectMapper snakeMapper = new ObjectMapper();
        snakeMapper.setPropertyNamingStrategy(SNAKE_CASE);
        snakeMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER_MAP.put(SNAKE_CASE, snakeMapper);

        ObjectMapper lowerCamelMapper = new ObjectMapper();
        lowerCamelMapper.setPropertyNamingStrategy(LOWER_CAMEL_CASE);
        lowerCamelMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER_MAP.put(LOWER_CAMEL_CASE, lowerCamelMapper);

        ObjectMapper upperCamelMapper = new ObjectMapper();
        upperCamelMapper.setPropertyNamingStrategy(UPPER_CAMEL_CASE);
        upperCamelMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER_MAP.put(UPPER_CAMEL_CASE, upperCamelMapper);

        ObjectMapper lowerMapper = new ObjectMapper();
        lowerMapper.setPropertyNamingStrategy(LOWER_CASE);
        lowerMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER_MAP.put(LOWER_CASE, lowerMapper);

        ObjectMapper kebabMapper = new ObjectMapper();
        kebabMapper.setPropertyNamingStrategy(KEBAB_CASE);
        kebabMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER_MAP.put(KEBAB_CASE, kebabMapper);
    }

    private SerializeUtils() {

    }

    public static Optional<String> serialize(Object object, PropertyNamingStrategy strategy) {
        ObjectMapper mapper = MAPPER_MAP.get(strategy);
        if (mapper == null) {
            throw new IllegalArgumentException("Unrecognized strategy");
        }
        try {
            return Optional.of(mapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static <T> Optional<T> deserialize(String json, Class<T> t, PropertyNamingStrategy strategy) {
        ObjectMapper mapper = MAPPER_MAP.get(strategy);
        if (mapper == null) {
            throw new IllegalArgumentException("Unrecognized strategy");
        }
        try {
            return Optional.of(mapper.readValue(json, t));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static <T> Optional<List<T>> deserializeList(String json, Class<T> t, PropertyNamingStrategy strategy) {
        ObjectMapper mapper = MAPPER_MAP.get(strategy);
        if (mapper == null) {
            throw new IllegalArgumentException("Unrecognized strategy");
        }
        try {
            return Optional.of(mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, t)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<String> serialize(Object object) {
        return serialize(object, DEFAULT_STRATEGY);
    }

    public static <T> Optional<T> deserialize(String json, Class<T> t) {
        return deserialize(json, t, DEFAULT_STRATEGY);
    }

    public static <T> Optional<List<T>> deserializeList(String json, Class<T> t) {
        return deserializeList(json, t, DEFAULT_STRATEGY);
    }

    public static void main(String[] args) {

    }
}
