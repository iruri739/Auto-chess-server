package com.accenture.huaweigroup.util;

import org.springframework.stereotype.Component;

@Component
public interface TokenGenerator {

    public String generate(String[] strings);


}
