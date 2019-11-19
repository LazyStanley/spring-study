package com.stanley.study.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/kafka")
public class KafkaProducerController {

    private Logger logger = LoggerFactory.getLogger(KafkaProducerController.class);

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/producer/sendMsg")
    public boolean singleSendMsg(String msg) {
        ListenableFuture<SendResult<String, String>> sender = kafkaTemplate.send("test-topic-001", msg);
        sender.addCallback(result -> {
            logger.info("send success, result={}", result);
        }, ex -> {
            logger.error("send fail", ex);
        });
        return true;
    }

    @GetMapping("/producer/sendMsg2")
    public boolean singleSendMsg2(String msg) {
        ListenableFuture<SendResult<String, String>> sender = kafkaTemplate.send("test-topic-002", msg);
        sender.addCallback(result -> {
            logger.info("send success, result={}", result);
        }, ex -> {
            logger.error("send fail", ex);
        });
        return true;
    }

}
