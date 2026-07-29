package com.webapp.onlineelectronicstore.controllers;

import com.webapp.onlineelectronicstore.dtos.response.ApiResponseMassage;
import com.webapp.onlineelectronicstore.dtos.response.PageableResponse;
import com.webapp.onlineelectronicstore.dtos.response.ImageResponse;
import com.webapp.onlineelectronicstore.dtos.response.UserDto;
import com.webapp.onlineelectronicstore.services.FileService;
import com.webapp.onlineelectronicstore.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    //create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto user = userService.createUser(userDto);
        return new ResponseEntity<> (user, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable("userId") String userId,
            @Valid @RequestBody UserDto userDto) {
        UserDto userDto1 = userService.updateUser(userDto, userId);
        return new ResponseEntity<> (userDto1, HttpStatus.OK);

    }
    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMassage> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        ApiResponseMassage massage =
                ApiResponseMassage
                        .builder()
                        .message("User has been deleted " + "successfully !!")
                        .success(true)
                        .status(HttpStatus.OK)
                        .build();
        return new ResponseEntity<>(massage, HttpStatus.OK);
    }

    //get all User
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam(value = "pageNumber",defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "ASC", required = false) String sortDir ) {
        PageableResponse<UserDto> allUsers = userService.findAllUsers(pageNumber, pageSize, sortBy,sortDir);
        return new ResponseEntity<>(allUsers, HttpStatus.OK);

    }
    //get single user by userId
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable String userId){
        UserDto user = userService.findUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //get single user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
        return new ResponseEntity<>(userService.findUserByEmail(email),HttpStatus.OK);
    }

    //search user
    @GetMapping("/search/{keywords}")
    public ResponseEntity<UserDto> searchUser(@PathVariable String keywords){
        return new ResponseEntity<>(userService.searchUser(keywords),HttpStatus.OK);
    }

    // Upload Image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadImage(
            @RequestParam("uploadImage") MultipartFile image,
            @PathVariable String userId) throws IOException {

        // Upload image and get image name
        String imageName = fileService.uploadFile(image, imageUploadPath);

        // Get user
        UserDto user = userService.findUserById(userId);
        // Set image name
        user.setImageName(imageName);
        // Update user
        userService.updateUser(user, userId);

        // Create response
        ImageResponse imageResponse = ImageResponse.builder()
                .message("Image has been successfully uploaded")
                .imageName(imageName)
                .success(true)
                .status(HttpStatus.CREATED)
                .build();

        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    //serve userImage api
    @GetMapping("/image/{userId}")
    public void serveUserImage(
            @PathVariable String userId,
            HttpServletResponse response) throws IOException {

        UserDto user = userService.findUserById(userId);

        InputStream inputStream = fileService.getFile(imageUploadPath, user.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream, response.getOutputStream());
    }

}
