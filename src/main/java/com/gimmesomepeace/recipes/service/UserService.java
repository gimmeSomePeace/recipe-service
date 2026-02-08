package com.gimmesomepeace.recipes.service;

import com.gimmesomepeace.recipes.dto.request.UpdateUserRequest;
import com.gimmesomepeace.recipes.dto.response.UserResponse;
import com.gimmesomepeace.recipes.exception.LoginAlreadyExistsException;
import com.gimmesomepeace.recipes.exception.UserNotFoundException;
import com.gimmesomepeace.recipes.model.User;
import com.gimmesomepeace.recipes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserResponse getUserInfo(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return UserResponse.from(user);
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (request.login() != null && !request.login().isBlank() && !user.getLogin().equals(request.login())) {
            if (userRepository.existsByLogin(request.login())) {
                throw new LoginAlreadyExistsException(request.login());
            }
            user.setLogin(request.login());
        }
        if (request.name() != null && !request.name().isBlank()) {
            user.setName(request.name());
        }
        userRepository.save(user);
        return UserResponse.from(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
    }
}
