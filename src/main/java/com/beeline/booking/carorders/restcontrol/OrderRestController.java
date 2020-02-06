package com.beeline.booking.carorders.restcontrol;

import com.beeline.booking.carorders.entity.Driver;
import com.beeline.booking.carorders.entity.Order;
import com.beeline.booking.carorders.entity.User;
import com.beeline.booking.carorders.exceptions.OrderException;
import com.beeline.booking.carorders.exceptions.UserNotFoundException;
import com.beeline.booking.carorders.pojo.OrderRequest;
import com.beeline.booking.carorders.pojo.OrderResponse;
import com.beeline.booking.carorders.repo.DriverRepository;
import com.beeline.booking.carorders.repo.OrderRepository;
import com.beeline.booking.carorders.repo.UserRepository;
import com.beeline.booking.carorders.service.SmsService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author NIsaev on 05.12.2019
 */

@RestController
@RequestMapping("/secured")
public class OrderRestController {
    @Autowired
    private SmsService smsService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverRepository driverRepository;


    @Value("${sms.send.login}")
    private String smsSendLogin;

    @Value("${sms.send.psw}")
    private String smsSendPsw;

    @GetMapping("/orders")
    public List<OrderResponse> getAllOrders(OAuth2Authentication authentication) {
        int userId = getOauthUserId(authentication);
        List<Order> list = orderRepository.getAllByUserId(userId);
        List<Driver> drivers = driverRepository.findAll();
        List<OrderResponse> listResult = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        for (Order order : list) {
            Driver driver = getDriver(drivers, order.getDriverId());
            JsonNode node = mapper.convertValue(driver, JsonNode.class);

            OrderResponse resp = new OrderResponse(order.getId(), order.getStartTime(), order.getEndTime(), order.getStartPoint(), order.getEndPoint(), order.getUserPhone(), order.getComment(), order.getUserId(), node);
            listResult.add(resp);
        }
        return listResult;
    }

    private Driver getDriver(List<Driver> drivers, int driverId) {
        return drivers.stream().filter(driver -> driver.getId().equals(driverId)).collect(Collectors.toList()).get(0);
    }


    @PostMapping("/create")
    public synchronized Order createOrder(@Valid @RequestBody OrderRequest request, OAuth2Authentication authentication) throws UserNotFoundException, OrderException {
        int userId = getOauthUserId(authentication);
        if (userId <= 0) {
            throw new UserNotFoundException("User is not found by the access token");
        }

        List<Order> list = orderRepository.getAllByDriverTimePeriod(request.getDriverId(), request.getStartTime(), request.getEndTime());
        if (list != null && !list.isEmpty()) {
            //driver is busy at this time
            throw new OrderException("Order for this time period exists for this driver");
        }

        User user = userRepository.getOne(userId);

        Order order = new Order();
        order.setDriverId(request.getDriverId());
        order.setStartTime(request.getStartTime());
        order.setEndTime(request.getEndTime());
        order.setStartPoint(request.getStartPoint());
        order.setEndPoint(request.getEndPoint());
        order.setComment(request.getComment());
        order.setUserPhone(request.getUserPhone());
        order.setUserId(userId);

        Order res = orderRepository.save(order);
        if (res != null) {
            boolean resInform = smsService.informBySMS(request, 1, user.getFirstName());
        }

        return res;
    }

    @DeleteMapping("/orders/{id}")
    public String delete(@PathVariable Integer id) {
        try {
            Order order = orderRepository.getOne(id);
            if (order != null) {
                User user = userRepository.getOne(order.getUserId());
                Driver driver = driverRepository.getOne(order.getDriverId());
                OrderRequest request = new OrderRequest(order.getId(), order.getDriverId(), order.getStartTime(), order.getEndTime(), order.getStartPoint(), order.getEndPoint(), order.getUserPhone(), order.getComment(), driver.getPhone());
                orderRepository.deleteById(id);

                smsService.informBySMS(request, 2, user.getFirstName());
                return "OK";
            }
        } catch (EmptyResultDataAccessException t) {
        }
        return "FAIL";
    }

    private Integer getOauthUserId(OAuth2Authentication authentication) {
        String userName = (String) authentication.getUserAuthentication().getPrincipal();

        com.beeline.booking.carorders.entity.User usr = userRepository.getUserByUserName(userName);
        if (usr != null)
            return usr.getId();

        return -1;
    }
}
