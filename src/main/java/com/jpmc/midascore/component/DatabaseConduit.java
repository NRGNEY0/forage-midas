package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.entity.TransactionRecord;

@Component
public class DatabaseConduit {
    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final RestTemplate restTemplate;

    public DatabaseConduit(UserRepository userRepository, TransactionRecordRepository transactionRecordRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.restTemplate = restTemplate;
    }



    public void processTransaction(Transaction transaction){
        System.out.println("1- Entered process transaction");

       UserRecord sender = userRepository.findById(transaction.getSenderId()); 
       System.out.println("Found sender");

       UserRecord recipient = userRepository.findById(transaction.getRecipientId()); 
       System.out.println("Found recipient");

       if(sender == null || recipient == null) { 
           System.out.println("Sender or recipient not found for transaction: " + transaction);
           return; 
       }

       if(sender.getBalance() < transaction.getAmount()) {
           System.out.println("Insufficient balance for transaction: " + transaction);
           return; 
       }

       //APi integration

       Incentive incentive = restTemplate.postForObject(
        "http://localhost:8080/incentive",
         transaction, // This object is the one that gets sent to the url
            Incentive.class // Whatever the API returns back gets turned into this class
         );
       
       if(sender.getBalance() >= transaction.getAmount()){

           sender.setBalance(sender.getBalance() - transaction.getAmount());

           recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentive.getAmount()); 
           
       } 

       userRepository.save(sender); // Saves the updated sender record to the database
       
       userRepository.save(recipient); // Saves the updated recipient record to the database   

      

        TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, transaction.getAmount(), incentive.getAmount()); // Creates a new transaction record
       System.out.println("Transaction record created: " + transactionRecord);

        transactionRecordRepository.save(transactionRecord); // Saves the transaction record to the database
       System.out.println("Transaction processed and saved: " + transactionRecord);
    }

    
    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

}
