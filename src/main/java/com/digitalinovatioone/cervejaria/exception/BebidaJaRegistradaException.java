package com.digitalinovatioone.cervejaria.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BebidaJaRegistradaException extends Exception{
    public BebidaJaRegistradaException (String nomeBebida){
        super(String.format("Bebida com o nome %s já está registrada no sistema!", nomeBebida));
    }
}
