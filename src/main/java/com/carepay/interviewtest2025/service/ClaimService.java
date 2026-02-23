package com.carepay.interviewtest2025.service;

import com.carepay.interviewtest2025.dto.ClaimCreateRequest;
import com.carepay.interviewtest2025.dto.ClaimItemCreateRequest;
import com.carepay.interviewtest2025.dto.ClaimItemResponse;
import com.carepay.interviewtest2025.dto.ClaimResponse;
import com.carepay.interviewtest2025.model.Claim;
import com.carepay.interviewtest2025.model.ClaimItem;
import com.carepay.interviewtest2025.repository.ClaimItemRepository;
import com.carepay.interviewtest2025.repository.ClaimRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClaimService {
    private final ClaimRepository claimRepository;
    private final ClaimItemRepository itemRepository;


public ClaimService(ClaimRepository claimRepository, ClaimItemRepository itemRepository) {
        this.claimRepository = claimRepository;
        this.itemRepository = itemRepository;
    }


    @Transactional
    public ClaimResponse createClaim (ClaimCreateRequest req){
        Claim claim = new Claim();
        claim.setPolicyNumber(req.getPolicyNumber());
        claim.setClaimantName(req.getClaimantName());
        claim.setStatus(req.getStatus());


        if (req.getItems() != null) {
            for (ClaimItemCreateRequest i : req.getItems()) {
                ClaimItem item = new ClaimItem();
                item.setDescription(i.getDescription());
                item.setAmount(i.getAmount());
                item.setStatus(i.getStatus());
                claim.addItem(item);
            }
        }


        Claim saved = claimRepository.save(claim);
        return toResponse(saved);
    }

    public ClaimResponse getClaim (Long id){
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Claim not found: " + id));
        return toResponse(claim);
    }


    private ClaimResponse toResponse (Claim claim){
        List<ClaimItemResponse> items = claim.getItems().stream()
                .map(ci -> new ClaimItemResponse(ci.getId(), ci.getDescription(), ci.getAmount(), ci.getStatus()))
                .collect(Collectors.toList());
        return new ClaimResponse(
                claim.getId(),
                claim.getPolicyNumber(),
                claim.getClaimantName(),
                claim.getStatus(),
                claim.getCreatedAt(),
                items
        );
    }
}
