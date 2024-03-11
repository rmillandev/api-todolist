package com.app.todolist.util;

import lombok.Data;

import java.util.Date;

@Data
public class AuthResponseDTO {

    private String accessToken;

    private Date expirationToken;

    private String tokenType = "Bearer ";

    private int id;

    private String fullname;

    private long dni;

    public AuthResponseDTO(String accessToken, Date expirationToken, int id, String fullname, long dni){
        this.accessToken = accessToken;
        this.expirationToken = expirationToken;
        this.fullname = fullname;
        this.id = id;
        this.dni = dni;
    }

}
