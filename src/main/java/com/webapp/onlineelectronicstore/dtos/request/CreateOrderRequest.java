package com.webapp.onlineelectronicstore.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {

    @NotBlank(message = "Billing name is required")
    @Size(min = 3, max = 100,
            message = "Billing name must be between 3 and 100 characters")
    private String billingName;

    @NotBlank(message = "Billing phone is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Enter a valid 10-digit mobile number"
    )
    private String billingPhone;

    @NotBlank(message = "Billing address is required")
    @Size(min = 10, max = 500,
            message = "Billing address must be between 10 and 500 characters")
    private String billingAddress;

    @NotNull(message = "Payment method is required")
    private String paymentMethod;
}