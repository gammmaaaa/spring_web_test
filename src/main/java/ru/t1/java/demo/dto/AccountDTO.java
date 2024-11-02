package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for {@link ru.t1.java.demo.model.Account}
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDTO {

    private Long id;
    @JsonProperty("client_id")
    private Long clientId;

    @JsonProperty("type")
    private String accountType;

    @JsonProperty("balance")
    private BigDecimal balance;
}
