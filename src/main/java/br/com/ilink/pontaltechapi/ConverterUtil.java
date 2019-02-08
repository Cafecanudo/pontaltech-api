package br.com.ilink.pontaltechapi;

import br.com.ilink.pontaltechapi.exceptions.ProcessorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class ConverterUtil {

  public static <T> T toObject(String json, Class<T> clazz) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(json, clazz);
  }

  public static <T> T toObject(String json, TypeReference listType) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(json, objectMapper.constructType(listType.getType()));
  }

  public static JsonNode toJson(String data) {
    try {
      return new ObjectMapper().readTree(data);
    } catch (IOException e) {
      throw new ProcessorException(e);
    }
  }

  public static String toJsonString(Object value) {
    try {
      return new ObjectMapper().writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new ProcessorException(e);
    }
  }
}
