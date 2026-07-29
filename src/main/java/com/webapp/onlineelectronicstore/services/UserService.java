package com.webapp.onlineelectronicstore.services;

import com.webapp.onlineelectronicstore.dtos.response.PageableResponse;
import com.webapp.onlineelectronicstore.dtos.response.UserDto;

public interface UserService {

    //create
    UserDto createUser(UserDto userdto);
    //update
    UserDto updateUser(UserDto userdto, String userId);
    //delete
    void deleteUser(String userId);
    //get all user
    PageableResponse<UserDto> findAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);
    //get single user with id
    UserDto findUserById(String userId);

    //get single user by email
    UserDto findUserByEmail(String email);
    //search
    UserDto searchUser(String keyword);


}
