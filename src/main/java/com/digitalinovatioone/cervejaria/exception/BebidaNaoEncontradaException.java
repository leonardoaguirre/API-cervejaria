package com.digitalinovatioone.cervejaria.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BebidaNaoEncontradaException extends Exception{
    public BebidaNaoEncontradaException(String nomeBebida){
        super(String.format("Bebida com o nome %s não foi encontrada no sistema!", nomeBebida));
    }
}
