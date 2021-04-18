package com.compasso.projectms.service;

import com.compasso.projectms.api.dto.ProductDto;
import com.compasso.projectms.domain.entity.Product;
import com.compasso.projectms.domain.repository.ProductRepository;
import com.compasso.projectms.domain.service.ProductService;
import com.compasso.projectms.domain.service.exceptions.ProductNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static com.compasso.projectms.domain.repository.spec.ProductSpec.productMinMax;
import static com.compasso.projectms.domain.repository.spec.ProductSpec.productWithNameOrDescription;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.jpa.domain.Specification.where;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void SetUp() {
        this.productRepository.deleteAll();
    }

    @Test
    @DisplayName("Must save a product successfully")
    public void insert() {
        ProductDto productDto = createValidProductDto();
        ProductDto savedProductDto = productService.insert(productDto);

        assertNotNull(savedProductDto.getId());
        assertEquals(productDto.getName(), savedProductDto.getName());
        assertEquals(productDto.getDescription(), savedProductDto.getDescription());
        assertEquals(productDto.getPrice(), savedProductDto.getPrice());
    }

    @Test
    @DisplayName("Must update a product successfully")
    public void update() {
        ProductDto productDto = createValidProductDto();
        ProductDto savedProductDto = productService.insert(productDto);

        String updatedName = "MacBook";
        String updatedDescription = "Notebook da hora!";
        BigDecimal updatedValue = BigDecimal.valueOf(18958.43);

        savedProductDto.setName(updatedName);
        savedProductDto.setDescription(updatedDescription);
        savedProductDto.setPrice(updatedValue);
        ProductDto updatedProduct = productService.update(savedProductDto.getId(), savedProductDto);

        assertEquals(updatedName, updatedProduct.getName());
        assertEquals(updatedDescription, updatedProduct.getDescription());
        assertEquals(updatedValue, updatedProduct.getPrice());
    }

    @Test
    @DisplayName("Must fail to update a product")
    public void updateFail() {
        ProductDto productDto = createValidProductDto();
        String exceptionMessage = "Resource not found";
        assertThrows(ProductNotFoundException.class, () ->
                productService.update("100", productDto), exceptionMessage);
    }

    @Test
    @DisplayName("Must return a list of all products in the database")
    public void findAll() {
        List<Product> products = productService.findAll();

        int expectedListSize = products.size();
        assertEquals(expectedListSize, products.size());
    }

    @Test
    @DisplayName("It should only return a product searched for by id")
    public void findById() {
        ProductDto productDto = createValidProductDto();
        ProductDto savedProductDto = productService.insert(productDto);

        String savedProductDtoId = savedProductDto.getId();
        String savedProductDtoDescription = savedProductDto.getDescription();
        BigDecimal savedProductDtoPrice = savedProductDto.getPrice();

        ProductDto foundedProduct = productService.findById(savedProductDtoId);

        assertEquals(savedProductDtoId, foundedProduct.getId());
        assertEquals(savedProductDtoDescription, foundedProduct.getDescription());
        assertEquals(savedProductDtoPrice, foundedProduct.getPrice());
    }

    @Test
    @DisplayName("Must fail to find a product by id")
    public void findByIdFail() {
        String id = "A";
        String exceptionMessage = "Resource not found";
        assertThrows(ProductNotFoundException.class, () -> productService.findById(id), exceptionMessage);
    }

    @Test
    @DisplayName("Must delete a product by id")
    public void deleteById() {
        ProductDto productDto = createValidProductDto();
        ProductDto savedProduct = productService.insert(productDto);
        productService.delete(savedProduct.getId());
    }

    @Test
    @DisplayName("Must fail to delete a product by id")
    public void deleteByIdFail() {
        String id = "FRE";
        String exceptionMessage = "Resource not found";
        assertThrows(ProductNotFoundException.class, () -> productService.findById(id), exceptionMessage);
    }

    @Test
    @DisplayName("Must find a product by name")
    public void findAllSpecByName() {
        ProductDto productDto = createValidProductDto();
        productService.insert(productDto);

        String name = "Shirt";
        List<Product> products = productService.findAllSpec(where(productWithNameOrDescription(name)
                        .and(productMinMax(null, null))));

        assertEquals(1, products.size());
        assertEquals(productDto.getName(), products.get(0).getName());
    }

    @Test
    @DisplayName("Must find a product by description")
    public void findAllSpecByDescription() {
        ProductDto productDto = createValidProductDto();
        productService.insert(productDto);

        String description = "Nice";
        List<Product> products = productService.findAllSpec(where(productWithNameOrDescription(description)
                        .and(productMinMax(null, null))));

        assertEquals(1, products.size());
        assertEquals(productDto.getDescription(), products.get(0).getDescription());
    }

    @Test
    @DisplayName("Must find a product by min price")
    public void findAllSpecByPriceMin() {
        ProductDto productDto = createValidProductDto();
        productService.insert(productDto);

        BigDecimal minPrice = BigDecimal.valueOf(8596.78);
        List<Product> products = productService.findAllSpec(where(productWithNameOrDescription(null)
                .and(productMinMax(minPrice, null))));

        assertEquals(1, products.size());
        assertEquals(productDto.getPrice(), products.get(0).getPrice());
    }

    @Test
    @DisplayName("Must find a product by max price")
    public void findProductByPriceMax() {
        ProductDto productDto = createValidProductDto();
        productDto.setPrice(BigDecimal.valueOf(1.52));
        productService.insert(productDto);

        BigDecimal maxPrice = BigDecimal.valueOf(3.52);
        List<Product> products = productService.findAllSpec(where(productWithNameOrDescription(null)
                .and(productMinMax(null, maxPrice))));

        assertEquals(products.size(), products.size());
        assertEquals(productDto.getPrice(), products.get(0).getPrice());
    }

    @Test
    @DisplayName("Must find a product by max price")
    public void findProductByPriceMaxAndMin() {
        ProductDto productDto = createValidProductDto();
        ProductDto anotherProductDto = createValidProductDto();
        anotherProductDto.setPrice(BigDecimal.valueOf(9568.98));
        productService.insert(productDto);
        productService.insert(anotherProductDto);

        List<Product> products = this.productService.findAllSpec(where(productWithNameOrDescription(null)
                        .and(productMinMax(BigDecimal.valueOf(8596.78), BigDecimal.valueOf(10250.36)))));

        assertEquals(2, products.size());
    }

    private ProductDto createValidProductDto() {
        return ProductDto.builder()
                .name("Shirt")
                .description("Nice")
                .price(BigDecimal.valueOf(8596.78))
                .build();
    }
}
