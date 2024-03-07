package ru.dgp.clientservice.crm.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.dgp.clientservice.crm.model.Address;
import ru.dgp.clientservice.crm.model.Client;
import ru.dgp.clientservice.crm.model.Phone;

public class ClientResultSetExtractor implements ResultSetExtractor<List<Client>> {
    @Override
    public List<Client> extractData(ResultSet rs) throws SQLException, DataAccessException {
        var clientList = new ArrayList<Client>();

        long prevClientId = 0;
        Client client = null;

        while (rs.next()) {
            var clientId = rs.getLong("client_id");

            if (prevClientId == 0 || prevClientId != clientId) {
                var address = new Address(rs.getLong("address_id"), rs.getString("address_street"), clientId);

                client = new Client(clientId, rs.getString("client_name"), address, new HashSet<>());

                clientList.add(client);
                prevClientId = clientId;
            }

            var phoneId = rs.getObject("phone_id", Long.class);

            if (phoneId != null) {
                client.getPhones().add(new Phone(phoneId, rs.getString("phone_number")));
            }
        }

        return clientList;
    }
}
