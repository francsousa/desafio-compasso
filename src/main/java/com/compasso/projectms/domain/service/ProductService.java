package com.compasso.projectms.domain.service;

import com.compasso.projectms.api.dto.ProductDto;
import com.compasso.projectms.domain.entity.Product;
import com.compasso.projectms.domain.repository.ProductRepository;
import com.compasso.projectms.domain.service.exceptions.FormatErrorException;
import com.compasso.projectms.domain.service.exceptions.ProductNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductDto insert(ProductDto dto) {
        try {
            Product entity = new Product();
            convertDtoToEntity(dto, entity);
            entity = productRepository.save(entity);
            return new ProductDto(entity);
        } catch (HttpMessageNotReadableException e) {
            throw new FormatErrorException("Invalid Json Format");
        }
    }

    @Transactional
    public ProductDto update(String id, ProductDto dto) {
        try {
            Product entity = productRepository.getOne(id);
            convertDtoToEntity(dto, entity);
            entity = productRepository.save(entity);
            return new ProductDto(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ProductNotFoundException("Id not found " + id);
        }
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ProductDto findById(String id) {
        Optional<Product> obj = productRepository.findById(id);
        Product entity = obj.orElseThrow(() -> new ProductNotFoundException("Entity not found"));
        return new ProductDto(entity);
    }

    public void delete(String id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ProductNotFoundException("Id not found " + id);
        }
    }

    @Transactional(readOnly = true)
    public List<Product> findAllSpec(Specification<Product> spec) {
        return productRepository.findAll(spec);
    }

    private void convertDtoToEntity(ProductDto dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
    }

    public List<ProductDto> convertEntityToDto(List<Product> products) {
        ModelMapper modelMapper = new ModelMapper();
        List<ProductDto> productsDto = new ArrayList<>();

        products.forEach(product -> {
            ProductDto dto = modelMapper.map(product, ProductDto.class);
            productsDto.add(dto);
        });
        return productsDto;
    }
}
