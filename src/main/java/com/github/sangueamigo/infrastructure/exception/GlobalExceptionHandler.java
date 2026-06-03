package com.github.sangueamigo.infrastructure.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.github.sangueamigo.modules.agendamento.exception.*;
import com.github.sangueamigo.modules.campanha.exception.CampanhaNaoEncontradaException;
import com.github.sangueamigo.modules.campanha.exception.CampanhaNaoPertenceAoHemocentroException;
import com.github.sangueamigo.modules.campanha.exception.PeriodoCampanhaInvalidoException;
import com.github.sangueamigo.modules.conta.exception.*;
import com.github.sangueamigo.modules.hemocentro.exception.*;
import com.github.sangueamigo.modules.usuario.exception.UsuarioNaoEncontradoException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Adicionar no GlobalExceptionHandler
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> handleDeserializacao(HttpMessageNotReadableException ex) {
        String mensagem = "Valor inválido no corpo da requisição.";

        Throwable causa = ex.getCause();
        if (causa instanceof InvalidFormatException invalidFormat) {
            String campo = invalidFormat.getPath().isEmpty()
                    ? "desconhecido"
                    : invalidFormat.getPath().get(0).getFieldName();

            String valorInformado = String.valueOf(invalidFormat.getValue());

            if (invalidFormat.getTargetType() != null && invalidFormat.getTargetType().isEnum()) {
                String valoresValidos = Arrays.stream(invalidFormat.getTargetType().getEnumConstants())
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));

                mensagem = "Valor '" + valorInformado + "' inválido para o campo '" + campo +
                        "'. Valores aceitos: " + valoresValidos;
            }
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErroResponse.of(HttpStatus.BAD_REQUEST, mensagem));
    }

    // Validação de campos (@Valid / @Validated)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidacao(MethodArgumentNotValidException ex){
        String mensagem = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(f -> f.getField() +": " + f.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErroResponse.of(HttpStatus.BAD_REQUEST, mensagem));
    }

    // 409 Conflict duplicidades
    @ExceptionHandler({
            EmailJaCadastradoException.class,
            CpfJaCadastradoException.class,
            CnpjJaCadastradoException.class,
            HorarioDisponivelDuplicadoException.class,
            DoacaoJaRegistradaException.class
    })
    public ResponseEntity<ErroResponse> handleConflito (RuntimeException ex){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErroResponse.of(HttpStatus.CONFLICT, ex.getMessage()));
    }

    // 401 Unauthorized autenticação
    @ExceptionHandler({
            CredenciaisInvalidasException.class,
            TokenInvalidoException.class
    })
    public ResponseEntity<ErroResponse> handleNaoAutorizado(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErroResponse.of(HttpStatus.UNAUTHORIZED, ex.getMessage()));
    }

    // 403 Forbidden ownership
    @ExceptionHandler({
            AgendamentoNaoPertenceAoUsuarioException.class,
            CampanhaNaoPertenceAoHemocentroException.class,
            HorarioNaoPertenceAoHemocentroException.class
    })
    public ResponseEntity<ErroResponse> handleProibido(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErroResponse.of(HttpStatus.FORBIDDEN, ex.getMessage()));
    }

    // 404 Not Found
    @ExceptionHandler({
            AgendamentoNaoEncontradoException.class,
            CampanhaNaoEncontradaException.class,
            HemocentroNaoEncontradoException.class,
            HorarioDisponivelNaoEncontradoException.class,
            UsuarioNaoEncontradoException.class,
            EntityNotFoundException.class
    })
    public ResponseEntity<ErroResponse> handleNaoEncontrado(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErroResponse.of(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    // 422 Unprocessable Entity regras de negócio
    @ExceptionHandler({
            UsuarioInaptoException.class,
            HorarioIndisponivelException.class,
            HorarioComAgendamentosAtivosException.class,
            PeriodoCampanhaInvalidoException.class,
            IllegalStateException.class
    })
    public ResponseEntity<ErroResponse> handleRegraDeNegocio(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ErroResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage()));
    }

    // 400 Bad Request QR Code
    @ExceptionHandler(QrCodeInvalidoException.class)
    public ResponseEntity<ErroResponse> handleQrCode(QrCodeInvalidoException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErroResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    // 500 fallback genérico
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGenerico(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErroResponse.of(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Erro interno. Tente novamente mais tarde."
                ));
    }
}
