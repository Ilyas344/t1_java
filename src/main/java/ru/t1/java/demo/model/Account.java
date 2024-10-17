package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "accounts")
@NoArgsConstructor
public class Account extends AbstractPersistable<Long> {

    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    @Column(name = "balance", precision = 19, scale = 2)
    private BigDecimal balance;
    private Boolean isBlocked;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    public Account(AccountType accountType, BigDecimal balance) {
        this.accountType = accountType;
        this.balance = balance;
        this.isBlocked = false;
    }


}
