package br.com.felixgilioli.shop.controller;

import br.com.felixgilioli.shop.dto.ShopDTO;
import br.com.felixgilioli.shop.event.SendKafkaMessage;
import br.com.felixgilioli.shop.model.Shop;
import br.com.felixgilioli.shop.model.ShopItem;
import br.com.felixgilioli.shop.repository.ShopRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shop")
public class ShopController {

    private final ShopRepository shopRepository;
    private final SendKafkaMessage sendKafkaMessage;

    public ShopController(ShopRepository shopRepository, SendKafkaMessage sendKafkaMessage) {
        this.shopRepository = shopRepository;
        this.sendKafkaMessage = sendKafkaMessage;
    }

    @GetMapping
    public List<ShopDTO> getShop() {
        return shopRepository.findAll()
                .stream()
                .map(ShopDTO::convert)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ShopDTO saveShop(@RequestBody ShopDTO shopDTO) {
        shopDTO.setIdentifier(UUID.randomUUID().toString());
        shopDTO.setDateShop(LocalDate.now());
        shopDTO.setStatus("PENDING");

        Shop shop = Shop.convert(shopDTO);
        for (ShopItem shopItem : shop.getItems()) {
            shopItem.setShop(shop);
        }

        shopDTO = ShopDTO.convert(shopRepository.save(shop));
        sendKafkaMessage.sendMessage(shopDTO);
        return shopDTO;

    }
}
