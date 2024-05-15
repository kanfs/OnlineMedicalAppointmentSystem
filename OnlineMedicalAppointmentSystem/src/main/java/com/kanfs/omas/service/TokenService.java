package com.kanfs.omas.service;


public interface TokenService {
    public String getToken(int id, String password);

    int getIdByToken(String token);
}
