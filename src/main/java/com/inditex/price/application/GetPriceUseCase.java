package com.inditex.price.application;
import com.inditex.price.domain.model.Price;
import com.inditex.price.domain.repository.PriceRepository;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

public class GetPriceUseCase {
    private final PriceRepository priceRepository;

    public GetPriceUseCase(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public Price execute(Long productId, Long brandId, LocalDateTime applicationDate) {
        return priceRepository.findApplicablePrice(productId, brandId, applicationDate)
                .orElseThrow(() -> new NoSuchElementException("No price found for given parameters"));
    }
}
