package com.jpmc.midascore.foundation;


import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Component;


@Component
public class TransactionListener{

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core")
    public void listen(Transaction transaction) { // This is a method that gets the transaction object from the Kafka topic and prints it to the console
        System.out.println("Received transaction: " + transaction);
    }
}