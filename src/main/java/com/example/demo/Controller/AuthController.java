package com.example.demo.Controller;

import com.example.demo.Entity.Reminder;
import com.example.demo.Entity.User;
import com.example.demo.Service.AuthService;
import com.example.demo.Service.UserService;
import com.example.demo.dto.JwtRequest;
import com.example.demo.dto.RegistrationUserDto;
import com.example.demo.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }


    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        return authService.createNewUser(registrationUserDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id){
        if(!userService.deleteUserById(id)){
            throw new NotFoundException();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/allUsers")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") Long id){
        return userService.getUserById(id).orElseThrow(NotFoundException::new);
    }

    @GetMapping("/user/reminders")
    public List<Reminder> getUsersReminders(Principal principal){
        User user = userService.findByUsername(principal.getName()).orElseThrow(NotFoundException::new);
        return user.getReminders();
    }


}
