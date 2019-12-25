package com.beeline.booking.carorders.util;

import com.beeline.booking.carorders.pojo.ADUserResp;
import com.beeline.booking.carorders.pojo.UserReg;

/**
 * @author NIsaev on 18.12.2019
 */
public interface ADAuthorizable {
    ADUserResp authenticateInAD(UserReg userReg);
}
