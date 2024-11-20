package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDTO implements Serializable {

    private Long id;
    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("account_id")
    private Long accountId;

    @JsonProperty("time_transaction")
    private LocalDateTime timeTransaction;
}
