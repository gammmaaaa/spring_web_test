package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;
import ru.t1.java.demo.model.enums.AccountTypeEnum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
public class Account extends AbstractPersistable<Long> {
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private AccountTypeEnum accountType;

    @Column(name = "balance", precision = 19, scale = 2)
    private BigDecimal balance;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "account")
    private List<Transaction> transactions = new ArrayList<>();
}
