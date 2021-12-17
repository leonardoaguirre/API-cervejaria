package com.digitalinovatioone.cervejaria.controller;

import com.digitalinovatioone.cervejaria.builder.CervejaDTOBuilder;
import com.digitalinovatioone.cervejaria.dto.CervejaDTO;
import com.digitalinovatioone.cervejaria.dto.response.QuantidadeDTO;
import com.digitalinovatioone.cervejaria.entity.Cerveja;
import com.digitalinovatioone.cervejaria.exception.BebidaNaoEncontradaException;
import com.digitalinovatioone.cervejaria.exception.BebidaNaoExisteException;
import com.digitalinovatioone.cervejaria.exception.EstoqueDeBebidaExcedidoException;
import com.digitalinovatioone.cervejaria.mapper.CervejaMapper;
import com.digitalinovatioone.cervejaria.service.CervejaService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.junit.JUnitTestRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;
import java.util.List;

import static com.digitalinovatioone.cervejaria.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CervejaControllerTest {
    private final String CERVEJA_API_URL = "/api/v1/cervejas";
    private final String CERVEJA_API_URL_SUBPATH_INCREMENT = "/incrementar";
    private final String CERVEJA_API_URL_SUBPATH_DECREMENT = "/decrementar";
    private final Long ID_CERVEJA_VALIDO = 1L;
    private final Long ID_CERVEJA_INVALIDO = 2L;

    private MockMvc mockMvc;

    @Mock
    private CervejaService cervejaService;

    @InjectMocks
    private CervejaController cervejaController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cervejaController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void quandoPOSTChamadoUmaCervejaDeveSerCriada() throws Exception {
        //given
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().getCervejaDTO();
        //when
        when(cervejaService.criaCerveja(cervejaDTO)).thenReturn(cervejaDTO);
        mockMvc.perform(post(CERVEJA_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(cervejaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is(cervejaDTO.getNome())))
                .andExpect(jsonPath("$.marca", is(cervejaDTO.getMarca())))
                .andExpect(jsonPath("$.tipoCerveja", is(cervejaDTO.getTipoCerveja().toString())));

    }

    @Test
    void quandoPOSTChamadoSemUmCampoObrigatorioUmErroDeveRetornar() throws Exception {
        //given
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().getCervejaDTO();
        cervejaDTO.setMarca(null);

        //then
        mockMvc.perform(post(CERVEJA_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(cervejaDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void quandoGETChamadoComNomeValidoEntaoStatusOKRetornado() throws Exception {
        //given
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().getCervejaDTO();
        //When
        when(cervejaService.procuraPorNome(cervejaDTO.getNome())).thenReturn(cervejaDTO);
        //then
        mockMvc.perform(get(CERVEJA_API_URL+"/"+cervejaDTO.getNome())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is(cervejaDTO.getNome())))
                .andExpect(jsonPath("$.marca", is(cervejaDTO.getMarca())))
                .andExpect(jsonPath("$.tipoCerveja", is(cervejaDTO.getTipoCerveja().toString())));
    }
    @Test
    void quandoGETChamadoComNomeInvalidoEntaoRetornaStatusNotFound() throws Exception {
        //given
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().getCervejaDTO();
        //When
        when(cervejaService.procuraPorNome(cervejaDTO.getNome())).thenThrow(BebidaNaoEncontradaException.class);
        //then
        mockMvc.perform(get(CERVEJA_API_URL+"/"+cervejaDTO.getNome())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    void quandoGETListChamadoEntaoStatusOKRetornado() throws Exception {
        //given
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().getCervejaDTO();
        //When
        when(cervejaService.listar()).thenReturn(Collections.singletonList(cervejaDTO));
        //then
        mockMvc.perform(get(CERVEJA_API_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome", is(cervejaDTO.getNome())))
                .andExpect(jsonPath("$[0].marca", is(cervejaDTO.getMarca())))
                .andExpect(jsonPath("$[0].tipoCerveja", is(cervejaDTO.getTipoCerveja().toString())));
    }
    @Test
    void quandoGETListSemCervejaEntaoStatusOKRetornado() throws Exception {
        //given
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().getCervejaDTO();
        //When
        when(cervejaService.listar()).thenReturn(Collections.singletonList(cervejaDTO));
        //then
        mockMvc.perform(get(CERVEJA_API_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void quandoDELETEChamadoComIdValidoEntaoRetornaStatusNoContent() throws Exception {
        //given
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().getCervejaDTO();
        //When
        doNothing().when(cervejaService).deletaPorId(cervejaDTO.getId());
        //then
        mockMvc.perform(delete(CERVEJA_API_URL+"/"+cervejaDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
    @Test
    void quandoDELETEChamadoComIdInvalidoEntaoRetornaStatusNoContent() throws Exception {
        //When
        doThrow(BebidaNaoExisteException.class).when(cervejaService).deletaPorId(ID_CERVEJA_INVALIDO);
        //then
        mockMvc.perform(delete(CERVEJA_API_URL+"/"+ID_CERVEJA_INVALIDO)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void quandoPATCHChamadoParaIncrementarEntaoRetornaStatusOK() throws Exception {
        QuantidadeDTO quantidadeDTO = QuantidadeDTO.builder().quantidade(15).build();

        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().getCervejaDTO();
        cervejaDTO.setQuantidade(cervejaDTO.getQuantidade()+quantidadeDTO.getQuantidade());

        when(cervejaService.incrementar(ID_CERVEJA_VALIDO,quantidadeDTO.getQuantidade())).thenReturn(cervejaDTO);

        mockMvc.perform(patch(CERVEJA_API_URL+"/"+ID_CERVEJA_VALIDO+CERVEJA_API_URL_SUBPATH_INCREMENT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantidadeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is(cervejaDTO.getNome())))
                .andExpect(jsonPath("$.marca", is(cervejaDTO.getMarca())))
                .andExpect(jsonPath("$.tipoCerveja", is(cervejaDTO.getTipoCerveja().toString())))
                .andExpect(jsonPath("$.quantidade", is(cervejaDTO.getQuantidade())));
    }
    @Test
    void quandoPATCHChamadoParaIncrementarMaiorQueoMaximoEntaoRetornaStatusBadRequestK() throws Exception {
        QuantidadeDTO quantidadeDTO = QuantidadeDTO.builder().quantidade(45).build();

        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().getCervejaDTO();
        cervejaDTO.setQuantidade(cervejaDTO.getQuantidade()+quantidadeDTO.getQuantidade());

        when(cervejaService.incrementar(ID_CERVEJA_VALIDO,quantidadeDTO.getQuantidade())).thenThrow(EstoqueDeBebidaExcedidoException.class);

        mockMvc.perform(patch(CERVEJA_API_URL+"/"+ID_CERVEJA_VALIDO+CERVEJA_API_URL_SUBPATH_INCREMENT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(quantidadeDTO)))
                .andExpect(status().isBadRequest())
        ;
    }
    @Test
    void quandoPATCHChamadoParaIncrementarComIdCervejaInvalidoEntaoRetornaStatusBadRequestK() throws Exception {
        QuantidadeDTO quantidadeDTO = QuantidadeDTO.builder().quantidade(15).build();

        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().getCervejaDTO();
        cervejaDTO.setQuantidade(cervejaDTO.getQuantidade()+quantidadeDTO.getQuantidade());

        when(cervejaService.incrementar(ID_CERVEJA_INVALIDO,quantidadeDTO.getQuantidade())).thenThrow(BebidaNaoExisteException.class);

        mockMvc.perform(patch(CERVEJA_API_URL+"/"+ID_CERVEJA_INVALIDO+CERVEJA_API_URL_SUBPATH_INCREMENT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(quantidadeDTO)))
                .andExpect(status().isNotFound())
        ;
    }
}
