package com.github.sangueamigo.modules.agendamento.entity;

import com.github.sangueamigo.modules.agendamento.enums.StatusAgendamento;
import com.github.sangueamigo.modules.doacao.entity.Doacao;
import com.github.sangueamigo.modules.hemocentro.entity.Hemocentro;
import com.github.sangueamigo.modules.horariodisponivel.entity.HorarioDisponivel;
import com.github.sangueamigo.modules.usuario.entity.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "agendamento",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"usuario_id", "horario_id"} // impede que o mesmo usuário agende o mesmo horário duas vezes.
        )
)
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime horario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamento status = StatusAgendamento.PENDENTE;

    @Column(unique = true)
    private String qrCodeToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hemocentro_id", nullable = false)
    private Hemocentro hemocentro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "horario_id", nullable = false)
    private HorarioDisponivel horarioDisponivel;

    @OneToOne(mappedBy = "agendamento", cascade = CascadeType.ALL)
    private Doacao doacao; // null ate validacao do Qr code
}
