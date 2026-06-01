package com.example;

import org.example.cart.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ShoppingCartService - testy mockowania")
class ShoppingCartServiceTest  {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    @Nested
    class CalculateTotalTests {
        @Test
        @DisplayName("Powinien obliczyć wartość koszyka dla jednego produktu bez rabatu")
        void shouldCalculateTotalForSingleItem() {
            Product product = new Product("P1","Laptop", 100.0);

            when(productRepository.findById("P1"))
                    .thenReturn(Optional.of(product));

            when(discountService.getDiscountForCustomer("C1"))
                    .thenReturn(0.0);

            List<CartItem> items = List.of(
                    new CartItem("P1", 1)
            );

            double total = shoppingCartService.calculateTotal("C1", items);

            assertThat(total).isEqualTo(100.0);

            verify(productRepository).findById("P1");
            verify(discountService).getDiscountForCustomer("C1");
        }

        @Test
        @DisplayName("Powinien poprawnie zastosować rabat")
        void shouldApplyDiscountCorrectly() {
            Product product = new Product("P1", "Monitor", 200.0);

            when(productRepository.findById("P1"))
                    .thenReturn(Optional.of(product));

            when(discountService.getDiscountForCustomer("C1"))
                    .thenReturn(0.1);

            List<CartItem> items = List.of(
                    new CartItem("P1", 2)
            );

            double total = shoppingCartService.calculateTotal("C1", items);

            assertThat(total)
                    .isCloseTo(360.0, within(0.01));
        }

        @Test
        @DisplayName("Powinien obliczyć wartość koszyka dla wielu produktów")
        void shouldCalculateTotalForMultipleProducts() {
            Product p1 = new Product("P1", "Produkt 1", 100.0);
            Product p2 = new Product("P2", "Produkt 2", 50.0);

            when(productRepository.findById("P1"))
                    .thenReturn(Optional.of(p1));

            when(productRepository.findById("P2"))
                    .thenReturn(Optional.of(p2));

            when(discountService.getDiscountForCustomer("C1"))
                    .thenReturn(0.0);
            List<CartItem> items = List.of(
                    new CartItem("P1", 1),
                    new CartItem("P2", 3)
            );

            double total = shoppingCartService.calculateTotal("C1", items);

            assertThat(total).isEqualTo(250.0);
        }

        @Test
        @DisplayName("Powinien rzucić wyjątek dla pustego koszyka")
        void shouldThrowExceptionForEmptyCart() {

            List<CartItem> items = List.of();

            assertThatThrownBy(() ->
                    shoppingCartService.calculateTotal("C1", items))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("pusty");

            verifyNoInteractions(productRepository);
            verifyNoInteractions(discountService);
        }

        @Test
        @DisplayName("Powinien rzucić wyjątek gdy produkt nie istnieje")
        void shouldThrowExceptionWhenProductNotFound() {

            when(productRepository.findById("P999"))
                    .thenReturn(Optional.empty());

            List<CartItem> items = List.of(
                    new CartItem("P999", 1)
            );

            assertThatThrownBy(() ->
                    shoppingCartService.calculateTotal("C1", items))
                    .isInstanceOf(ProductNotFoundException.class)
                    .hasMessageContaining("P999");
        }
    }
}
