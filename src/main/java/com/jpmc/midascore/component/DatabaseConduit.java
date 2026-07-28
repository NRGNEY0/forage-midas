package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import org.springframework.stereotype.Component;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.entity.TransactionRecord;

@Component
public class DatabaseConduit {
    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;

    public DatabaseConduit(UserRepository userRepository, TransactionRecordRepository transactionRecordRepository) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }

    public void processTransaction(Transaction transaction){
        System.out.println("1- Entered process transaction");
       UserRecord sender = userRepository.findById(transaction.getSenderId()); // Tries to find the sender in the database
       System.out.println("Found sender");

       UserRecord recipient = userRepository.findById(transaction.getRecipientId()); 
       System.out.println("Found recipient");

       if(sender == null || recipient == null) { // Checks if either the sender or recipient is not found in the database
           System.out.println("Sender or recipient not found for transaction: " + transaction);
           return; // Transaction cannot proceed if either party is not found
       }

       if(sender.getBalance() >= transaction.getAmount()){ // Checks if the sender has enough balance to make the transaction
           sender.setBalance(sender.getBalance() - transaction.getAmount()); // Deducts the amount from the sender's balance
           recipient.setBalance(recipient.getBalance() + transaction.getAmount()); // Adds the amount to the recipient's balance
       } else {
           System.out.println("Insufficient balance for transaction: " + transaction);
       }
        TransactionRecord transactionRecord = new TransactionRecord(transaction.getAmount(), sender, recipient); // Creates a new transaction record
       
        transactionRecordRepository.save(transactionRecord); // Saves the transaction record to the database
       
    }

    
    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

}
