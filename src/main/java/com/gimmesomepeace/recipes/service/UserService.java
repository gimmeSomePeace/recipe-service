package com.gimmesomepeace.recipes.service;

import com.gimmesomepeace.recipes.dto.request.UpdateUserRequest;
import com.gimmesomepeace.recipes.dto.response.UserResponse;
import com.gimmesomepeace.recipes.exception.LoginAlreadyExistsException;
import com.gimmesomepeace.recipes.exception.UserNotFoundException;
import com.gimmesomepeace.recipes.model.User;
import com.gimmesomepeace.recipes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getUserInfo(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (request.login() != null && !user.getLogin().equals(request.login())) {
            if (userRepository.existsByLogin(request.login())) {
                throw new LoginAlreadyExistsException(request.login());
            }
            user.setLogin(request.login());
        }
        if (request.name() != null) user.setName(request.name());

        userRepository.save(user);
        return UserResponse.from(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
    }
}
