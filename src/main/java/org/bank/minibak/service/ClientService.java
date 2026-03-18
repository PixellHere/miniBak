package org.bank.minibak.service;

import org.bank.minibak.model.Client;
import org.bank.minibak.repository.ClientRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client registerNewClient(Client newClient) {

        if (newClient == null) {
            throw new IllegalArgumentException("Client must not be null");
        }

        if(clientRepository.existsByEmail(newClient.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newClient.getPassword());
        newClient.setPassword(encodedPassword);

        return clientRepository.save(newClient);
    }
}
