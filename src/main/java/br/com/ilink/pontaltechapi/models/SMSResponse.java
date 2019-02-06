package br.com.ilink.pontaltechapi.models;

import br.com.ilink.pontaltechapi.models.converters.ZonedDateTimeDeserializerToLocalDateTime;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SMSResponse {

  private String id;
  private String to;
  private String message;
  @JsonDeserialize(using = ZonedDateTimeDeserializerToLocalDateTime.class)
  private LocalDateTime schedule;
  private LocalDateTime sent;
  private String reference;
  private Integer account;
  private String from;
  private Integer status;
  private String statusDescription;
  private SMSResponseError error;

}
