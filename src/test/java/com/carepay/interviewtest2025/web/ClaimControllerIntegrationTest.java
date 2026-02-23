package com.carepay.interviewtest2025.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.carepay.interviewtest2025.model.Claim;
import com.carepay.interviewtest2025.model.ClaimStatus;
import com.carepay.interviewtest2025.repository.ClaimRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClaimControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClaimRepository claimRepository;

    private Long savedClaimId;

    @BeforeEach
    void setUp() {
        claimRepository.deleteAll();

        Claim claim = new Claim();
        claim.setPolicyNumber("POL-123");
        claim.setClaimantName("Jane Doe");
        claim.setStatus(ClaimStatus.OPEN);
        savedClaimId = claimRepository.save(claim).getId();
    }

    @Test
    void createClaim_shouldReturn201_withValidRequest() throws Exception {
        String requestBody = objectMapper.writeValueAsString(Map.of(
            "policyNumber", "POL-456",
            "claimantName", "John Smith",
            "status", "OPEN",
            "items", java.util.List.of()
        ));

        mockMvc.perform(post("/api/claims")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.policyNumber").value("POL-456"))
            .andExpect(jsonPath("$.claimantName").value("John Smith"))
            .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void createClaim_shouldReturn400_whenPolicyNumberMissing() throws Exception {
        String requestBody = objectMapper.writeValueAsString(Map.of(
            "claimantName", "John Smith",
            "status", "OPEN"
        ));

        mockMvc.perform(post("/api/claims")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getClaim_shouldReturn200_whenClaimExists() throws Exception {
        mockMvc.perform(get("/api/claims/{id}", savedClaimId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(savedClaimId))
            .andExpect(jsonPath("$.policyNumber").value("POL-123"))
            .andExpect(jsonPath("$.claimantName").value("Jane Doe"));
    }

    @Test
    void getClaim_shouldReturn404_whenClaimNotFound() throws Exception {
        mockMvc.perform(get("/api/claims/{id}", 999L))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Claim not found: 999"));
    }

    @Test
    void addItem_shouldReturn201_withValidRequest() throws Exception {
        String requestBody = objectMapper.writeValueAsString(Map.of(
            "description", "Back cover",
            "amount", "25.00",
            "status", "PENDING"
        ));

        mockMvc.perform(post("/api/claims/{id}/items", savedClaimId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.description").value("Back cover"))
            .andExpect(jsonPath("$.amount").value(25.00))
            .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void addItem_shouldReturn400_whenDescriptionMissing() throws Exception {
        String requestBody = objectMapper.writeValueAsString(Map.of(
            "amount", "25.00",
            "status", "PENDING"
        ));

        mockMvc.perform(post("/api/claims/{id}/items", savedClaimId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());
    }

    @Test
    void addItem_shouldReturn404_whenClaimNotFound() throws Exception {
        String requestBody = objectMapper.writeValueAsString(Map.of(
            "description", "Back cover",
            "amount", "25.00",
            "status", "PENDING"
        ));

        mockMvc.perform(post("/api/claims/{id}/items", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Claim not found: 999"));
    }

}