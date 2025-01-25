package br.com.felixgilioli.shop.validator.event;

import br.com.felixgilioli.shop.validator.model.Product;
import br.com.felixgilioli.shop.validator.repository.ProductRepository;
import br.com.felixgilioli.shop.validator.dto.ShopDTO;
import br.com.felixgilioli.shop.validator.dto.ShopItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReceiveKafkaMessage {

    private static final String SHOP_TOPIC_NAME = "SHOP_TOPIC";
    private static final String SHOP_TOPIC_EVENT_NAME = "SHOP_TOPIC_EVENT";
    private static final Logger log = LoggerFactory.getLogger(ReceiveKafkaMessage.class);

    private final ProductRepository productRepository;
    private final KafkaTemplate<String, ShopDTO> kafkaTemplate;

    public ReceiveKafkaMessage(ProductRepository productRepository, KafkaTemplate<String, ShopDTO> kafkaTemplate) {
        this.productRepository = productRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = SHOP_TOPIC_NAME, groupId = "group")
    public void listenShopTopic(ShopDTO shopDTO) {
        log.info("Compra recebida no tópico: {}", shopDTO.getIdentifier());

        boolean success = true;
        for (ShopItemDTO item : shopDTO.getItems()) {
            Product product = productRepository.findByIdentifier(item.getProductIdentifier());
            if (!isValidShop(item, product)) {
                shopError(shopDTO);
                success = false;
                break;
            }
        }
        if (success) {
            shopSuccess(shopDTO);
        }

    }

    private boolean isValidShop(ShopItemDTO item, Product product) {
        return product != null && product.getAmount() >= item.getAmount();
    }

    private void shopError(ShopDTO shopDTO) {
        log.info("Erro no processamento da compra {}.", shopDTO.getIdentifier());
        shopDTO.setStatus("ERROR");
        kafkaTemplate.send(SHOP_TOPIC_EVENT_NAME, shopDTO);
    }

    private void shopSuccess(ShopDTO shopDTO) {
        log.info("Compra {} efetuada com sucesso.", shopDTO.getIdentifier());
        shopDTO.setStatus("SUCCESS");
        kafkaTemplate.send(SHOP_TOPIC_EVENT_NAME, shopDTO);
    }

}
