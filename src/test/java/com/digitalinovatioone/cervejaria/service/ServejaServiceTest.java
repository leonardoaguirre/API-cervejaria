package com.digitalinovatioone.cervejaria.service;

import com.digitalinovatioone.cervejaria.builder.CervejaDTOBuilder;
import com.digitalinovatioone.cervejaria.dto.CervejaDTO;
import com.digitalinovatioone.cervejaria.entity.Cerveja;
import com.digitalinovatioone.cervejaria.exception.BebidaJaRegistradaException;
import com.digitalinovatioone.cervejaria.mapper.CervejaMapper;
import com.digitalinovatioone.cervejaria.repository.CervejaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ServejaServiceTest {
    private static final Long ID_CERVEJA_INVALIDO =1L;

    @Mock
    private CervejaRepository cervejaRepository;

    private CervejaMapper cervejaMapper = CervejaMapper.INSTANCE;

    @InjectMocks
    private CervejaService cervejaService;

    @Test
    void quandoCervejaInformadaEntaoDeveSerCriada() throws BebidaJaRegistradaException {
        //Given
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().getCervejaDTO();
        Cerveja cerveja_a_ser_salva = cervejaMapper.toModel(cervejaDTO);
        //When
        Mockito.when(cervejaRepository.findByNome(cervejaDTO.getNome())).thenReturn(Optional.empty());
        Mockito.when(cervejaRepository.save(cerveja_a_ser_salva)).thenReturn(cerveja_a_ser_salva);
        //Then
        CervejaDTO cervejaDTOcriada = cervejaService.criaCerveja(cervejaDTO);

        assertEquals(cervejaDTO.getId(),cervejaDTOcriada.getId());
        assertEquals(cervejaDTO.getNome(),cervejaDTOcriada.getNome());
    }
}
