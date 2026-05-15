package com.github.sangueamigo.modules.campanha.entity;

import com.github.sangueamigo.modules.campanha.enums.StatusCampanha;
import com.github.sangueamigo.modules.campanha.enums.UrgenciaCampanha;
import com.github.sangueamigo.modules.hemocentro.entity.Hemocentro;
import com.github.sangueamigo.modules.usuario.enums.TipoSanguineo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "campanha")
public class Campanha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 500)
    private String descricao;

    @Column
    private String urlImagem;

    @ElementCollection
    @CollectionTable(
            name = "campanha_tipos_sanguineos",
            joinColumns = @JoinColumn(name = "campanha_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_sanguineo", nullable = false)
    private Set<TipoSanguineo> tiposSanguineosNecessarios = new HashSet<>();

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column(nullable = false)
    private LocalDate dataFim;

    @Column
    private String endereco;

    @Column
    private String cidade;

    @Column
    private String estado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCampanha status = StatusCampanha.AGENDADA;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UrgenciaCampanha urgencia = UrgenciaCampanha.NORMAL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hemocentro_id", nullable = false)
    private Hemocentro hemocentro;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadaEm = LocalDateTime.now();
}

