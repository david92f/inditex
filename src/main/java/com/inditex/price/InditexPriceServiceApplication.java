package com.inditex.price;

import com.inditex.price.application.GetPriceUseCase;
import com.inditex.price.domain.repository.PriceRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InditexPriceServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InditexPriceServiceApplication.class, args);
    }

    @Bean
    public GetPriceUseCase getPriceUseCase(PriceRepository priceRepository) {
        return new GetPriceUseCase(priceRepository);
    }
}
