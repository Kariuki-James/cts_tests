package com.carepay.interviewtest2025.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "claim")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "policy_number", nullable = false)
    private String policyNumber;


    @Column(name = "claimant_name", nullable = false)
    private String claimantName;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimStatus status = ClaimStatus.OPEN;


    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();


    @Column(name = "updated_at")
    private Instant updatedAt;


    @OneToMany(mappedBy = "claim", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @Builder.Default
    private List<ClaimItem> items = new ArrayList<>();


    public void addItem(ClaimItem item) {
        item.setClaim(this);
        this.items.add(item);
    }
}