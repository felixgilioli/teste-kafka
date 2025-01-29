package br.com.felixgilioli.shop.report.event;

import br.com.felixgilioli.shop.report.dto.ShopDTO;
import br.com.felixgilioli.shop.report.repository.ReportRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ReceiveKafkaMessage {

    private static final String SHOP_TOPIC_EVENT_NAME = "SHOP_TOPIC_EVENT";
    private static final Logger log = LoggerFactory.getLogger(ReceiveKafkaMessage.class);

    private final ReportRepository reportRepository;

    public ReceiveKafkaMessage(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Transactional
    @KafkaListener(topics = SHOP_TOPIC_EVENT_NAME, groupId = "group_report")
    public void listenShopTopic(ShopDTO shopDTO) {
        try {
            log.info("Compra recebida no tópico: {}.", shopDTO.getIdentifier());
            reportRepository.incrementShopStatus(shopDTO.getStatus());
        } catch (Exception e) {
            log.error("Erro no processamento da mensagem", e);
        }
    }
}
