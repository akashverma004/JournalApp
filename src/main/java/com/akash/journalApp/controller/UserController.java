package com.akash.journalApp.controller;

import com.akash.journalApp.api.response.WeatherResponse;
import com.akash.journalApp.entity.User;
import com.akash.journalApp.repository.UserRepository;
import com.akash.journalApp.service.EmailService;
import com.akash.journalApp.service.UserService;
import com.akash.journalApp.service.WeatherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private EmailService emailService;

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User oldUser = userService.findByUserName(userName);

        if (oldUser != null) {
            oldUser.setPassword(user.getPassword());
            oldUser.setUsername(user.getUsername());
            userService.saveEntry(oldUser);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        userRepository.deleteByUsername(userName);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<?> greeting() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        WeatherResponse weather = weatherService.getWeather("New York");
        String greetingData = "";
        if(weather != null) {
          greetingData = "Weather of your city is " + weather.getCurrent().getFeelslike();
        }
        return new ResponseEntity<>("Hi " + userName + " " + greetingData , HttpStatus.OK);
    }

    @GetMapping("/send-mail")
    public ResponseEntity<?> sendEmail() {
        emailService.sendMail("vermaakash04012002@gmail.com", "Testing by Akash", "This is my first email.");

        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
