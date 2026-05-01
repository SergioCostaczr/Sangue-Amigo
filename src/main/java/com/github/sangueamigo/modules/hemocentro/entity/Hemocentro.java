package com.github.sangueamigo.modules.hemocentro.entity;

import com.github.sangueamigo.modules.conta.entity.Conta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hemocentro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false)
    private String endereco;

    @Column
    private String cidade;

    @Column
    private String estado;

    @JoinColumn(name = "id_conta")
    private Conta conta;

}
