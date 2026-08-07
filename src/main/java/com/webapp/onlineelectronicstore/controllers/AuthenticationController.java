package com.webapp.onlineelectronicstore.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.webapp.onlineelectronicstore.dtos.request.GoogleLoginRequest;
import com.webapp.onlineelectronicstore.dtos.request.JwtRequest;
import com.webapp.onlineelectronicstore.dtos.response.JwtResponse;
import com.webapp.onlineelectronicstore.dtos.response.UserDto;
import com.webapp.onlineelectronicstore.entites.User;
import com.webapp.onlineelectronicstore.exceptions.BadApiRequest;
import com.webapp.onlineelectronicstore.exceptions.ResourceNotFoundException;
import com.webapp.onlineelectronicstore.repositories.UserRepository;
import com.webapp.onlineelectronicstore.security.CustomUserDetails;
import com.webapp.onlineelectronicstore.security.JwtHelper;
import com.webapp.onlineelectronicstore.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

   private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Value("${app.google.client_id}")
    private String googleClientId;

    @Value("${app.google_default_password}")
    private String googleProviderDefaultPassword;


    @PostMapping("/generate-token")
    public ResponseEntity<JwtResponse> login( @Valid @RequestBody JwtRequest jwtRequest) {

        Authentication authentication;

        try {

            authentication =
                    authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );

        } catch (BadCredentialsException ex) {

            throw new BadCredentialsException("Invalid Username or Password");
        }

        // Get logged-in user details
        CustomUserDetails customUserDetails =
                (CustomUserDetails) authentication.getPrincipal();

        // Generate JWT
        String token = jwtHelper.generateToken(customUserDetails);

        // Fetch User entity from database
        User user = userRepository.findByEmail(customUserDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .user(user)
                .build();

        return ResponseEntity.ok(response);
    }

    //handle login-with-google
    @PostMapping("/login-with-google")
    public ResponseEntity<JwtResponse> handleGooglelogin( @RequestBody GoogleLoginRequest request) throws GeneralSecurityException, IOException {

        logger.info("Token: {}", request.getToken());

        //Jwt Google verify
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                .Builder(new ApacheHttpTransport(), new GsonFactory())
                .setAudience(List.of(googleClientId))
                .build();

        GoogleIdToken googleIdToken = verifier.verify(request.getToken());

        if (googleIdToken != null) { //Token verified

            GoogleIdToken.Payload payload = googleIdToken.getPayload();

            //user identifier
            String username = payload.getSubject();

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            logger.info("userName: {}", username);
            logger.info("Name: {}", name);
            logger.info("Email: {}", email);
            logger.info("Picture Url: {}", pictureUrl);

            UserDto userDto = new UserDto();

            userDto.setName(name);
            userDto.setEmail(email);
            userDto.setImageName(pictureUrl);
            userDto.setPassword(googleProviderDefaultPassword);
            userDto.setAbout("User created by using google account");

            UserDto user;
            try {
                user = userService.findUserByEmail(userDto.getEmail());
                logger.info("User loaded from database");
            } catch (ResourceNotFoundException e) {
                logger.info("Creating new Google user");
                user = userService.createUser(userDto);
            }

            //authentication verify
            Authentication authentication =
                    authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(
                                    user.getEmail(),
                                    userDto.getPassword()
                            )
                    );

            //dot-->entity
           User user1 = modelMapper.map(user, User.class);

           String token = jwtHelper.generateToken(user1);


            JwtResponse response = JwtResponse.builder()
                    .jwtToken(token)
                    .user(user1)
                    .build();

            return ResponseEntity.ok(response);
        } else {
            logger.info("Invalid Google Id token");
            throw new BadApiRequest("Invalid Google user!");
        }
    }
}