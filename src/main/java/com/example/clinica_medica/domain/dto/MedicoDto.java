package com.example.clinica_medica.domain.dto;

import com.example.clinica_medica.domain.enuns.Especializacao;
import com.example.clinica_medica.domain.enuns.Genero;
import com.example.clinica_medica.domain.model.Consulta;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicoDto {

    private Long id;

    @NotNull(message = "O nome não pode ser nulo")
    private String nome;

    @NotNull(message = "O CPF não pode ser nulo")
    private String cpf;

    @NotNull(message = "A data de nascimento não pode ser nula")
    @JsonFormat(pattern = "dd-mm-yyyy")
    private Date dataNascimento;

    private String numeroRegistro;
    private Genero genero;
    private Especializacao especializacao;
    private List<EnderecoDto> enderecos;
    private List<ContatoDto> contatos;
    private List<Consulta> consultas;

}
