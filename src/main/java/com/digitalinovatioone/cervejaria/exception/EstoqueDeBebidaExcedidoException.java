package com.digitalinovatioone.cervejaria.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EstoqueDeBebidaExcedidoException extends Exception{
    public EstoqueDeBebidaExcedidoException(Long id, int quantidade_a_incrementar){
        super(String.format("Estoque de bebida com o id %s foi excedido com a quantidade de %s!", id,quantidade_a_incrementar));
    }
}
