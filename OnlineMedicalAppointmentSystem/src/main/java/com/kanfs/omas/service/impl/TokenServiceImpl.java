package com.kanfs.omas.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.kanfs.omas.service.TokenService;
import com.kanfs.omas.pojo.Administer;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    @Override
    public String getToken(int id, String password) {
        String token = "";
        // 将管理员的id保存在token 以password作为token密钥
        token = JWT.create().withAudience(String.valueOf(id)).sign(Algorithm.HMAC256(password));
        return token;
    }

    @Override
    public int getIdByToken(String token) {
        int id = Integer.parseInt(JWT.decode(token).getAudience().get(0));
        return id;
    }
}
