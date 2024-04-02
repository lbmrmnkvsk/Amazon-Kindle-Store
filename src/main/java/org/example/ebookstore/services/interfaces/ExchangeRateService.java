package org.example.ebookstore.services.interfaces;

import java.math.BigDecimal;
import java.util.Optional;

public interface ExchangeRateService {
    Optional<BigDecimal> getLatestRate(String code);
}
