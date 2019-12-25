package com.beeline.booking.carorders.restcontrol;

import com.beeline.booking.carorders.entity.Driver;
import com.beeline.booking.carorders.entity.Order;
import com.beeline.booking.carorders.exceptions.OrderException;
import com.beeline.booking.carorders.exceptions.UserNotFoundException;
import com.beeline.booking.carorders.pojo.OrderRequest;
import com.beeline.booking.carorders.pojo.OrderResponse;
import com.beeline.booking.carorders.pojo.SmsResp;
import com.beeline.booking.carorders.pojo.SmsSendRequest;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    public Order createOrder(@Valid @RequestBody OrderRequest request, OAuth2Authentication authentication) throws UserNotFoundException, OrderException {
        int userId = getOauthUserId(authentication);
        if (userId <= 0) {
            throw new UserNotFoundException("User is not found by the access token");
        }

        List<Order> list = orderRepository.getAllByDriverTimePeriod(request.getDriverId(), request.getStartTime(), request.getEndTime());
        if (list != null && !list.isEmpty()) {
            //driver is busy at this time
            throw new OrderException("Order for this time period exists for this driver");
        }

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
            boolean resInform = informBySMS(request);
        }

        return res;
    }

    @DeleteMapping("/orders/{id}")
    public String delete(@PathVariable Integer id) {
        try {
            orderRepository.deleteById(id);
            return "OK";
        } catch (EmptyResultDataAccessException t) {
            return "FAIL";
        }
    }

    private Integer getOauthUserId(OAuth2Authentication authentication) {
        String userName = (String) authentication.getUserAuthentication().getPrincipal();

        com.beeline.booking.carorders.entity.User usr = userRepository.getUserByUserName(userName);
        if (usr != null)
            return usr.getId();

        return -1;
    }

    private boolean informBySMS(OrderRequest request) {
        String userPhone = request.getUserPhone();
        String driverMsisdn = driverRepository.getOne(request.getDriverId()).getPhone();

        Date date1 = new Date(request.getStartTime());
        Date date2 = new Date(request.getEndTime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        String dt1 = sdf.format(date1);
        String dt2 = sdf.format(date2);


        String smsToDriver = String.format("Поезка в %s-%s, %s-%s, заказчик %s", dt1, dt2, request.getStartPoint(), request.getEndPoint(), userPhone);
        String smsToUser = String.format("Поезка в %s-%s, %s-%s, водитель %s", dt1, dt2, request.getStartPoint(), request.getEndPoint(), driverMsisdn);
        //to driver
        SmsResp resp1 = sendSMS("996773905665"/*driverMsisdn*/, smsToDriver);
        //to user
        SmsResp resp2 = sendSMS("996773905665"/*userPhone*/, smsToUser);
        if (resp1 == null || resp2 == null)
            return false;

        if (resp1.getStatus().equalsIgnoreCase("success") || resp2.getStatus().equalsIgnoreCase("success"))
            return true;

        return false;
    }

    private SmsResp sendSMS(String msisdn, String smsText) {
        SmsSendRequest req1 = new SmsSendRequest(smsSendLogin, smsSendPsw, msisdn, smsText);
        return smsService.sendSms(req1);
    }
}
