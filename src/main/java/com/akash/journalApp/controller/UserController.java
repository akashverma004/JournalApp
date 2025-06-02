package com.akash.journalApp.controller;

import com.akash.journalApp.entity.JournalEntry;
import com.akash.journalApp.entity.User;
import com.akash.journalApp.service.JournalEntryService;
import com.akash.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            userService.saveEntry(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/username")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable String userName) {
        User oldUser = userService.findByUserName(userName);

        if(oldUser != null) {
            oldUser.setPassword(user.getPassword());
            oldUser.setUsername(user.getUsername());
            userService.saveEntry(oldUser);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
