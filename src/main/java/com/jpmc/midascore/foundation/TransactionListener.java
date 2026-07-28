package com.jpmc.midascore.foundation;


import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Component;

import com.jpmc.midascore.component.DatabaseConduit;


@Component
public class TransactionListener{
    private final DatabaseConduit databaseConduit;

    public TransactionListener(DatabaseConduit databaseConduit) {
        this.databaseConduit = databaseConduit;
    }

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core")
    public void listen(Transaction transaction) { // This is a method that gets the transaction object from the Kafka topic

        databaseConduit.processTransaction(transaction);
        

        System.out.println("Received transaction: " + transaction);
    }
}