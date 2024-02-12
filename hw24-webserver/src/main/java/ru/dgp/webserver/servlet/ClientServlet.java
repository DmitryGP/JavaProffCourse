package ru.dgp.webserver.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import ru.dgp.clientservice.crm.model.Address;
import ru.dgp.clientservice.crm.model.Client;
import ru.dgp.clientservice.crm.model.Phone;
import ru.dgp.clientservice.crm.service.DbServiceClient;
import ru.dgp.webserver.services.TemplateProcessor;

public class ClientServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";

    private final transient DbServiceClient dbServiceClient;

    private final transient TemplateProcessor templateProcessor;

    public ClientServlet(DbServiceClient dbServiceClient, TemplateProcessor templateProcessor) {
        this.dbServiceClient = dbServiceClient;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();

        var clients =
                dbServiceClient.findAll().stream().map(ClientPlainView::new).toList();
        paramsMap.put("clients", clients);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String phones = request.getParameter("phones");

        Client newClient = new Client(name);

        if (address != null) {
            newClient.setAddress(new Address(null, address));
        }

        if (phones != null) {
            var phoneList = Arrays.stream(phones.split(","))
                    .map(ph -> new Phone(null, ph))
                    .toList();
            newClient.getPhones().addAll(phoneList);
        }

        dbServiceClient.saveClient(newClient);

        response.sendRedirect("/clients");
    }
}
