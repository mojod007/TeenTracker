package com.trace.product.service;

import com.trace.product.entity.Product;
import com.trace.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> findAll() {
        log.info("Récupération de tous les produits");
        return productRepository.findAll();
    }

    public Page<Product> findAll(Pageable pageable) {
        log.info("Récupération des produits avec pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        return productRepository.findAll(pageable);
    }

    public Product findById(Long id) {
        log.info("Recherche du produit avec ID: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID: " + id));
    }

    public Product save(Product product) {
        log.info("Sauvegarde du produit: {}", product.getNom());
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        log.info("Suppression du produit avec ID: {}", id);
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Produit non trouvé avec l'ID: " + id);
        }
        productRepository.deleteById(id);
    }
}
