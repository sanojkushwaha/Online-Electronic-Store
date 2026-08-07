package com.webapp.onlineelectronicstore;

import com.webapp.onlineelectronicstore.entites.User;
import com.webapp.onlineelectronicstore.repositories.UserRepository;
import com.webapp.onlineelectronicstore.security.JwtHelper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

@SpringBootTest
class OnlineElectronicStoreApplicationTests {

    private Logger logger = LoggerFactory.getLogger(OnlineElectronicStoreApplicationTests.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtHelper jwtHelper;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldGenerateToken(){
        //generate token
        User user = userRepository.findByEmail("sanoj@gmail.com").get();
        String token = jwtHelper.generateToken(user);
        logger.info("Generated Token: {}",token);

        //extract username from token
        logger.info("UserName :{}", jwtHelper.extractUsername(token));

        //expiration of token
        logger.info("Token Expire at :{}", jwtHelper.extractExpiration(token));

        //check token validation
        logger.info("Token validation : {}", jwtHelper.validateToken(token,user));

    }
}
