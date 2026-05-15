package com.github.sangueamigo.modules.doacao.entity;

import com.github.sangueamigo.modules.agendamento.entity.Agendamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(length = 500)
    private String observacoes;

    @OneToOne
    @JoinColumn(name = "agendamento_id", unique = true, nullable = false)
    private Agendamento agendamento;
}
