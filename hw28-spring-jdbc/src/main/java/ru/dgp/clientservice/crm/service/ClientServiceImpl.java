package ru.dgp.clientservice.crm.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.dgp.clientservice.crm.model.Client;
import ru.dgp.clientservice.crm.repository.ClientRepository;
import ru.dgp.clientservice.crm.transaction.TransactionManager;

@Service
public class ClientServiceImpl implements ClientService {
    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;
    private final TransactionManager transactionManager;

    public ClientServiceImpl(TransactionManager transactionManager, ClientRepository clientRepository) {
        this.transactionManager = transactionManager;
        this.clientRepository = clientRepository;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(() -> {
            var savedClient = clientRepository.save(client);
            log.info("updated client: {}", savedClient);
            return savedClient;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        var client = clientRepository.findById(id);
        log.info("client: {}", client);
        return client;
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }
}
