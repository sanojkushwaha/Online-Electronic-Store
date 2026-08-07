package com.webapp.onlineelectronicstore.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtHelper jwtHelper;

    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //It run before API: as Filter : Jwt token ko verify karne ke liye

        //Authorization : Bearer uhfddgfeiofekopk[fjofefeiho
        String requestHeader = request.getHeader("Authorization");
        logger.info("Header:{ } " + requestHeader);

        String username = null;
        String token = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            //sab thik hai:-> proceed next...
            token = requestHeader.substring(7);
            logger.info("Token:{ } " + token);

            //username
            try {
                username = jwtHelper.extractUsername(token);
                logger.info("Token Username:{ } " + username);
            }catch (IllegalArgumentException e) {
                logger.info("Illegal Argument while fetching JWT Token Username:{ } " + e.getMessage());
            }catch (ExpiredJwtException e) {
                logger.info("Expired JWT Token !!:{ } " + e.getMessage());
            }catch(MalformedJwtException e) {
                logger.info("Some change done in token !! Invalid token:{ } " + e.getMessage());
            }catch (Exception e) {
                e.printStackTrace();
            }

        }else {
            logger.info("Invalid Header!! ->Header is not starting with Bearer ");
        }

        //agar username null nahi hai and Authentication null hai , then
        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            if (jwtHelper.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request));

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                logger.info("Authorities = {}", userDetails.getAuthorities());
                logger.info("User authenticated successfully");
            }
        }

        filterChain.doFilter(request, response);
    }
}
