package com.saferide.insurance.entity.policy;

import com.saferide.insurance.entity.user.Customer;
import com.saferide.insurance.entity.vehicle.Vehicle;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "comprehensive_policies")
@PrimaryKeyJoinColumn(name = "id")
public class ComprehensivePolicy extends Policy {

    @Column(name = "includes_natural_disaster")
    private boolean includesNaturalDisaster = true;

    @Column(name = "includes_theft")
    private boolean includesTheft = true;

    @Column(name = "includes_riot_strike")
    private boolean includesRiotStrike = true;

    @Column(name = "roadside_assistance_level", length = 30)
    private String roadsideAssistanceLevel = "PLATINUM";

    public ComprehensivePolicy() {
        super();
        setPolicyType(PolicyType.COMPREHENSIVE);
    }

    public ComprehensivePolicy(String policyNumber, Customer customer, Vehicle vehicle,
                               PolicyStatus status, LocalDate startDate, LocalDate endDate,
                               BigDecimal sumInsured, BigDecimal basePremium, BigDecimal totalAnnualPremium,
                               BigDecimal deductibleAmount, boolean includesNaturalDisaster,
                               boolean includesTheft, boolean includesRiotStrike,
                               String roadsideAssistanceLevel) {
        super(policyNumber, customer, vehicle, PolicyType.COMPREHENSIVE, status, startDate, endDate,
                sumInsured, basePremium, totalAnnualPremium, deductibleAmount);
        this.includesNaturalDisaster = includesNaturalDisaster;
        this.includesTheft = includesTheft;
        this.includesRiotStrike = includesRiotStrike;
        this.roadsideAssistanceLevel = roadsideAssistanceLevel;
    }

    @Override
    public String getCoverageDetailsSummary() {
        return "Comprehensive Tier: Full Own Damage, Natural Perils, Riot & Civil Commotion, Theft & " +
                roadsideAssistanceLevel + " 24/7 Roadside Assistance";
    }

    public boolean isIncludesNaturalDisaster() {
        return includesNaturalDisaster;
    }

    public void setIncludesNaturalDisaster(boolean includesNaturalDisaster) {
        this.includesNaturalDisaster = includesNaturalDisaster;
    }

    public boolean isIncludesTheft() {
        return includesTheft;
    }

    public void setIncludesTheft(boolean includesTheft) {
        this.includesTheft = includesTheft;
    }

    public boolean isIncludesRiotStrike() {
        return includesRiotStrike;
    }

    public void setIncludesRiotStrike(boolean includesRiotStrike) {
        this.includesRiotStrike = includesRiotStrike;
    }

    public String getRoadsideAssistanceLevel() {
        return roadsideAssistanceLevel;
    }

    public void setRoadsideAssistanceLevel(String roadsideAssistanceLevel) {
        this.roadsideAssistanceLevel = roadsideAssistanceLevel;
    }
}
