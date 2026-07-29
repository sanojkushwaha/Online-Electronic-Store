package com.webapp.onlineelectronicstore.dtos.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseMassage {

    private String message;
    private boolean success;
    private HttpStatus status;

}
