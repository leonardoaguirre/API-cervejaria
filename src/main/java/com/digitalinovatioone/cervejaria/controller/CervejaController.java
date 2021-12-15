package com.digitalinovatioone.cervejaria.controller;

import com.digitalinovatioone.cervejaria.dto.CervejaDTO;
import com.digitalinovatioone.cervejaria.dto.response.ResponseMessage;
import com.digitalinovatioone.cervejaria.exception.BebidaJaRegistradaException;
import com.digitalinovatioone.cervejaria.exception.BebidaNaoEncontradaException;
import com.digitalinovatioone.cervejaria.exception.BebidaNaoExisteException;
import com.digitalinovatioone.cervejaria.service.CervejaService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cervejas")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CervejaController {
    private final CervejaService cervejaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseMessage criaCerveja(@RequestBody @Valid CervejaDTO cervejaDTO) throws BebidaJaRegistradaException {
        return cervejaService.criaCerveja(cervejaDTO);
    }

    @GetMapping("/{nome}")
    public  CervejaDTO buscarPorNome(@PathVariable String nome) throws BebidaNaoEncontradaException {
        return cervejaService.procuraPorNome(nome);
    }

    @GetMapping
    public List<CervejaDTO> listarCervejas(){
        return cervejaService.listar();
    }

    @PutMapping("/{id}")
    public ResponseMessage alterarCerveja(@PathVariable Long id, @RequestBody @Valid CervejaDTO cervejaDTO) throws BebidaNaoExisteException {
        return cervejaService.alterarPorId(id,cervejaDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarCerveja(@PathVariable Long id) throws BebidaNaoExisteException {
        cervejaService.deletaPorId(id);
    }

}
