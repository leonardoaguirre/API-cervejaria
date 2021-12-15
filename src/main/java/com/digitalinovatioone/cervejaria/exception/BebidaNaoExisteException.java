package com.digitalinovatioone.cervejaria.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BebidaNaoExisteException extends Exception{
    public BebidaNaoExisteException(Long id){
        super(String.format("Bebida com o id %s não está registrada no sistema!", id));
    }
}
