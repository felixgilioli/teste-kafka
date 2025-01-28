package br.com.felixgilioli.shop.api.event;

import br.com.felixgilioli.shop.api.dto.ShopDTO;
import br.com.felixgilioli.shop.api.model.Shop;
import br.com.felixgilioli.shop.api.repository.ShopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ReceiveKafkaMessage {

    private final ShopRepository shopRepository;

    private static final String SHOP_TOPIC_EVENT_NAME = "SHOP_TOPIC_EVENT";
    private static final Logger log = LoggerFactory.getLogger(ReceiveKafkaMessage.class);

    public ReceiveKafkaMessage(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @KafkaListener(topics = SHOP_TOPIC_EVENT_NAME, groupId = "group")
    public void listenShopEvents(ShopDTO shopDTO) {
        try {
            log.info("Status da compra recebida no tópico: {}.", shopDTO.getIdentifier());

            Shop shop = shopRepository.findByIdentifier(shopDTO.getIdentifier());
            shop.setStatus(shopDTO.getStatus());
            shopRepository.save(shop);
        } catch(Exception e) {
            log.error("Erro no processamento da compra {}", shopDTO.getIdentifier());
        }
    }

}
