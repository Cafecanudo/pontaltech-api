package br.com.ilink.pontaltechapi.models;

import br.com.ilink.pontaltechapi.models.converters.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class SingleSMS {

  private String to;
  private String message;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime schedule;
  private String reference;
  private Integer account;
  private String accountReference;
  private String from;
  private String urlCallback;
  private Boolean flashSms;

}
