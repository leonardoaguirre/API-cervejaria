package com.digitalinovatioone.cervejaria.service;

import com.digitalinovatioone.cervejaria.dto.CervejaDTO;
import com.digitalinovatioone.cervejaria.dto.response.ResponseMessage;
import com.digitalinovatioone.cervejaria.entity.Cerveja;
import com.digitalinovatioone.cervejaria.exception.BebidaJaRegistradaException;
import com.digitalinovatioone.cervejaria.exception.BebidaNaoEncontradaException;
import com.digitalinovatioone.cervejaria.exception.BebidaNaoExisteException;
import com.digitalinovatioone.cervejaria.mapper.CervejaMapper;
import com.digitalinovatioone.cervejaria.repository.CervejaRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CervejaService {
    private final CervejaRepository cervejaRepository;

    private final CervejaMapper cervejaMapper = CervejaMapper.INSTANCE;

    public CervejaDTO criaCerveja(CervejaDTO cervejaDTO) throws BebidaJaRegistradaException {
        verificaSeJaEstaCastrada(cervejaDTO.getNome());
        Cerveja cerveja_a_salvar = cervejaMapper.toModel(cervejaDTO);

        Cerveja cervejaSalva = cervejaRepository.save(cerveja_a_salvar);
        return cervejaMapper.toDTO(cervejaSalva);
    }
    public CervejaDTO procuraPorNome(String nome) throws BebidaNaoEncontradaException {
        Cerveja cerveja = cervejaRepository.findByNome(nome)
                .orElseThrow(()-> new BebidaNaoEncontradaException(nome));
        return cervejaMapper.toDTO(cerveja);
    }

    public List<CervejaDTO> listar(){
        return cervejaRepository.findAll()
                .stream()
                .map(cervejaMapper::toDTO)
                .collect(Collectors.toList());
    }
    public void deletaPorId(Long id) throws BebidaNaoExisteException {
        verificaSeExiste(id);

        cervejaRepository.deleteById(id);
    }
    public ResponseMessage alterarPorId(Long id,CervejaDTO cervejaDTO) throws BebidaNaoExisteException {
        verificaSeExiste(id);

        Cerveja cerveja_a_alterar = cervejaMapper.toModel(cervejaDTO);
        Cerveja cervejaAlterada = cervejaRepository.save(cerveja_a_alterar);
        return  getResponseMessage(cervejaAlterada,"Cerveja alterada com sucesso! id: ");
    }

    private void verificaSeJaEstaCastrada(String nome) throws BebidaJaRegistradaException {
        Optional<Cerveja> cervejaSalva = cervejaRepository.findByNome(nome);
        if(cervejaSalva.isPresent()) throw new BebidaJaRegistradaException(nome);
    }
    private Cerveja verificaSeExiste(Long id) throws BebidaNaoExisteException {
       return cervejaRepository.findById(id)
                .orElseThrow(()->  new BebidaNaoExisteException(id));
    }
    private ResponseMessage getResponseMessage(Cerveja cervejaSalva, String message) {
        return ResponseMessage
                .builder()
                .message(message + cervejaSalva.getId())
                .build();
    }

}
