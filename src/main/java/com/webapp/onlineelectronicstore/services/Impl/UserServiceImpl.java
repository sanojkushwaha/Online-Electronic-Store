package com.webapp.onlineelectronicstore.services.Impl;

import com.webapp.onlineelectronicstore.dtos.response.PageableResponse;
import com.webapp.onlineelectronicstore.dtos.response.UserDto;
import com.webapp.onlineelectronicstore.entites.User;
import com.webapp.onlineelectronicstore.exceptions.ResourceNotFoundException;
import com.webapp.onlineelectronicstore.helper.Helper;
import com.webapp.onlineelectronicstore.repositories.UserRepository;
import com.webapp.onlineelectronicstore.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;

    @Value("${user.profile.image.path}")
    private String imagePath;

    @Override
    public UserDto createUser(UserDto userdto) {
        //generate unique userId
        String userId = UUID.randomUUID().toString();
        userdto.setUserId(userId);
        //dto->entity
        User user1 =dtoToEntity(userdto);
        User savedUser = userRepository.save(user1);
        //entity->dto
        UserDto newuser =entityToDto(savedUser);

        return newuser;
    }


    @Override
    public UserDto updateUser(UserDto userdto, String userId) {
        User user1 = userRepository.findById(userId).orElseThrow(() ->new ResourceNotFoundException("User not found with given " + "id"));

        user1.setName(userdto.getName());
        //email update
        user1.setPassword(userdto.getPassword());
        user1.setGender(userdto.getGender());
        user1.setAbout(userdto.getAbout());
        user1.setImageName(userdto.getImageName());
        //save data
        User updatedUser = userRepository.save(user1);
        UserDto userDto = entityToDto(updatedUser);

        return userDto;
    }

    @Override
    public void deleteUser(String userId) {

        User user =userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found " + "with given" + " " + "id"));

        //delete user profile image
        // /images/user/xyz.png
        String fullPath = imagePath + user.getImageName();

        try {
            Path path = Paths.get(fullPath);
            Files.deleteIfExists(path);
        }catch (NoSuchFileException ex){
            logger.info("User Image Not Found in Such Folder");
            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }

        userRepository.delete(user);
    }

    @Override
    public PageableResponse<UserDto> findAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {

        //page number default start from '0'
        Sort sort= (sortDir.equalsIgnoreCase("desc")?(Sort.by(sortBy).descending()): (Sort.by(sortBy).ascending()));

        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<User> page = userRepository.findAll(pageable);

        PageableResponse<UserDto> pageableResponse = Helper.getPageableResponse(page,UserDto.class);
        return pageableResponse;
    }

    @Override
    public UserDto findUserById(String userId) {
        User user =userRepository.findById(userId).orElseThrow(() ->new ResourceNotFoundException("User not found with given " + "id"));
        return entityToDto(user);
    }

    @Override
    public UserDto findUserByEmail(String email) {
        User user =userRepository.findByEmail(email).orElseThrow(() ->new ResourceNotFoundException("User not found with given" + " " + "email"));
        return entityToDto(user);
    }

    @Override
    public UserDto searchUser(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDto> dtosList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return (UserDto) dtosList;
    }

    private UserDto entityToDto(User savedUser) {
//        UserDto user = UserDto.builder()
//                .userId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .gender(savedUser.getGender())
//                .about(savedUser.getAbout())
//                .imageName(savedUser.getImageName()).build();

        return mapper.map(savedUser, UserDto.class);

    }

    private User dtoToEntity(UserDto userdto) {
//        User userEntity = User.builder()
//                .userId(userdto.getUserId())
//                .name(userdto.getName())
//                .email(userdto.getEmail())
//                .password(userdto.getPassword())
//                .gender(userdto.getGender())
//                .about(userdto.getAbout())
//                .imageName(userdto.getImageName()).build();

        return mapper.map(userdto, User.class);

    }
}
