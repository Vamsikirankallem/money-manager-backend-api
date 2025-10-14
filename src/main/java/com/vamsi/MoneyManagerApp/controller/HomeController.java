package com.vamsi.MoneyManagerApp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HomeController {

    @GetMapping("/health")
    public ResponseEntity<?> checkHealth(){
        return new ResponseEntity<>("Application is Running", HttpStatus.OK);
    }

}
