package com.digitalinovatioone.cervejaria.controller;

import com.digitalinovatioone.cervejaria.builder.CervejaDTOBuilder;
import com.digitalinovatioone.cervejaria.dto.CervejaDTO;
import com.digitalinovatioone.cervejaria.exception.BebidaNaoEncontradaException;
import com.digitalinovatioone.cervejaria.service.CervejaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static com.digitalinovatioone.cervejaria.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CervejaControllerTest {
    private final String CERVEJA_API_URL = "/api/v1/cervejas";
    private final String CERVEJA_API_URL_SUBPATH_INCREMENT = "/increment";
    private final String CERVEJA_API_URL_SUBPATH_DECREMENT = "/decrement";
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
        when(cervejaController.buscarPorNome(cervejaDTO.getNome())).thenReturn(cervejaDTO);
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
}
