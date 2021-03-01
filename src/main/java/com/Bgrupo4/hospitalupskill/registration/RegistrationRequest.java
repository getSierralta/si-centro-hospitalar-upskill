package com.Bgrupo4.hospitalupskill.registration;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {
    private final Integer utente;
    private final String name;
    private final String username;
    private final String email;
    private final String password;
    /*
    private String morada;
    private String localidade;
    private int phone;
    private Date birthDay;
     */
}