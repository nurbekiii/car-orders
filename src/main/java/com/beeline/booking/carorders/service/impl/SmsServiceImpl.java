package com.beeline.booking.carorders.service.impl;

import com.beeline.booking.carorders.pojo.SmsResp;
import com.beeline.booking.carorders.pojo.SmsSendRequest;
import com.beeline.booking.carorders.service.SmsService;
import com.beeline.booking.carorders.util.HttpHeadersUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author NIsaev on 18.12.2019
 */

@Service
public class SmsServiceImpl implements SmsService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpHeadersUtil httpHeadersUtil;

    @Value("${sms.send.url}")
    private String smsSendUrl;

   @Override
    public SmsResp sendSms(SmsSendRequest request){
        HttpEntity<SmsSendRequest> requestEntity = new HttpEntity<>(request, httpHeadersUtil.getHttpHeadersJson());
        HttpEntity<SmsResp> response = restTemplate.exchange(smsSendUrl, HttpMethod.POST, requestEntity, SmsResp.class);

        return  response.getBody();
    }
}
