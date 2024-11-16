package com.example.clinica_medica.domain.service;

import com.example.clinica_medica.domain.dto.ContatoDto;
import com.example.clinica_medica.domain.dto.EnderecoDto;
import com.example.clinica_medica.domain.dto.MedicoDto;
import com.example.clinica_medica.domain.dto.MedicoSimplificadoDto;
import com.example.clinica_medica.domain.exception.BusinessException;
import com.example.clinica_medica.domain.model.Medico;
import com.example.clinica_medica.domain.repository.MedicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicoService {
    
    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private ContatoService contatoService;

    public MedicoDto consultarDadosMedicoPeloId(Long id){

        Medico medico = medicoRepository.findOneById(id);
        MedicoDto medicoDto = toDto(medico);

        List<EnderecoDto> listaEnderecoMedico = enderecoService.buscarEnderecoPeloIdMedico(id);
        List<ContatoDto> listaContatoMedico = contatoService.buscarContatoPeloIdMedico(id);

        medicoDto.setEnderecos(listaEnderecoMedico);
        medicoDto.setContatos(listaContatoMedico);


        return medicoDto;
    }

    public List<MedicoSimplificadoDto> buscarTodosMedicos() throws BusinessException{
        try {

            List<Medico> medicos = medicoRepository.findAll();
            return toDtoListSimplificado(medicos);

        } catch (Exception e) {
            throw new BusinessException("Não foi possível listar todos os restaurantes.");
        }
    }

    @Transactional
    public void cadastroCompleto(MedicoDto medicoDto) throws BusinessException{
        try {

            Medico medico = toEntity(medicoDto);
            this.medicoRepository.save(medico);

            medicoDto.setId(medico.getId());
            enderecoService.salvarEndercoMedico(medicoDto);
            contatoService.salvarContatoMedico(medicoDto);

        } catch (BusinessException e){
            throw new BusinessException(e.getMessage());
        }
    }

    @Transactional
    public void alterarMedico(MedicoDto medicoDto) throws BusinessException {
        medicoRepository.findById(medicoDto.getId())
                .orElseThrow(() -> new BusinessException(String.format("Médico com ID %d não encontrado para atualização.", medicoDto.getId())));

        medicoRepository.updateById(medicoDto.getId(), medicoDto.getDataNascimento(), medicoDto.getCpf(), medicoDto.getNome(),
                medicoDto.getGenero().toString(), medicoDto.getNumeroRegistro(), medicoDto.getEspecializacao().toString());
    }

    @Transactional
    public void deletarMedico(Long idRestaurante) throws BusinessException{
        Medico medico = medicoRepository.findById(idRestaurante)
                .orElseThrow(() -> new BusinessException("Medico não encontrado"));

        medicoRepository.delete(medico);
    }

    private MedicoDto toDto(Medico medico) {
        if (medico == null) {
            throw new BusinessException("Medico não encontrado");
        }

        MedicoDto medicoDto = new MedicoDto();
        medicoDto.setId(medico.getId());
        medicoDto.setCpf(medico.getCpf());
        medicoDto.setDataNascimento(medico.getDataNascimento());
        medicoDto.setGenero(medico.getGenero());
        medicoDto.setNome(medico.getNome());
        medicoDto.setNumeroRegistro(medico.getNumeroRegistro());
        medicoDto.setEspecializacao(medico.getEspecializacao());
        medicoDto.setConsultas(medico.getConsultas());

        return medicoDto;
    }

    private MedicoSimplificadoDto toDtoSimplificado(Medico medico) {
        if (medico == null) {
            throw new BusinessException("Medico não encontrado");
        }

        MedicoSimplificadoDto medicoDto = new MedicoSimplificadoDto();

        medicoDto.setId(medico.getId());
        medicoDto.setCpf(medico.getCpf());
        medicoDto.setDataNascimento(medico.getDataNascimento());
        medicoDto.setGenero(medico.getGenero());
        medicoDto.setNome(medico.getNome());
        medicoDto.setNumeroRegistro(medico.getNumeroRegistro());
        medicoDto.setEspecializacao(medico.getEspecializacao());

        return medicoDto;
    }

    private Medico toEntity(MedicoDto medicoDto) {
        if (medicoDto == null) {
            throw new BusinessException("Medico não encontrado");
        }

        Medico medico = new Medico();
        medico.setId(medicoDto.getId());
        medico.setCpf(medicoDto.getCpf());
        medico.setNome(medicoDto.getNome());
        medico.setGenero(medicoDto.getGenero());
        medico.setDataNascimento(medicoDto.getDataNascimento());
        medico.setNumeroRegistro(medicoDto.getNumeroRegistro());
        medico.setEspecializacao(medicoDto.getEspecializacao());

        return medico;
    }

    private List<MedicoDto> toDtoList(List<Medico> medicos) {

        return medicos.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

    }

    private List<MedicoSimplificadoDto> toDtoListSimplificado(List<Medico> medicos) {

        return medicos.stream()
                .map(this::toDtoSimplificado)
                .collect(Collectors.toList());

    }

}
