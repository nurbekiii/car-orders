package com.beeline.booking.carorders.service.impl;

import com.beeline.booking.carorders.entity.User;
import com.beeline.booking.carorders.repo.UserRepository;
import com.beeline.booking.carorders.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author NIsaev on 13.12.2019
 */
@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User usr = userRepository.getUserByUserName(userName);

        boolean isActive = usr.getActive();
        List role = Arrays.asList(new SimpleGrantedAuthority(usr.getSuperUser() != null ? "ROLE_ADMIN" : "ROLE_USER"));

        if (usr != null && isActive && usr.getUserName().equals(userName)) {
            return new org.springframework.security.core.userdetails.User(usr.getUserName(), usr.getPassword(), role); //getAuthority()
        }
        throw new UsernameNotFoundException("Invalid username or password.");
    }

    private List getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public List findAll() {
        List list = new ArrayList();
        userRepository.findAll().iterator().forEachRemaining(list::add);
        return list;
    }
}
