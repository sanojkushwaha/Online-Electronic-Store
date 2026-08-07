package com.webapp.onlineelectronicstore.dtos.response;

import com.webapp.onlineelectronicstore.enums.Role;
import com.webapp.onlineelectronicstore.validate.ImageNameValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String userId;

    @Size(min =4, max=25, message = "Invalid User name !!")
    private String name;

    //@Email(message = "Invalid user email !!")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
            , message = "Invalid user email !!")
    @NotBlank(message = "Email is required !!")
    private String email;

    @NotBlank(message = "Password is required !@!")
    private String password;

    @Size(min=4 , max=6, message = "Invalid gender !!")
    private String gender;

    @NotBlank(message = "Write something about yourself !!")
    private String about;

    @ImageNameValid
    private String imageName;

    private Role role;
}
