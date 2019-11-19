package com.stanley.study.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaInitConfig {

    @Bean
    public NewTopic initTopic1() {
        return new NewTopic("test-topic-001", 1, (short) 1);
    }

    @Bean
    public NewTopic initTopic2() {
        return new NewTopic("test-topic-002", 2, (short) 1);
    }

}
