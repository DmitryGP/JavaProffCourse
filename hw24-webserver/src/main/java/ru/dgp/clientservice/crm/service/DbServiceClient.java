package ru.dgp.clientservice.crm.service;

import java.util.List;
import java.util.Optional;
import ru.dgp.clientservice.crm.model.Client;

public interface DbServiceClient {

    Client saveClient(Client client);

    Optional<Client> getClient(long id);

    List<Client> findAll();
}
