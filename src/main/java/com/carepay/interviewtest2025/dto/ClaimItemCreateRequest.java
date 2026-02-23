package com.carepay.interviewtest2025.dto;

import com.carepay.interviewtest2025.model.ClaimItemStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
public class ClaimItemCreateRequest {
    @NotBlank
    private String description;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;


    @NotNull
    private ClaimItemStatus status;
}
