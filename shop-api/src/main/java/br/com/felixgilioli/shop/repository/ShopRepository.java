package br.com.felixgilioli.shop.repository;

import br.com.felixgilioli.shop.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Long> {
}
