package br.com.felixgilioli.shop.retry.event;

import br.com.felixgilioli.shop.retry.dto.ShopDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReceiveKafkaMessage {

    private final KafkaTemplate<String, ShopDTO> kafkaTemplate;

    private static final String SHOP_TOPIC = "SHOP_TOPIC";
    private static final String SHOP_TOPIC_RETRY = "SHOP_TOPIC_RETRY";
    private static final Logger log = LoggerFactory.getLogger(ReceiveKafkaMessage.class);

    public ReceiveKafkaMessage(KafkaTemplate<String, ShopDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = SHOP_TOPIC, groupId = "group_retry")
    public void listenShopTopic(ShopDTO shopDTO)  {
        try {
            if (shopDTO.getItems() == null ||
                    shopDTO.getItems().isEmpty()) {
                log.error("Compra sem items");
                throw new Exception();
            }
        } catch(Exception e) {
            log.info("Erro na aplicação");
            kafkaTemplate.send(SHOP_TOPIC_RETRY, shopDTO);
        }
    }


    @KafkaListener(topics = SHOP_TOPIC_RETRY, groupId = "group_retry")
    public void listenShopTopicRetry(ShopDTO shopDTO) throws Exception {
        log.info("Retentativa de processamento: {}.", shopDTO.getIdentifier());
    }
}
