package com.digitalinovatioone.cervejaria.service;

import com.digitalinovatioone.cervejaria.builder.CervejaDTOBuilder;
import com.digitalinovatioone.cervejaria.dto.CervejaDTO;
import com.digitalinovatioone.cervejaria.entity.Cerveja;
import com.digitalinovatioone.cervejaria.exception.BebidaJaRegistradaException;
import com.digitalinovatioone.cervejaria.exception.BebidaNaoEncontradaException;
import com.digitalinovatioone.cervejaria.exception.BebidaNaoExisteException;
import com.digitalinovatioone.cervejaria.mapper.CervejaMapper;
import com.digitalinovatioone.cervejaria.repository.CervejaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CervejaServiceTest {
    private static final Long ID_CERVEJA_INVALIDO =1L;

    @Mock
    private CervejaRepository cervejaRepository;

    private final CervejaMapper cervejaMapper = CervejaMapper.INSTANCE;

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

        assertThat(cervejaDTO.getId(), is(equalTo((cervejaDTO.getId()))));
        assertThat(cervejaDTO.getNome(), is(equalTo((cervejaDTO.getNome()))));
        assertThat(cervejaDTO.getQuantidade(), is(equalTo((cervejaDTO.getQuantidade()))));
        assertThat(cervejaDTO.getQuantidade(), is(greaterThan(9)));

//        assertEquals(cervejaDTO.getId(),cervejaDTOcriada.getId());
//        assertEquals(cervejaDTO.getNome(),cervejaDTOcriada.getNome());
    }

    @Test
    void quandoCervejaJaRegistradaUmaExcecaoDeveSerLancada(){
        //Given
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().getCervejaDTO();
        Cerveja cervejaDuplicada = cervejaMapper.toModel(cervejaDTO);
        //When
        Mockito.when(cervejaRepository.findByNome(cervejaDTO.getNome())).thenReturn(Optional.of(cervejaDuplicada));
        //then
        assertThrows(BebidaJaRegistradaException.class,()-> cervejaService.criaCerveja(cervejaDTO));
    }

    @Test
    void quandoNomeValidoDeCervejaEInformadoRetornaUmaCerveja() throws BebidaNaoEncontradaException {
        //Given
        CervejaDTO cervejaProcuradaEsperadaDTO = CervejaDTOBuilder.builder().build().getCervejaDTO();
        Cerveja cervejaProcuradaEsperada = cervejaMapper.toModel(cervejaProcuradaEsperadaDTO);
        //When
        Mockito.when(cervejaRepository.findByNome(cervejaProcuradaEsperada.getNome())).thenReturn(Optional.of(cervejaProcuradaEsperada));
        //Then
        CervejaDTO cervejaAchadaDTO = cervejaService.procuraPorNome(cervejaProcuradaEsperadaDTO.getNome());
        assertThat(cervejaAchadaDTO, is(equalTo(cervejaProcuradaEsperadaDTO)));
    }

    @Test
    void quandoUmaCervejaNaoRegistradaLancaUmaExcecao(){
        //Given
        CervejaDTO cervejaProcuradaEsperadaDTO = CervejaDTOBuilder.builder().build().getCervejaDTO();

        //When
        Mockito.when(cervejaRepository.findByNome(cervejaProcuradaEsperadaDTO.getNome())).thenReturn(Optional.empty());
        //Then
        assertThrows(BebidaNaoEncontradaException.class,
                ()->cervejaService.procuraPorNome(cervejaProcuradaEsperadaDTO.getNome()));
    }

    @Test
    void quandoListaCervejaChamadoRetornaUmaListaDeCerveja() {
        //Given
        CervejaDTO cervejaProcuradaEsperadaDTO = CervejaDTOBuilder.builder().build().getCervejaDTO();
        Cerveja cervejaProcuradaEsperada = cervejaMapper.toModel(cervejaProcuradaEsperadaDTO);
        //When
        Mockito.when(cervejaRepository.findAll()).thenReturn(Collections.singletonList(cervejaProcuradaEsperada));
        //Then
        List<CervejaDTO> listaDeCervejas = cervejaService.listar();
        assertThat(listaDeCervejas, is(not(empty())));
        assertThat(listaDeCervejas.get(0), is(equalTo(cervejaProcuradaEsperadaDTO)));
    }
    @Test
    void quandoListaCervejaChamadoRetornaUmaListaDeCervejaVazia() {
        //When
        Mockito.when(cervejaRepository.findAll()).thenReturn(Collections.emptyList());
        //Then
        List<CervejaDTO> listaDeCervejas = cervejaService.listar();
        assertThat(listaDeCervejas, is(empty()));
    }

    @Test
    void quandoDeleteChamadoComIdValidoCervejaDeveSerExcluida() throws BebidaNaoExisteException {
        //Given
        CervejaDTO cervejaDeletadaEsperadaDTO = CervejaDTOBuilder.builder().build().getCervejaDTO();
        Cerveja cervejaDeletadaEsperada = cervejaMapper.toModel(cervejaDeletadaEsperadaDTO);
        //When
        Mockito.when(cervejaRepository.findById(cervejaDeletadaEsperadaDTO.getId())).thenReturn(Optional.of(cervejaDeletadaEsperada));
        Mockito.doNothing().when(cervejaRepository).deleteById(cervejaDeletadaEsperadaDTO.getId());
        //then
        cervejaService.deletaPorId(cervejaDeletadaEsperadaDTO.getId());

        Mockito.verify(cervejaRepository, Mockito.times(1)).findById(cervejaDeletadaEsperadaDTO.getId());
        Mockito.verify(cervejaRepository, Mockito.times(1)).deleteById(cervejaDeletadaEsperadaDTO.getId());
    }
}
