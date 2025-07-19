package com.inditex.price.unit;

import com.inditex.price.application.GetPriceUseCase;
import com.inditex.price.domain.model.Price;
import com.inditex.price.domain.repository.PriceRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetPriceUseCaseTest {

    @Test
    void shouldReturnPriceWhenFound() {
        PriceRepository repository = mock(PriceRepository.class);
        Price price = new Price(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1L, 35455L, 0, 35.50, "EUR");
        when(repository.findApplicablePrice(eq(35455L), eq(1L), any(LocalDateTime.class))).thenReturn(Optional.of(price));

        GetPriceUseCase useCase = new GetPriceUseCase(repository);
        Price result = useCase.execute(35455L, 1L, LocalDateTime.now());

        assertNotNull(result);
        assertEquals(35.50, result.getProductPrice());
        verify(repository, times(1)).findApplicablePrice(eq(35455L), eq(1L), any(LocalDateTime.class));
    }

    @Test
    void shouldThrowExceptionWhenPriceNotFound() {
        PriceRepository repository = mock(PriceRepository.class);
        when(repository.findApplicablePrice(anyLong(), anyLong(), any(LocalDateTime.class))).thenReturn(Optional.empty());

        GetPriceUseCase useCase = new GetPriceUseCase(repository);

        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> useCase.execute(99999L, 999L, LocalDateTime.now())
        );
        assertEquals("No price found for given parameters", exception.getMessage());

        verify(repository, times(1)).findApplicablePrice(eq(99999L), eq(999L), any(LocalDateTime.class));
    }
}
