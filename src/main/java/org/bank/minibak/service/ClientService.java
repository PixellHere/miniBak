package org.bank.minibak.service;

import org.bank.minibak.model.Client;

import java.math.BigDecimal;

public class ClientService {
    Client client;

    public ClientService(Client client) {
        this.client = client;
    }

    public void addMoney(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0)
            client.setBalance(client.getBalance().add(amount)); // To do SQL communication
        else
            throw new IllegalArgumentException("Amount must be greater than 0");
    }
}
