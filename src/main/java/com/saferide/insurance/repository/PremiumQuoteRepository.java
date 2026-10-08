package com.saferide.insurance.repository;

import com.saferide.insurance.entity.quote.PremiumQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PremiumQuoteRepository extends JpaRepository<PremiumQuote, Long> {
    Optional<PremiumQuote> findByQuoteReference(String quoteReference);
    List<PremiumQuote> findByCustomerId(Long customerId);
    List<PremiumQuote> findByAppliedToPolicyFalse();
    void deleteByQuoteReference(String quoteReference);
}
