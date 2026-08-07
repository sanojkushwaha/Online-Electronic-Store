package com.webapp.onlineelectronicstore.config;

import com.webapp.onlineelectronicstore.security.JwtAuthenticationEntryPoint;
import com.webapp.onlineelectronicstore.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Security configuration class.
 * Configures Spring Security, JWT Authentication,
 * Role-based Authorization and Global CORS.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Handles unauthorized access (401 Unauthorized).
     */
    private final JwtAuthenticationEntryPoint entryPoint;

    /**
     * Custom JWT filter that validates JWT token
     * before every protected request.
     */
    private final JwtAuthenticationFilter filter;

    private final String [] PUBLIC_URLS = {
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/v2/api-docs/**",
    };

    public SecurityConfig(JwtAuthenticationEntryPoint entryPoint,
                          JwtAuthenticationFilter filter) {
        this.entryPoint = entryPoint;
        this.filter = filter;
    }

    /**
     * Password encoder used to hash passwords
     * before storing them in the database.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Exposes AuthenticationManager as a Spring Bean.
     * Used during user login to authenticate username & password.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    /**
     * Main Spring Security configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http

                // Disable CSRF because JWT authentication is stateless.
                .csrf(csrf -> csrf.disable())

                // Enable Global CORS configuration.
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Do not create HTTP Session.
                // Every request must contain JWT Token.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Custom Authentication Entry Point
                // Executes when authentication fails.
                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(entryPoint))

                // Authorization Rules
                .authorizeHttpRequests(auth -> auth

                        // Authentication APIs are public.
                        .requestMatchers("/auth/**", "/auth/google-with-login")
                        .permitAll()

                        // User Registration API
                        .requestMatchers(HttpMethod.POST, "/users")
                        .permitAll()

                        // Public APIs
                        .requestMatchers(HttpMethod.GET,
                                "/products/**",
                                "/categories/**",
                                "/products/image/**",
                                "/users/image/**")
                        .permitAll()

                        .requestMatchers(PUBLIC_URLS).permitAll()

                        // ==========================
                        // ADMIN APIs
                        // ==========================

                        .requestMatchers(HttpMethod.POST,
                                "/products/**",
                                "/categories/**")
                        .hasAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.PUT,
                                "/products/**",
                                "/categories/**")
                        .hasAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.DELETE,
                                "/products/**",
                                "/categories/**")
                        .hasAuthority("ROLE_ADMIN")

                        // ==========================
                        // USER APIs
                        // ==========================

                        .requestMatchers("/orders/**")
                        .hasAuthority("ROLE_USER")

                        .requestMatchers("/carts/**")
                        .hasAuthority("ROLE_USER")

                        // Any remaining request requires authentication.
                        .anyRequest()
                        .authenticated()
                );

        /**
         * Register JWT Filter before UsernamePasswordAuthenticationFilter.
         *
         * Flow:
         * Client Request
         *      ↓
         * JwtAuthenticationFilter
         *      ↓
         * Validate JWT
         *      ↓
         * Set Authentication
         *      ↓
         * UsernamePasswordAuthenticationFilter
         *      ↓
         * Controller
         */
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Global CORS Configuration
     *
     * Allows frontend applications
     * (React, Angular, Vue, Mobile Apps)
     * to access backend APIs.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        // Allowed Frontend Origins
        configuration.setAllowedOriginPatterns(List.of("*"));
        // Allowed HTTP Methods
        configuration.setAllowedMethods(List.of("*"));
        // Allow all request headers
        configuration.setAllowedHeaders(List.of("*"));
        // Headers accessible to frontend
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));
        // Allow cookies and credentials
        configuration.setAllowCredentials(true);
        //max age of cache memory loading
        configuration.setMaxAge(4000L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Apply CORS configuration to all APIs
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}