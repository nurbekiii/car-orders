package com.beeline.booking.carorders.util;

import com.beeline.booking.carorders.pojo.ADUserResp;
import com.beeline.booking.carorders.pojo.UserReg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @author NIsaev on 18.12.2019
 */

@Service
public class ADAuthorizer implements ADAuthorizable {
    private static final Logger logger = LoggerFactory.getLogger(ADAuthorizer.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${ad.auth.url}")
    private String adAuthUrl;

    @Override
    public ADUserResp authenticateInAD(UserReg userReg) {
        /*if(true){
            return new ADUserResp("Нурбек", "Исаев");
        }*/

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", userReg.getUsername());
        map.add("password", userReg.getPassword());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            ResponseEntity<ADUserResp> response = restTemplate.postForEntity(adAuthUrl, request, ADUserResp.class);
            ADUserResp resp = response.getBody();
            if (response.getStatusCode() == HttpStatus.OK)
                return resp;
        } catch (RestClientException e) {
            logger.error(e.toString());
        }
        return null;
    }
}
