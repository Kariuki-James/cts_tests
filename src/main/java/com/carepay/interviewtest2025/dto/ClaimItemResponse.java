package com.carepay.interviewtest2025.dto;

import com.carepay.interviewtest2025.model.ClaimItemStatus;
import lombok.Value;

import java.math.BigDecimal;

public record ClaimItemResponse(
    Long id,
    String description,
    BigDecimal amount,
    ClaimItemStatus status) { }
