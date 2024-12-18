package salen.tasks.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import salen.tasks.entity.User;
import salen.tasks.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final UserService service;

    @PostMapping("/login")
    String login(@RequestBody User user) {
        return service.verify(user);
    }
}
