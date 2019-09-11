package com.accenture.huaweigroup.util;


import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.UUID;

@Component
public abstract class TokenGenerator {
    public static String generate(int userId) {
        String origin = "" + userId + UUID.randomUUID();
        String token = DigestUtils.md5DigestAsHex(origin.getBytes());
        return token;
    }


    public abstract String generate(String... strings);
}
