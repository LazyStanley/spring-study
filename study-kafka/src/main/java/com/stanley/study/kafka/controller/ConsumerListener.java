package com.stanley.study.kafka.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerListener {

    private final Logger logger = LoggerFactory.getLogger(ConsumerListener.class);

    @KafkaListener(topics = "testTopic")
    public void dealMessage(String msg) {
        logger.info(msg);
    }

}
