package com.github.sangueamigo.modules.usuario.entity;

import com.github.sangueamigo.modules.conta.entity.Conta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column
    private String telefone;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @OneToOne
    @JoinColumn(name = "conta_id")
    private Conta conta;

}
