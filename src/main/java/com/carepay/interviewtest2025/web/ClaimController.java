package com.carepay.interviewtest2025.web;

import com.carepay.interviewtest2025.dto.ClaimCreateRequest;
import com.carepay.interviewtest2025.dto.ClaimItemCreateRequest;
import com.carepay.interviewtest2025.dto.ClaimItemResponse;
import com.carepay.interviewtest2025.dto.ClaimResponse;
import com.carepay.interviewtest2025.service.ClaimService;
import jakarta.validation.Valid;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {
    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClaimResponse createClaim(@Valid @RequestBody ClaimCreateRequest request) {
        return claimService.createClaim(request);
    }

    @PostMapping("/{id}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public ClaimItemResponse addItem(@PathVariable Long id, @Valid @RequestBody ClaimItemCreateRequest request) {
        return claimService.addItem(id, request);
    }

    @GetMapping("/{id}")
    public ClaimResponse getClaim(@PathVariable Long id) {
        return claimService.getClaim(id);
    }
}
