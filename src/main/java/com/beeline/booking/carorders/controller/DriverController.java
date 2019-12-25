package com.beeline.booking.carorders.controller;

import com.beeline.booking.carorders.entity.Driver;
import com.beeline.booking.carorders.repo.DriverRepository;
import com.beeline.booking.carorders.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * @author NIsaev on 05.12.2019
 */

@Controller
public class DriverController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private DriverRepository driverRepository;

    @GetMapping("/add-new-driver")
    public String showAddForm(Driver driver) {
        return "add-driver";
    }

    @PostMapping("/add-new-driver")
    public String addUser(@Valid Driver driver, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-driver";
        }
        driverRepository.save(driver);
        model.addAttribute("drivers", driverRepository.findAll());
        return "drivers-list";
    }

    @GetMapping("/edit-driver/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Driver driver = driverRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid driver Id:" + id));
        model.addAttribute("driver", driver);
        return "update-driver";
    }

    @PostMapping("/updatedriver/{id}")
    public String updateUser(@PathVariable("id") int id, @Valid Driver driver, BindingResult result, Model model) {
        if (result.hasErrors()) {
            driver.setId(id);
            return "update-driver";
        }

        driverRepository.save(driver);
        model.addAttribute("drivers", driverRepository.findAll());
        return "drivers-list";
    }

    @GetMapping("/delete-driver/{id}")
    public String deleteUser(@PathVariable("id") int id, Model model) {
        Driver driver = driverRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid driver Id:" + id));
        driverRepository.delete(driver);
        model.addAttribute("drivers", driverRepository.findAll());
        return "drivers-list";
    }

    @GetMapping("/drivers")
    public String getAllUsers(Model model) {
        if(authenticationService.getAuthentication() == null)
            return "redirect:/login";

        model.addAttribute("drivers", driverRepository.findAll());
        return "drivers-list";
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        String user = "";
        Authentication authentication = authenticationService.getAuthentication();
        if(authentication == null)
            return;

        user = authenticationService.currentUserNameSimple();

        model.addAttribute("curUser", user);
        model.addAttribute("curRole", authentication.getAuthorities());
    }
}
