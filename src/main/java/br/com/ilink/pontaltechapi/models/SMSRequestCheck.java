package br.com.ilink.pontaltechapi.models;


import br.com.ilink.pontaltechapi.models.converters.LocalDateTimeSerializerToZonedDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SMSRequestCheck {

  @JsonProperty("startDate")
  @JsonSerialize(using = LocalDateTimeSerializerToZonedDateTime.class)
  private LocalDateTime dataInicio;
  @JsonProperty("endDate")
  @JsonSerialize(using = LocalDateTimeSerializerToZonedDateTime.class)
  private LocalDateTime dataFim;

}
