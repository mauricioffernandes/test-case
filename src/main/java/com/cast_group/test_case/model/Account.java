package com.cast_group.test_case.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ownerName;  // Nome do proprietário da conta

    private double balance;  // Saldo da conta

    @Version  // Controle de versão para concorrência otimista
    private int version;

}

