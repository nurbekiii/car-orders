package com.beeline.booking.carorders.service;

import org.springframework.security.core.Authentication;

/**
 * @author NIsaev on 22.12.2019
 */
public interface AuthenticationService {
    String currentUserNameSimple();

    Authentication getAuthentication();
}
