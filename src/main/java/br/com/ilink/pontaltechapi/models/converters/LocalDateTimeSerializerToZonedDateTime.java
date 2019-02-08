package br.com.ilink.pontaltechapi.models.converters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class LocalDateTimeSerializerToZonedDateTime extends JsonSerializer<LocalDateTime> {

  @Override
  public void serialize(LocalDateTime localDateTime, JsonGenerator json,
      SerializerProvider serializerProvider) throws IOException {
    if (localDateTime != null) {
      json.writeString(ZonedDateTime.of(localDateTime, ZoneOffset.UTC).toString());
    }
  }
}
