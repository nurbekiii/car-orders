package com.beeline.booking.carorders.restcontrol;

import com.beeline.booking.carorders.entity.User;
import com.beeline.booking.carorders.pojo.ADUserResp;
import com.beeline.booking.carorders.pojo.UserReg;
import com.beeline.booking.carorders.repo.UserRepository;
import com.beeline.booking.carorders.util.ADAuthorizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author NIsaev on 13.12.2019
 */
@RestController
@RequestMapping("/secured")
public class UserRestController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value="/user", method = RequestMethod.GET)
    public List listUser(){
        return userRepository.findAll();
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public User create(@RequestBody User user){
        return userRepository.save(user);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable(value = "id") int id){
        userRepository.deleteById(id);
        return "success";
    }
}
