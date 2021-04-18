package com.compasso.projectms.api.resource;

import com.compasso.projectms.api.dto.ProductDto;
import com.compasso.projectms.domain.entity.Product;
import com.compasso.projectms.domain.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import static com.compasso.projectms.domain.repository.spec.ProductSpec.productMinMax;
import static com.compasso.projectms.domain.repository.spec.ProductSpec.productWithNameOrDescription;
import static org.springframework.data.jpa.domain.Specification.where;

@Api(value = "Product")
@RestController
@RequestMapping(value = "/products")
public class ProductResource {

    private final ProductService productService;

    @Autowired
    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Save product")
    @ApiResponses({@ApiResponse(code = 201, message = "Created")})
    public ResponseEntity<ProductDto> insert(@Valid @RequestBody ProductDto dto) {
        dto = productService.insert(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update product")
    public ResponseEntity<ProductDto> update(@PathVariable String id, @Valid @RequestBody ProductDto dto) {
        dto = productService.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping(value ="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Search for a product by its id")
    public ResponseEntity<ProductDto> findById(@PathVariable String id) {
        ProductDto dto = productService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Search all products")
    @ApiResponses({@ApiResponse(code = 500, message = "Internal Error")})
    public ResponseEntity<List<Product>> findAll() {
        List<Product> list = productService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filtered products list")
    public ResponseEntity<List<ProductDto>> filter(
            @RequestParam(value = "q", required = false) String nameOrDescription,
            @RequestParam(value = "min_price", required = false) BigDecimal minPrice,
            @RequestParam(value = "max_price", required = false) BigDecimal maxPrice) {

        List<Product> products = this.productService.findAllSpec(
                where(productWithNameOrDescription(nameOrDescription)).
                and(productMinMax(minPrice, maxPrice)));

        List<ProductDto> productDto = productService.convertEntityToDto(products);

        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "Remove product")
    public ResponseEntity<ProductDto> delete(@PathVariable String id) {
        productService.delete(id);
        return ResponseEntity.ok().build();
    }
}
