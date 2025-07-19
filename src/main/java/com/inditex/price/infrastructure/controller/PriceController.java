package com.inditex.price.infrastructure.controller;
import com.inditex.price.application.GetPriceUseCase;
import com.inditex.price.domain.model.Price;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/prices")
public class PriceController {
    private final GetPriceUseCase getPriceUseCase;

    public PriceController(GetPriceUseCase getPriceUseCase) {
        this.getPriceUseCase = getPriceUseCase;
    }

    @GetMapping
    public Price getPrice(@RequestParam Long productId,
                          @RequestParam Long brandId,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate) {
        return getPriceUseCase.execute(productId, brandId, applicationDate);
    }
}
