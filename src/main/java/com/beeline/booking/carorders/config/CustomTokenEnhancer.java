package com.beeline.booking.carorders.config;

import com.beeline.booking.carorders.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author NIsaev on 20.12.2019
 */
public class CustomTokenEnhancer implements TokenEnhancer {

    @Autowired
    private UserRepository userRepository;


    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<>();
        //System.out.println("Details:" + authentication.getUserAuthentication().toString());

        User user = (User) authentication.getUserAuthentication().getPrincipal();
        String userName = user.getUsername();
        com.beeline.booking.carorders.entity.User usr = userRepository.getUserByUserName(userName);

        additionalInfo.put("id", (usr != null ? usr.getId() : null));
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(
                additionalInfo);
        return accessToken;
    }
}
