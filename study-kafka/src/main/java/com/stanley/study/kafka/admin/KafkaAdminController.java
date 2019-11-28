package com.stanley.study.kafka.admin;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/kafka/admin")
public class KafkaAdminController {

    private Logger logger = LoggerFactory.getLogger(KafkaAdminController.class);

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Autowired
    private AdminClient adminClient;

    @RequestMapping("/listTopics")
    public ResponseEntity listTopics() throws ExecutionException, InterruptedException {
        ListTopicsResult listTopicsResult = adminClient.listTopics();
        Map<String, TopicListing> stringTopicListingMap = listTopicsResult.namesToListings().get();
        for (Map.Entry<String, TopicListing> stringTopicListingEntry : stringTopicListingMap.entrySet()) {
            String key = stringTopicListingEntry.getKey();
            TopicListing value = stringTopicListingEntry.getValue();
            logger.info("key={}", key);
            logger.info("value={}", JSON.toJSONString(value));
        }
        return ResponseEntity.ok(stringTopicListingMap);
    }

    @RequestMapping("/listConsumerGroups")
    public ResponseEntity listConsumerGroups() throws ExecutionException, InterruptedException {
        ListConsumerGroupsResult listConsumerGroupsResult = adminClient.listConsumerGroups();
        Collection<ConsumerGroupListing> consumerGroupListings = listConsumerGroupsResult.all().get();
        for (ConsumerGroupListing consumerGroupListing : consumerGroupListings) {
            logger.info("consumer groupId={}", consumerGroupListing.groupId());
            logger.info("isSimpleConsumerGroup={}", consumerGroupListing.isSimpleConsumerGroup());
        }
        return ResponseEntity.ok(Collections.singletonList(consumerGroupListings.stream().map(ConsumerGroupListing::groupId).collect(Collectors.toList())));
    }

    @Bean
    public AdminClient adminClient() {
        Properties properties = new Properties();
        properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return KafkaAdminClient.create(properties);
    }

}
