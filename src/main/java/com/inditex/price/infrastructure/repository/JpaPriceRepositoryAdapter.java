package com.inditex.price.infrastructure.repository;

import com.inditex.price.domain.model.Price;
import com.inditex.price.domain.repository.PriceRepository;
import com.inditex.price.infrastructure.entity.PriceEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

@Component
public class JpaPriceRepositoryAdapter implements PriceRepository {

    private final JpaPriceRepository jpaPriceRepository;

    public JpaPriceRepositoryAdapter(JpaPriceRepository jpaPriceRepository) {
        this.jpaPriceRepository = jpaPriceRepository;
    }

    @Override
    public Optional<Price> findApplicablePrice(Long productId, Long brandId, LocalDateTime applicationDate) {
        return jpaPriceRepository.findApplicablePrices(productId, brandId, applicationDate)
                .stream()
                .max(Comparator.comparingInt(PriceEntity::getPriority))
                .map(this::toDomainPrice);
    }

    private Price toDomainPrice(PriceEntity entity) {
        return Price.builder()
                .brandId(entity.getBrandId())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .priceList(entity.getPriceList())
                .productId(entity.getProductId())
                .priority(entity.getPriority())
                .productPrice(entity.getProductPrice())
                .curr(entity.getCurr())
                .build();
    }
}
