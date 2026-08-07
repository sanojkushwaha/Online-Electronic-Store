package com.webapp.onlineelectronicstore.dtos.response;

import com.webapp.onlineelectronicstore.entites.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {

    private String jwtToken;
    private User user;
}
