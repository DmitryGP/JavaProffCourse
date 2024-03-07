package ru.dgp.clientservice.crm.controller;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.dgp.clientservice.crm.dto.ClientDto;
import ru.dgp.clientservice.crm.model.Address;
import ru.dgp.clientservice.crm.model.Client;
import ru.dgp.clientservice.crm.model.Phone;
import ru.dgp.clientservice.crm.service.ClientService;

@Controller
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/clients")
    public String clientListView(Model model) {
        var clients = clientService.findAll().stream().map(ClientDto::new).toList();

        var newClient = new ClientDto();

        model.addAttribute("clients", clients);
        model.addAttribute("clientPlainView", newClient);

        return "clients";
    }

    @PostMapping("/client/save")
    public String createClient(ClientDto client) {

        var address = new Address(client.getAddress());
        var phones = Arrays.stream(client.getPhones().split(","))
                .map(ph -> new Phone(ph, null))
                .collect(Collectors.toSet());

        var newClient = new Client(null, client.getName(), address, phones);

        clientService.saveClient(newClient);

        return "redirect:/clients";
    }
}
