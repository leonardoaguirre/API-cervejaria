package com.digitalinovatioone.cervejaria.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BebidaNaoEncontradaException extends Exception{
    public BebidaNaoEncontradaException(String nomeBebida){
        super(String.format("Bebida com o nome %s não foi encontrada no sistema!", nomeBebida));
    }
}
