package com.compasso.projectms.domain.repository.spec;

import com.compasso.projectms.domain.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.Arrays;

public class ProductSpec {

    public static Specification<Product> productMinMax(BigDecimal minPrice, BigDecimal maxPrice) {

        return (root, query, criteriaBuilder) -> {

            if (minPrice == null && maxPrice == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            Path<BigDecimal> path = root.get("price");
            Predicate minPricePredicate = criteriaBuilder.greaterThanOrEqualTo(path, minPrice);

            if (maxPrice == null) {
                return minPricePredicate;
            }

            Predicate maxPricePredicate = criteriaBuilder.lessThanOrEqualTo(path, maxPrice);

            if (minPrice == null) {
                return maxPricePredicate;
            }

            return criteriaBuilder.between(path, minPrice, maxPrice);
        };
    }

    public static Specification<Product> productWithNameOrDescription(String nameOrDescription) {
        return (root, query, criteriaBuilder) -> {

            if (nameOrDescription == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            String nameOrDescriptionLikeValue = like(nameOrDescription);
            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                    nameOrDescriptionLikeValue);

            Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                    nameOrDescriptionLikeValue);

            return criteriaBuilder.or(namePredicate, descriptionPredicate);
        };
    }

    private static String like(String value) {

        StringBuilder sb = new StringBuilder();
        sb.append("%");
        Arrays.stream(value.split(" "))
                .forEach(word -> sb.append(word.toLowerCase()).append("%"));

        return sb.toString();
    }
}
