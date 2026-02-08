package com.gimmesomepeace.recipes.controller;

import com.gimmesomepeace.recipes.dto.request.UpdateUserRequest;
import com.gimmesomepeace.recipes.dto.response.UserResponse;
import com.gimmesomepeace.recipes.security.UserPrincipal;
import com.gimmesomepeace.recipes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/me")
    UserResponse getUserInfo(@AuthenticationPrincipal UserPrincipal principal) {
        return service.getUserInfo(principal.getId());
    }

    @PatchMapping("/me")
    UserResponse updateUserInfo(
            @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return service.updateUser(principal.getId(), request);
    }

    @DeleteMapping("/me")
    ResponseEntity<Void> delete(@AuthenticationPrincipal UserPrincipal principal) {
        service.deleteUser(principal.getId());
        return ResponseEntity.noContent().build();
    }
}
