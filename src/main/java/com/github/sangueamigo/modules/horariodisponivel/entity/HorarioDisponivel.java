package com.github.sangueamigo.modules.horariodisponivel.entity;

import com.github.sangueamigo.modules.hemocentro.entity.Hemocentro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(
        name = "horario_disponivel",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"hemocentro_id", "data", "hora"} // um hemocentro nao pode ter dois horarios com a mesma data e hora
        )
)
public class HorarioDisponivel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime hora;

    @Column(nullable = false)
    private Integer vagas;

    @Column(nullable = false)
    private Boolean disponivel = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hemocentro_id", nullable = false)
    private Hemocentro hemocentro;
}
