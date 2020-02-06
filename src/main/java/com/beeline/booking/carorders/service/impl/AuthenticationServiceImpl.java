package com.beeline.booking.carorders.service.impl;

import com.beeline.booking.carorders.component.IAuthenticationFacade;
import com.beeline.booking.carorders.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * @author NIsaev on 22.12.2019
 */

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    HttpSession session;

    @Override
    public String currentUserNameSimple() {

        Authentication authentication = authenticationFacade.getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }

        String userName = (String) session.getAttribute("username");
        return userName;
    }

    @Override
    public Authentication getAuthentication() {
        return authenticationFacade.getAuthentication();
    }
}
