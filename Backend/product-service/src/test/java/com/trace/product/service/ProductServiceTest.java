package com.trace.product.service;

import com.trace.product.entity.Product;
import com.trace.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .code("TEST001")
                .nom("Test Product")
                .description("Test Description")
                .prix(100.0)
                .actif(true)
                .build();
    }

    @Test
    void findAll_ShouldReturnAllProducts() {
        // Given
        List<Product> products = Arrays.asList(product);
        when(productRepository.findAll()).thenReturn(products);

        // When
        List<Product> result = productService.findAll();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCode()).isEqualTo("TEST001");
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnProduct_WhenExists() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // When
        Product result = productService.findById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getCode()).isEqualTo("TEST001");
    }

    @Test
    void findById_ShouldThrowException_WhenNotExists() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productService.findById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Produit non trouvé avec l'ID: 1");
    }

    @Test
    void save_ShouldReturnSavedProduct() {
        // Given
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // When
        Product result = productService.save(product);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo("TEST001");
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        // Given
        when(productRepository.existsById(1L)).thenReturn(true);

        // When
        productService.deleteById(1L);

        // Then
        verify(productRepository, times(1)).deleteById(1L);
    }
}
