package com.carepay.interviewtest2025.service;

import com.carepay.interviewtest2025.dto.ClaimCreateRequest;
import com.carepay.interviewtest2025.dto.ClaimItemCreateRequest;
import com.carepay.interviewtest2025.dto.ClaimItemResponse;
import com.carepay.interviewtest2025.dto.ClaimResponse;
import com.carepay.interviewtest2025.model.Claim;
import com.carepay.interviewtest2025.model.ClaimItem;
import com.carepay.interviewtest2025.model.ClaimItemStatus;
import com.carepay.interviewtest2025.model.ClaimStatus;
import com.carepay.interviewtest2025.repository.ClaimItemRepository;
import com.carepay.interviewtest2025.repository.ClaimRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClaimServiceTest {

    @Mock
    private ClaimRepository claimRepository;

    @Mock
    private ClaimItemRepository itemRepository;

    @InjectMocks
    private ClaimService claimService;

    private Claim claim;

    @BeforeEach
    void setUp() {
        claim = new Claim();
        claim.setId(1L);
        claim.setPolicyNumber("POL-123");
        claim.setClaimantName("Jane Doe");
        claim.setStatus(ClaimStatus.OPEN);
    }

    @Test
    void createClaim_shouldCreateClaimWithNoItems() {
        ClaimCreateRequest request = new ClaimCreateRequest();
        request.setPolicyNumber("POL-123");
        request.setClaimantName("Jane Doe");
        request.setStatus(ClaimStatus.OPEN);

        when(claimRepository.save(any(Claim.class))).thenReturn(claim);

        ClaimResponse response = claimService.createClaim(request);

        assertThat(response).isNotNull();
        assertThat(response.policyNumber()).isEqualTo("POL-123");
        assertThat(response.claimantName()).isEqualTo("Jane Doe");
        assertThat(response.status()).isEqualTo(ClaimStatus.OPEN);
        verify(claimRepository).save(any(Claim.class));
    }

    @Test
    void createClaim_shouldCreateClaimWithItems() {
        ClaimItemCreateRequest itemRequest = new ClaimItemCreateRequest();
        itemRequest.setDescription("Screen replacement");
        itemRequest.setAmount(new BigDecimal("120.50"));
        itemRequest.setStatus(ClaimItemStatus.PENDING);

        ClaimCreateRequest request = new ClaimCreateRequest();
        request.setPolicyNumber("POL-123");
        request.setClaimantName("Jane Doe");
        request.setStatus(ClaimStatus.OPEN);
        request.setItems(List.of(itemRequest));

        ClaimItem claimItem = new ClaimItem();
        claimItem.setId(1L);
        claimItem.setDescription("Screen replacement");
        claimItem.setAmount(new BigDecimal("120.50"));
        claimItem.setStatus(ClaimItemStatus.PENDING);
        claim.getItems().add(claimItem);

        when(claimRepository.save(any(Claim.class))).thenReturn(claim);

        ClaimResponse response = claimService.createClaim(request);

        assertThat(response.items()).hasSize(1);
        assertThat(response.items().get(0).description()).isEqualTo("Screen replacement");
    }

    @Test
    void getClaim_shouldReturnClaim_whenExists() {
        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));

        ClaimResponse response = claimService.getClaim(1L);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.policyNumber()).isEqualTo("POL-123");
    }

    @Test
    void getClaim_shouldThrowException_whenNotFound() {
        when(claimRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> claimService.getClaim(99L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Claim not found: 99");
    }

    @Test
    void addItem_shouldAddItemToClaim() {
        ClaimItemCreateRequest request = new ClaimItemCreateRequest();
        request.setDescription("Battery");
        request.setAmount(new BigDecimal("40.00"));
        request.setStatus(ClaimItemStatus.PENDING);

        ClaimItem savedItem = new ClaimItem();
        savedItem.setId(1L);
        savedItem.setDescription("Battery");
        savedItem.setAmount(new BigDecimal("40.00"));
        savedItem.setStatus(ClaimItemStatus.PENDING);

        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));
        when(itemRepository.save(any(ClaimItem.class))).thenReturn(savedItem);

        ClaimItemResponse response = claimService.addItem(1L, request);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.description()).isEqualTo("Battery");
        assertThat(response.amount()).isEqualByComparingTo("40.00");
    }

    @Test
    void addItem_shouldThrowException_whenClaimNotFound() {
        ClaimItemCreateRequest request = new ClaimItemCreateRequest();
        request.setDescription("Battery");
        request.setAmount(new BigDecimal("40.00"));
        request.setStatus(ClaimItemStatus.PENDING);

        when(claimRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> claimService.addItem(99L, request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Claim not found: 99");
    }
}