package com.stanley.study.kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class ProducerController {

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/message/send")
    public boolean send(@RequestParam String message){
        kafkaTemplate.send("testTopic",message);
        return true;
    }

}
