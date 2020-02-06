package com.beeline.booking.carorders.controller;

import com.beeline.booking.carorders.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * @author NIsaev on 16.12.2019
 */
@Controller
public class HomeController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/")
    public String root() {
        if (authenticationService.getAuthentication() == null)
            return "redirect:/auth-login";

        return "index";
    }

    @GetMapping("/auth-login")
    public String login() {
        if (authenticationService.getAuthentication() != null)
            return "redirect:/orders";

        return "auth-login";
    }

    @GetMapping("/login")
    public String login2() {
        return "redirect:/auth-login?logout";
    }
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/auth-login?logout";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "/error/access-denied";
    }


    @GetMapping("/errorring")
    public String errorTest() {
        throw new RuntimeException("test exception");
    }


    @ModelAttribute
    public void addAttributes(Model model) {
        String user = "";
        Authentication authentication = authenticationService.getAuthentication();
        if (authentication == null)
            return;

        user = authenticationService.currentUserNameSimple();

        model.addAttribute("curUser", user);
        model.addAttribute("curRole", authentication.getAuthorities());
    }

}
