package com.vamsi.MoneyManagerApp.controller;

import com.vamsi.MoneyManagerApp.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
@CrossOrigin("*")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    private ResponseEntity<Map<String, Object>> getDashboardData(){
        return new ResponseEntity<>(dashboardService.getDashboardData(), HttpStatus.OK);
    }
}
