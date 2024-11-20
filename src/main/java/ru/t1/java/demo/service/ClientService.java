package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.ClientDTO;
import ru.t1.java.demo.model.Client;

import java.util.List;

public interface ClientService {
    List<Client> registerClients(List<Client> clients);

    Client registerClient(Client client);

    List<ClientDTO> parseJson();

    void clearMiddleName(List<ClientDTO> dtos);

    Client getClientById(long id);
}
