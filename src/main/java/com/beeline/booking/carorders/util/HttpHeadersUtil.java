package com.beeline.booking.carorders.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author NIsaev on 18.12.2019
 */

@Service
public class HttpHeadersUtil {
    //@Value("${rest.jwt_token}")
    //private String jwtToken;

    public HttpHeaders getHttpHeadersJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        //headers.add("Authorization", "Bearer " + jwtToken);
        return headers;
    }

    public HttpHeaders getHttpHeadersMultiPart() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE);
        ///headers.add("Authorization", "Bearer " + jwtToken);

        return headers;
    }

    public HttpHeaders getHttpHeadersFormUrlEncoded() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        return headers;
    }
}
