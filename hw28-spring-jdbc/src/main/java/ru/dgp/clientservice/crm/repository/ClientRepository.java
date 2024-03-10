package ru.dgp.clientservice.crm.repository;

import java.util.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import ru.dgp.clientservice.crm.model.Client;

public interface ClientRepository extends ListCrudRepository<Client, Long> {

    @Override
    @Query(
            value =
                    """
            select  c.id as client_id,
                    c.name as client_name,
                    a.id as address_id,
                    a.street as address_street,
                    ph.id as phone_id,
                    ph.number as phone_number
            from client c
                left outer join address a
                        on c.id = a.client_id
                left outer join phone ph
                        on c.id = ph.client_id
            order by c.id
            """,
            resultSetExtractorClass = ClientResultSetExtractor.class)
    List<Client> findAll();
}
