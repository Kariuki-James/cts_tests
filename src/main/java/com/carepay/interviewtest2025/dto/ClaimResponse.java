package com.carepay.interviewtest2025.dto;

import com.carepay.interviewtest2025.model.ClaimStatus;
import lombok.Value;

import java.time.Instant;
import java.util.List;

public record ClaimResponse(
    Long id,
    String policyNumber,
    String claimantName,
    ClaimStatus status,
    Instant createdAt,
    List<ClaimItemResponse> items) { }
