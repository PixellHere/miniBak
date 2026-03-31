package org.bank.minibak.controller;

import org.bank.minibak.dto.responses.FileResponse;
import org.bank.minibak.model.Client;
import org.bank.minibak.repository.ClientRepository;
import org.bank.minibak.service.ClientService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;
    private final ClientRepository clientRepository;

    public ClientController(ClientService clientService, ClientRepository clientRepository) {
        this.clientService = clientService;
        this.clientRepository = clientRepository;
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

    @PostMapping("/profile-picture")
    public ResponseEntity<?> uploadPicture(
            Principal principal,
            @RequestParam("file") MultipartFile file) {

        String fileName = clientService.uploadProfilePicture(principal, file);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Upload successful");
        response.put("imageUrl", "/images/" + fileName);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile-picture")
    public ResponseEntity<Resource> getProfilePicture(Principal principal) {
        FileResponse file = clientService.loadProfilePicture(principal);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, file.contentType())
                .body(file.resource());
    }
}
