package com.beeline.booking.carorders.restcontrol;

import com.beeline.booking.carorders.entity.Driver;
import com.beeline.booking.carorders.pojo.Period;
import com.beeline.booking.carorders.repo.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author NIsaev on 07.12.2019
 */

@RestController
@RequestMapping("/secured")
public class DriverRestController {

    @Autowired
    private DriverRepository driverRepository;

    @PostMapping("/drivers")
    public List<Driver> checkFreeDrivers(@Valid @RequestBody Period period) {
        return driverRepository.findFreeDriversByPeriod(period.getStartTime(), period.getEndTime());
    }

}
