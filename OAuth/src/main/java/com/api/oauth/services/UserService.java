package com.api.oauth.services;

import com.api.oauth.entities.User;
import com.api.oauth.feignclients.UserFeignClients;
import com.api.oauth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UserService {

    private static Logger logger = Logger.getLogger(UserService.class.getName());

    @Autowired
    private UserFeignClients userFeignClients;

    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email).get();
        if(user == null) {
            logger.warning("User not found");
            throw new IllegalArgumentException("User not found");
        }
        logger.info("Email found: " + user.getEmail());
        return user;
    }

}
