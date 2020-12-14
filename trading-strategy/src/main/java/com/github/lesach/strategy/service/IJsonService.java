package com.github.lesach.strategy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;

public interface IJsonService {

    String toJson(Object value) throws JsonProcessingException;

    <T> T fromJson(String value, Class<T> tClass) throws IOException;

    <T> T fromJson(String value, TypeReference<T> tClass) throws IOException;
}
