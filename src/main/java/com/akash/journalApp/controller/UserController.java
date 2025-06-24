package com.akash.journalApp.controller;

import com.akash.journalApp.entity.User;
import com.akash.journalApp.repository.UserRepository;
import com.akash.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User oldUser = userService.findByUserName(userName);

        if(oldUser != null) {
            oldUser.setPassword(user.getPassword());
            oldUser.setUsername(user.getUsername());
            userService.saveEntry(oldUser);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestBody User user) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        userRepository.deleteByUsername(userName);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
