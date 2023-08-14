package com.coolSchool.CoolSchool.service;

import com.coolSchool.CoolSchool.models.dto.UserDTO;
import com.coolSchool.CoolSchool.models.dto.request.RegistrationRequest;
import com.coolSchool.CoolSchool.models.entity.User;
import com.coolSchool.CoolSchool.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(carton -> modelMapper.map(carton, UserDTO.class)).toList();
    }

    public UserDTO createUser(RegistrationRequest registrationRequest) {
        if (StringUtils.isBlank(registrationRequest.getName())) {
            throw new ValidationException("Name is required");
        }
        if (StringUtils.isBlank(registrationRequest.getUsername())) {
            throw new ValidationException("Username is required");
        }
        if (StringUtils.isBlank(registrationRequest.getLastname())) {
            throw new ValidationException("Size is required");
        }
        if (StringUtils.isBlank(registrationRequest.getLastname())) {
            throw new ValidationException("Size is required");
        }
        if (StringUtils.isBlank(registrationRequest.getEmail())) {
            throw new ValidationException("Email is required");
        }
        if (StringUtils.isBlank(registrationRequest.getPassword())) {
            throw new ValidationException("Password is required");
        }
        User userEntity = userRepository.save(modelMapper.map(registrationRequest, User.class));
        return modelMapper.map(userEntity, UserDTO.class);
    }
}
