package ru.dgp.webserver.servlet;

import java.util.stream.Collectors;
import lombok.Data;
import ru.dgp.clientservice.crm.model.Client;
import ru.dgp.clientservice.crm.model.Phone;

@Data
public class ClientPlainView {

    private long id;

    private String name;

    private String address;

    private String phones;

    public ClientPlainView(Client client) {
        this.id = client.getId();
        this.name = client.getName();
        this.address = client.getAddress() != null ? client.getAddress().getStreet() : "";
        this.phones = client.getPhones().stream().map(Phone::getNumber).collect(Collectors.joining(", "));
    }
}
