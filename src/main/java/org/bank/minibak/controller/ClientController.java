package org.bank.minibak.controller;

import org.bank.minibak.model.Client;
import org.bank.minibak.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client savedClient = clientService.registerNewClient(client);

        return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    }

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance(Principal principal) {
        BigDecimal balance = clientService.getBalance(principal);

        return new ResponseEntity<>(balance, HttpStatus.OK);
    }
}
