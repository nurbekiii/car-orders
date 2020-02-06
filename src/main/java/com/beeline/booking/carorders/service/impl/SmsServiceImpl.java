package com.beeline.booking.carorders.service.impl;

import com.beeline.booking.carorders.entity.Driver;
import com.beeline.booking.carorders.pojo.OrderRequest;
import com.beeline.booking.carorders.pojo.SmsResp;
import com.beeline.booking.carorders.pojo.SmsSendRequest;
import com.beeline.booking.carorders.repo.DriverRepository;
import com.beeline.booking.carorders.repo.UserRepository;
import com.beeline.booking.carorders.service.SmsService;
import com.beeline.booking.carorders.util.HttpHeadersUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author NIsaev on 18.12.2019
 */

@Service
public class SmsServiceImpl implements SmsService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpHeadersUtil httpHeadersUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Value("${sms.send.url}")
    private String smsSendUrl;

    @Value("${sms.send.login}")
    private String smsSendLogin;

    @Value("${sms.send.psw}")
    private String smsSendPsw;

    @Override
    public SmsResp sendSms(SmsSendRequest request) {
        HttpEntity<SmsSendRequest> requestEntity = new HttpEntity<>(request, httpHeadersUtil.getHttpHeadersJson());
        HttpEntity<SmsResp> response = restTemplate.exchange(smsSendUrl, HttpMethod.POST, requestEntity, SmsResp.class);

        return response.getBody();
    }


    @Override
    public boolean informBySMS(OrderRequest request, int type, String userName) {
        String userPhone = request.getUserPhone();
        String driverMsisdn = request.getDriverPhone();

        String driverName = "";
        Driver driver = driverRepository.getDriverByPhone(driverMsisdn);
        if (driver != null) {
            driverName = driver.getName();
        }

        Date date1 = new Date(request.getStartTime());
        Date date2 = new Date(request.getEndTime());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy");

        String dt1 = sdf.format(date1);
        String dt2 = sdf.format(date2);
        String dt3 = sdf2.format(date2);

        String smsToDriver = "", smsToUser = "";

        //новая заявка
        if (type == 1) {
            smsToDriver = String.format("Заказ в %s %s-%s, %s-%s, заказчик %s %s", dt3, dt1, dt2, request.getStartPoint(), request.getEndPoint(), userName, userPhone);
            smsToUser = String.format("Заказ в %s %s-%s, %s-%s, водитель %s %s", dt3, dt1, dt2, request.getStartPoint(), request.getEndPoint(), driverName, driverMsisdn);
        } else if (type == 2) {
            //заявка - удаление
            smsToDriver = String.format("Отмена заказа в %s %s-%s, %s-%s, заказчик %s %s", dt3, dt1, dt2, request.getStartPoint(), request.getEndPoint(), userName, userPhone);
            smsToUser = String.format("Отмена заказа в %s %s-%s, %s-%s, водитель %s %s", dt3, dt1, dt2, request.getStartPoint(), request.getEndPoint(), driverName, driverMsisdn);
        } else if (type == 3) {
            //заявка - изменение
            smsToDriver = String.format("Изменение заказа в %s %s-%s, %s-%s, заказчик %s %s", dt3, dt1, dt2, request.getStartPoint(), request.getEndPoint(), userName, userPhone);
            smsToUser = String.format("Изменение заказа в %s %s-%s, %s-%s, водитель  %s %s", dt3, dt1, dt2, request.getStartPoint(), request.getEndPoint(), driverName, driverMsisdn);
        }

        //to driver
        SmsResp resp1 = send(driverMsisdn, smsToDriver);
        //to user
        SmsResp resp2 = send(userPhone, smsToUser);
        if (resp1 == null || resp2 == null)
            return false;

        if (resp1.getStatus().equalsIgnoreCase("success") || resp2.getStatus().equalsIgnoreCase("success"))
            return true;

        return false;
    }

    private SmsResp send(String msisdn, String smsText) {
        SmsSendRequest req1 = new SmsSendRequest(smsSendLogin, smsSendPsw, msisdn, smsText);
        return sendSms(req1);
    }

}
