package com.beeline.booking.carorders.service;

import com.beeline.booking.carorders.pojo.SmsResp;
import com.beeline.booking.carorders.pojo.SmsSendRequest;

/**
 * @author NIsaev on 18.12.2019
 */
public interface SmsService {
  SmsResp sendSms(SmsSendRequest request);
}
