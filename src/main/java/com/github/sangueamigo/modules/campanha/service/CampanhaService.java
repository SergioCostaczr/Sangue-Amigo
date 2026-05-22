package com.github.sangueamigo.modules.campanha.service;

import com.github.sangueamigo.modules.campanha.dto.request.AtualizarCampanhaRequest;
import com.github.sangueamigo.modules.campanha.dto.request.CriarCampanhaRequest;
import com.github.sangueamigo.modules.campanha.dto.response.CampanhaResponse;
import com.github.sangueamigo.modules.campanha.entity.Campanha;
import com.github.sangueamigo.modules.campanha.enums.StatusCampanha;
import com.github.sangueamigo.modules.campanha.enums.UrgenciaCampanha;
import com.github.sangueamigo.modules.campanha.event.CampanhaUrgenciaCriticaEvent;
import com.github.sangueamigo.modules.campanha.event.DestinatarioCampanhaCritica;
import com.github.sangueamigo.modules.campanha.exception.CampanhaNaoEncontradaException;
import com.github.sangueamigo.modules.campanha.exception.CampanhaNaoPertenceAoHemocentroException;
import com.github.sangueamigo.modules.campanha.exception.PeriodoCampanhaInvalidoException;
import com.github.sangueamigo.modules.campanha.repository.CampanhaRepository;
import com.github.sangueamigo.modules.hemocentro.entity.Hemocentro;
import com.github.sangueamigo.modules.hemocentro.service.HemocentroService;
import com.github.sangueamigo.modules.usuario.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampanhaService {

    private final CampanhaRepository campanhaRepository;
    private final HemocentroService hemocentroService;
    private final UsuarioService usuarioService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public CampanhaResponse criar(Long contaId, CriarCampanhaRequest request) {
        Hemocentro hemocentro = buscarHemocentroPorContaId(contaId);
        validarPeriodo(request.dataInicio(), request.dataFim());

        Campanha campanha = new Campanha();
        campanha.setHemocentro(hemocentro);
        campanha.setTitulo(request.titulo());
        campanha.setDescricao(request.descricao());
        campanha.setUrlImagem(request.urlImagem());
        campanha.setTiposSanguineosNecessarios(request.tiposSanguineosNecessarios());
        campanha.setDataInicio(request.dataInicio());
        campanha.setDataFim(request.dataFim());
        campanha.setEndereco(request.endereco());
        campanha.setCidade(request.cidade());
        campanha.setEstado(request.estado());
        campanha.setUrgencia(request.urgencia());
        campanha.setStatus(statusInicial(request.dataInicio()));

        Campanha salva = campanhaRepository.save(campanha);

        if (salva.getUrgencia() == UrgenciaCampanha.CRITICA){
            publicarUrgenciaCritica(salva);
        }

        return CampanhaResponse.from(salva);
    }

    @Transactional
    public CampanhaResponse atualizar(Long contaId, Long campanhaId, AtualizarCampanhaRequest request) {
        Hemocentro hemocentro = buscarHemocentroPorContaId(contaId);
        Campanha campanha = buscarCampanhaDoHemocentro(campanhaId, hemocentro.getId());
        validarPeriodo(request.dataInicio(), request.dataFim());

        UrgenciaCampanha urgenciaAnterior = campanha.getUrgencia();

        campanha.setTitulo(request.titulo());
        campanha.setDescricao(request.descricao());
        campanha.setUrlImagem(request.urlImagem());
        campanha.setTiposSanguineosNecessarios(request.tiposSanguineosNecessarios());
        campanha.setDataInicio(request.dataInicio());
        campanha.setDataFim(request.dataFim());
        campanha.setEndereco(request.endereco());
        campanha.setCidade(request.cidade());
        campanha.setEstado(request.estado());
        campanha.setStatus(request.status());
        campanha.setUrgencia(request.urgencia());

        Campanha salva = campanhaRepository.save(campanha);

        if (urgenciaAnterior != UrgenciaCampanha.CRITICA && salva.getUrgencia() == UrgenciaCampanha.CRITICA) {
            publicarUrgenciaCritica(salva);
        }

        return CampanhaResponse.from(salva);
    }

    @Transactional
    public void remover(Long contaId, Long campanhaId) {
        Hemocentro hemocentro = buscarHemocentroPorContaId(contaId);
        Campanha campanha = buscarCampanhaDoHemocentro(campanhaId, hemocentro.getId());
        campanhaRepository.delete(campanha);
    }

    public CampanhaResponse buscarPorId(Long contaId, Long campanhaId) {
        Hemocentro hemocentro = buscarHemocentroPorContaId(contaId);
        return CampanhaResponse.from(buscarCampanhaDoHemocentro(campanhaId, hemocentro.getId()));
    }

    public List<CampanhaResponse> listarDoHemocentro(Long contaId) {
        Hemocentro hemocentro = buscarHemocentroPorContaId(contaId);

        return campanhaRepository.findByHemocentroIdOrderByDataInicioDesc(hemocentro.getId())
                .stream()
                .map(CampanhaResponse::from)
                .toList();
    }

    public List<CampanhaResponse> listarAtivas() {
        return campanhaRepository.findByStatusOrderByUrgenciaDesc(StatusCampanha.ATIVA)
                .stream()
                .map(CampanhaResponse::from)
                .toList();
    }

    private Hemocentro buscarHemocentroPorContaId(Long contaId) {
        return hemocentroService.buscarEntidadePorContaId(contaId);
    }

    private Campanha buscarCampanhaDoHemocentro(Long campanhaId, Long hemocentroId) {
        Campanha campanha = campanhaRepository.findById(campanhaId)
                .orElseThrow(CampanhaNaoEncontradaException::new);

        if (!campanha.getHemocentro().getId().equals(hemocentroId)) {
            throw new CampanhaNaoPertenceAoHemocentroException();
        }

        return campanha;
    }

    private void validarPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataFim.isBefore(dataInicio)) {
            throw new PeriodoCampanhaInvalidoException();
        }
    }

    private StatusCampanha statusInicial(LocalDate dataInicio) {
        return dataInicio.isAfter(LocalDate.now()) ? StatusCampanha.AGENDADA : StatusCampanha.ATIVA;
    }

    private void publicarUrgenciaCritica(Campanha campanha) {
        List<DestinatarioCampanhaCritica> destinatarios = usuarioService
                .listarDestinatariosCompativeis(campanha.getTiposSanguineosNecessarios())
                .stream()
                .map(usuario -> new DestinatarioCampanhaCritica(
                        usuario.email(),
                        usuario.nome(),
                        usuario.tipoSanguineo()
                ))
                .toList();

        eventPublisher.publishEvent(new CampanhaUrgenciaCriticaEvent(
                campanha.getId(),
                campanha.getTitulo(),
                campanha.getDescricao(),
                campanha.getHemocentro().getNome(),
                campanha.getDataInicio(),
                campanha.getDataFim(),
                campanha.getEndereco(),
                campanha.getCidade(),
                campanha.getEstado(),
                campanha.getTiposSanguineosNecessarios()
                        .stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet()),
                destinatarios
        ));
    }
}
