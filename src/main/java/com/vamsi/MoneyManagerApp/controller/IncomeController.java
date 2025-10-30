package com.vamsi.MoneyManagerApp.controller;

import com.vamsi.MoneyManagerApp.dto.IncomeDTO;
import com.vamsi.MoneyManagerApp.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/incomes")
@RequiredArgsConstructor
@CrossOrigin("*")
public class IncomeController {

    private final IncomeService incomeService;

    @GetMapping
    public ResponseEntity<List<IncomeDTO>> getCurrentMonthIncomesOfCurrentUser(){
        return new ResponseEntity<>(incomeService.getCurrentMonthIncomesOfCurrentUser(),HttpStatus.OK);
    }

    @GetMapping("/topFive")
    public ResponseEntity<List<IncomeDTO>> getTop5IncomesOfCurrentUser(){
        return new ResponseEntity<>(incomeService.getTop5IncomesOfCurrentUser(),HttpStatus.FOUND);
    }

    @GetMapping("/totalIncome")
    public ResponseEntity<?> getTotalIncomeOfCurrentUser(){
        return new ResponseEntity<>(incomeService.getTotalIncomeOfCurrentUser(),HttpStatus.FOUND);
    }

    @PostMapping("/addIncome")
    public ResponseEntity<IncomeDTO> addIncome(@RequestBody IncomeDTO incomeDTO){
        return new ResponseEntity<>(incomeService.addIncome(incomeDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{incomeId}")
    public ResponseEntity<IncomeDTO> deleteIncomeOfCurrentUser(@PathVariable Long incomeId){
        return new ResponseEntity<>(incomeService.deleteIncomeOfCurrentUser(incomeId),HttpStatus.OK);
    }
}
