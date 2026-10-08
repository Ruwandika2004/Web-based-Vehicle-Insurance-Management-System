package com.saferide.insurance.dto.request;

import com.saferide.insurance.entity.claim.ClaimStatus;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ClaimStatusUpdateRequest {

    @NotNull(message = "New claim status is required")
    private ClaimStatus status;

    private BigDecimal approvedPayoutAmount;
    private String assessorNotes;
    private String rejectionReason;
    private LocalDate settlementDate;

    public ClaimStatusUpdateRequest() {
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public BigDecimal getApprovedPayoutAmount() {
        return approvedPayoutAmount;
    }

    public void setApprovedPayoutAmount(BigDecimal approvedPayoutAmount) {
        this.approvedPayoutAmount = approvedPayoutAmount;
    }

    public String getAssessorNotes() {
        return assessorNotes;
    }

    public void setAssessorNotes(String assessorNotes) {
        this.assessorNotes = assessorNotes;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }
}
