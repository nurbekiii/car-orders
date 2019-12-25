package com.beeline.booking.carorders.component;

import org.springframework.security.core.Authentication;

/**
 * @author NIsaev on 22.12.2019
 */
public interface IAuthenticationFacade {
    Authentication getAuthentication();
}
