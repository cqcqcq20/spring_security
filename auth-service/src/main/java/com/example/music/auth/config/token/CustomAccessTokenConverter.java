package com.example.music.auth.config.token;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

import java.util.HashMap;
import java.util.Map;

public class CustomAccessTokenConverter extends DefaultAccessTokenConverter {

    @Override
    public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        Map<String, ?> stringMap = super.convertAccessToken(token, authentication);
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put("msg","success");
        objectMap.put("code",0);
        objectMap.put("payload",stringMap);
        return objectMap;
    }
}
