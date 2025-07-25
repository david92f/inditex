package com.inditex.price.domain.repository;
import com.inditex.price.domain.model.Price;
import java.time.LocalDateTime;
import java.util.Optional;

public interface PriceRepository {
    Optional<Price> findApplicablePrice(Long productId, Long brandId, LocalDateTime applicationDate);
}
