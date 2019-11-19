package com.stanley.study.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaConsumer {

    private Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    /**
     * 近的覆盖远的 groupId 配置
     *
     * @param msg msg
     */
    @KafkaListener(topics = "test-topic-001", groupId = "test2")
    public void singleConsumeMsg(@Payload String msg,
                                 @Header(KafkaHeaders.GROUP_ID) String groupId) {
        logger.info("singleConsumeMsg={}", msg);
        logger.info("groupId={}", groupId);
    }

    @KafkaListener(topics = "test-topic-002", containerFactory = "kafkaBatchListener3", groupId = "test-group-002")
    public void batchConsumeMsg(List<ConsumerRecord<String, String>> records, Acknowledgment ack) throws InterruptedException {
        logger.info("records.size={}", records.size());
        for (ConsumerRecord<String, String> record : records) {
            logger.info("record.partition={}", record.partition());
            logger.info("record.msg={}", record.value());
            if (record.value().equals("001")) {
                // 睡两秒，测试 MAX_POLL_INTERVAL_MS_CONFIG
                Thread.sleep(2000);
            }
        }
        ack.acknowledge();
    }

}
