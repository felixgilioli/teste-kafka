package br.com.felixgilioli.shop.validator.repository;

import br.com.felixgilioli.shop.validator.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByIdentifier(String identifier);

}