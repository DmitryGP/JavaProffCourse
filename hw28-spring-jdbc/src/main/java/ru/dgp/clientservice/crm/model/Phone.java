package ru.dgp.clientservice.crm.model;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table(name = "phone")
@Slf4j
public class Phone {

    @Id
    private Long id;

    @Nonnull
    private final String number;

    @Nonnull
    private Long clientId;

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    @PersistenceCreator
    public Phone(Long id, String number, Long clientId) {
        this.id = id;
        this.number = number;
        this.clientId = clientId;
    }

    public Phone(String number, Long clientId) {
        this(null, number, clientId);
    }
}
