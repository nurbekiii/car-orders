package com.beeline.booking.carorders.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * @author NIsaev on 22.12.2019
 */
public interface AuthenticationService {
    String currentUserNameSimple();

    Authentication getAuthentication();
}
