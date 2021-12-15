package com.digitalinovatioone.cervejaria.builder;

import com.digitalinovatioone.cervejaria.dto.CervejaDTO;
import com.digitalinovatioone.cervejaria.enums.TipoCerveja;
import lombok.Builder;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
public class CervejaDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String nome = "Brahma";

    @Builder.Default
    private String marca = "Ambev";

    @Builder.Default
    private int max = 50;

    @Builder.Default
    private int quantidade = 10;

    @Builder.Default
    private TipoCerveja tipoCerveja = TipoCerveja.ALE;

    public CervejaDTO getCervejaDTO(){
        return new CervejaDTO(id,
                nome,
                marca,
                max,
                quantidade,
                tipoCerveja);
    }
}
