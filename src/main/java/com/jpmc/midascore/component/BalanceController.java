package com.jpmc.midascore.component;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.foundation.Balance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {
    private final DatabaseConduit databaseConduit;

    public BalanceController(DatabaseConduit databaseConduit) {
        this.databaseConduit = databaseConduit;
    }   

    @GetMapping(value = "/balance")
    public Balance getbalance(@RequestParam("userId") long userId) {
         Balance balance = databaseConduit.getBalance(userId);
        return balance;
    }

     
}
