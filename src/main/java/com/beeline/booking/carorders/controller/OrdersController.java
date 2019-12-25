package com.beeline.booking.carorders.controller;

import com.beeline.booking.carorders.entity.Order;
import com.beeline.booking.carorders.pojo.UserFilter;
import com.beeline.booking.carorders.repo.DriverRepository;
import com.beeline.booking.carorders.repo.OrderRepository;
import com.beeline.booking.carorders.repo.UserRepository;
import com.beeline.booking.carorders.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author NIsaev on 06.12.2019
 */
@Controller
public class OrdersController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverRepository driverRepository;

    //@Autowired
    //private OrderService orderService;

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/add-new-order")
    public String showSignUpForm(Order order, Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("drivers", driverRepository.findAll());

        return "add-order";
    }

    @PostMapping("/addorder")
    public String addOrder(@Valid Order order, BindingResult result, Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        if (result.hasErrors()) {
            return "add-order";
        }

        orderRepository.save(order);

        return fillOrders(model, page, size);
    }

    @GetMapping("/editorder/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
        model.addAttribute("order", order);
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("drivers", driverRepository.findAll());
        return "update-order";
    }

    @PostMapping("/updateorder/{id}")
    public String updateUser(@PathVariable("id") int id, @Valid Order order, BindingResult result, Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        if (result.hasErrors()) {
            order.setId(id);
            return "update-order";
        }

        orderRepository.save(order);

        return fillOrders(model, page, size);
    }

    @GetMapping("/deleteorder/{id}")
    public String deleteUser(@PathVariable("id") int id, Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
        orderRepository.delete(order);

        return fillOrders(model, page, size);
    }

    @PostMapping("/orders")
    public String getFilteredUsers(@Valid UserFilter filter, Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(20);

        String txtFilter = filter.getTextvalue();
        List<Order> orders = orderRepository.getAllByFilterText(txtFilter);

        Page<Order> orderPage = new  PageImpl<>(orders, PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id")), orders.size());

        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("drivers", driverRepository.findAll());

        model.addAttribute("orders", orderPage);
        return "orders-list";
    }

    @GetMapping("/orders")
    public String getAllOrders(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        if(authenticationService.getAuthentication() == null)
            return "redirect:/login";

        return fillOrders(model, page, size);
    }

    private String fillOrders(Model model, Optional<Integer> page, Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(20);

        Page<Order> orderPage = orderRepository.findAll(PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute("orders", orderPage);

        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("drivers", driverRepository.findAll());

        return "orders-list";
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