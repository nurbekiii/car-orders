package com.beeline.booking.carorders.restcontrol;

import com.beeline.booking.carorders.entity.User;
import com.beeline.booking.carorders.pojo.ADUserResp;
import com.beeline.booking.carorders.pojo.OauthTokenResponse2;
import com.beeline.booking.carorders.pojo.UserReg;
import com.beeline.booking.carorders.repo.UserRepository;
import com.beeline.booking.carorders.util.ADAuthorizer;
import org.apache.commons.net.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

/**
 * @author NIsaev on 13.12.2019
 */
@RestController
public class ADRestController {
    @Value("${auth.token.client.id}")
    private String CLIENT_ID;

    @Value("${auth.token.client.secret}")
    private String CLIENT_SECRET;

    @Value("${auth.token.grant.type}")
    private String GRANT_TYPE;

    @Value("${auth.token.generate.url}")
    private String AUTH_TOKEN_GENERATE_UR;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ADAuthorizer adAuthorizer;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/check_user", method = RequestMethod.POST)
    public OauthTokenResponse2 create(@RequestBody UserReg userReg) {

        ADUserResp resp = adAuthorizer.authenticateInAD(userReg);
        if (resp != null) {
            User usr = userRepository.getUserByUserName(userReg.getUsername());
            if (usr == null) {
                usr = new User();
                usr.setDateJoined(LocalDateTime.now());
                usr.setActive(true);
                usr.setStaff(true);
                usr.setSuperUser(false);
                usr.setUserName(userReg.getUsername());
                usr.setEmail(userReg.getUsername() + "@beeline.kg");

                usr.setFirstName(resp.getFirstname());
                usr.setLastName(resp.getLastname());
            }
            usr.setPassword(passwordEncoder.encode(userReg.getPassword()));
            usr.setLastLogin(LocalDateTime.now());

            userRepository.save(usr);
        }

        return getOauthToken(userReg);
    }

    private OauthTokenResponse2 getOauthToken(UserReg userReg) {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        // Basic Auth
        String plainCreds = CLIENT_ID + ":" + CLIENT_SECRET;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        headers.add("Authorization", "Basic " + base64Creds);
        // params
        map.add("username", userReg.getUsername());
        map.add("password", userReg.getPassword());
        map.add("grant_type", GRANT_TYPE);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map,
                headers);
        // CALLING TOKEN URL
        OauthTokenResponse2 res = null;
        try {
            res = restTemplate.postForObject(AUTH_TOKEN_GENERATE_UR, request, OauthTokenResponse2.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }
}

