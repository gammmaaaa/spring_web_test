package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.*;
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
public class Account extends EntityObject {
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private AccountTypeEnum accountType;

    @Column(name = "balance", precision = 19, scale = 2)
    private BigDecimal balance;

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
