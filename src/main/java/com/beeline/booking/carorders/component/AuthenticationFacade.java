package com.beeline.booking.carorders.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * @author NIsaev on 22.12.2019
 */
@Component
public class AuthenticationFacade implements IAuthenticationFacade {

    @Autowired
    HttpSession session;

    @Override
    public Authentication getAuthentication() {
        //return SecurityContextHolder.getContext().getAuthentication();

        Object obj = session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        if (obj != null && obj instanceof SecurityContext) {
            return ((SecurityContext) obj).getAuthentication();
        }
        return null;
    }
}
