package com.beeline.booking.carorders.controller;

import com.beeline.booking.carorders.entity.User;
import com.beeline.booking.carorders.pojo.ADUserResp;
import com.beeline.booking.carorders.pojo.Param;
import com.beeline.booking.carorders.pojo.UserFilter;
import com.beeline.booking.carorders.pojo.UserReg;
import com.beeline.booking.carorders.repo.UserRepository;
import com.beeline.booking.carorders.service.AuthenticationService;
import com.beeline.booking.carorders.util.ADAuthorizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author NIsaev on 06.12.2019
 */
@Controller
public class UserController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private ADAuthorizer adAuthorizer;

    @Autowired
    HttpSession session;

    @GetMapping("/signup")
    public String showSignUpForm(User user) {
        return "add-user";
    }

    @PostMapping("/adduser")
    public String addUser(@Valid User user, BindingResult result, Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        if (result.hasErrors()) {
            return "add-user";
        }
        user.setDateJoined(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return fillUsers(model, page, size);
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "update-user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") int id, @Valid User user, BindingResult result, Model model,
                             @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        if (result.hasErrors()) {
            user.setId(id);
            return "update-user";
        }
        if (user.getDateJoined() == null)
            user.setDateJoined(LocalDateTime.now());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return fillUsers(model, page, size);
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        model.addAttribute("users", userRepository.findAll());
        return "users-list";
    }

    @PostMapping("/auth-login")
    public String loginUser(HttpServletRequest req, @Valid UserReg userReg, Model model, BindingResult result) throws Exception {
        Map<String, String> map = new HashMap<>();

        if (result.hasErrors()) {
            //map.put("error",);
            Param param = new Param(result.toString());
            model.addAttribute("param", param);
            return "auth-login";
        }

        ADUserResp resp = adAuthorizer.authenticateInAD(userReg);
        if (resp == null) {
            Param param = new Param("Invalid login or password");
            model.addAttribute("param", param);
            return "auth-login";
        }

        User usr = userRepository.getUserByUserName(userReg.getUsername());
        if (usr != null) {
            usr.setPassword(passwordEncoder.encode(userReg.getPassword()));
            usr.setFirstName(resp.getFirstname());
            usr.setLastName(resp.getLastname());
            usr.setLastLogin(LocalDateTime.now());
            userRepository.save(usr);


            UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(userReg.getUsername(), userReg.getPassword());
            Authentication auth = authManager.authenticate(authReq);

            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);
            //HttpSession session = req.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);
            session.setAttribute("username", userReg.getUsername());

            //globalUserObject(model);

            return "redirect:/orders";
        }

        Param param = new Param("Invalid login or password");
        model.addAttribute("param", param);
        return "auth-login";
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

    @PostMapping("/users")
    public String getFilteredUsers(@Valid UserFilter filter, Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(20);

        String txtFilter = filter.getTextvalue();
        List<User> users = userRepository.getAllByFilterText(txtFilter);

        Page<User> userPage = new PageImpl<>(users, PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id")), users.size());

        model.addAttribute("users", userPage);
        return "users-list";
    }

    @GetMapping("/users")
    public String getAllOrders(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        if (authenticationService.getAuthentication() == null)
            return "redirect:/auth-login";

        return fillUsers(model, page, size);
    }

    private String fillUsers(Model model, Optional<Integer> page, Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(20);
        Page<User> userPage = userRepository.findAll(PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute("users", userPage);

        return "users-list";
    }
}