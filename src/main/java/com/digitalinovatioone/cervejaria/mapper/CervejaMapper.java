package com.digitalinovatioone.cervejaria.mapper;

import com.digitalinovatioone.cervejaria.dto.CervejaDTO;
import com.digitalinovatioone.cervejaria.entity.Cerveja;
import com.digitalinovatioone.cervejaria.enums.TipoCerveja;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Mapper
public interface CervejaMapper {
    CervejaMapper INSTANCE = Mappers.getMapper(CervejaMapper.class);

    Cerveja toModel(CervejaDTO cervejaDTO);

    CervejaDTO toDTO(Cerveja cerveja);
}
