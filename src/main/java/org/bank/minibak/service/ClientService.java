package org.bank.minibak.service;

import org.bank.minibak.dto.responses.FileResponse;
import org.bank.minibak.model.Client;
import org.bank.minibak.repository.ClientRepository;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.security.Principal;
import java.util.UUID;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final FileService fileService;

    public ClientService(ClientRepository clientRepository, FileService fileService) {
        this.clientRepository = clientRepository;
        this.fileService = fileService;
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

    public BigDecimal getBalance(Principal principal){
        String clientEmail = principal.getName();

        Client client = clientRepository.findByEmail(clientEmail).orElseThrow(() -> new RuntimeException("Client not found"));

        return client.getBalance();
    }

    public String uploadProfilePicture(Principal principal, MultipartFile profilePicture) {
        Client client = clientRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        String filePath = fileService.saveFile(profilePicture);

        client.setProfilePicture(filePath);

        clientRepository.save(client);

        return filePath;
    }

    public FileResponse loadProfilePicture(Principal principal) {

        Client client = clientRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        String fileName = client.getProfilePicture();

        if (fileName == null) {
            throw new RuntimeException("Profile picture not found");
        }

        Resource resource = fileService.getFile(fileName);
        Path path = fileService.getFilePath(fileName);
        String contentType = fileService.getContentType(path);

        return new FileResponse(resource, contentType);
    }
}
