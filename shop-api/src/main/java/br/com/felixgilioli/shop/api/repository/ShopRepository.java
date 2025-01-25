package br.com.felixgilioli.shop.api.repository;

import br.com.felixgilioli.shop.api.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Long> {
}
