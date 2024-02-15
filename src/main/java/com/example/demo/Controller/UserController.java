package com.example.demo.Controller;

import com.example.demo.Entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
@RequiredArgsConstructor
public class UserController {



    @GetMapping("/unsecured")
    public String unsecuredData(){
        return "unsecuredData";
    }

    @GetMapping("/secured")
    public String securedData() {
        return "securedData";
    }

    @GetMapping("/admin")
    public String adminData(){
        return "adminData";
    }

    @GetMapping("/info")
    public String userData(Principal principal){
        return principal.getName();
    }








//    @GetMapping
//    public void getById(){
//
//    }
//
//    @PostMapping
//    public void createUser(@RequestBody User user){
//
//    }
}
