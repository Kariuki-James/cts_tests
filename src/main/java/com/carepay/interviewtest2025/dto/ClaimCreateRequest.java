package com.carepay.interviewtest2025.dto;

import com.carepay.interviewtest2025.model.ClaimStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class ClaimCreateRequest {
    @NotBlank
    private String policyNumber;

    @NotBlank
    private String claimantName;

    @NotNull
    private ClaimStatus status;


    @Valid
    private List<ClaimItemCreateRequest> items = new ArrayList<>();

}
