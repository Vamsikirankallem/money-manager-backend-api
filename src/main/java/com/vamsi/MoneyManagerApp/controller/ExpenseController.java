package com.vamsi.MoneyManagerApp.controller;

import com.vamsi.MoneyManagerApp.dto.ExpenseDTO;
import com.vamsi.MoneyManagerApp.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getCurrentMonthExpensesOfCurrentUser(){
        return new ResponseEntity<>(expenseService.getCurrentMonthExpensesOfCurrentUser(),HttpStatus.FOUND);
    }

    @GetMapping("/topFive")
    public ResponseEntity<List<ExpenseDTO>> getTop5ExpensesOfCurrentUser(){
        return new ResponseEntity<>(expenseService.get5LatestExpensesOfCurrentUser(),HttpStatus.FOUND);
    }

    @GetMapping("/totalExpense")
    public ResponseEntity<?> getTotalExpenseOfCurrentUser(){
        return new ResponseEntity<>(expenseService.getTotalExpenseOfCurrentUser(),HttpStatus.OK);
    }



    @PostMapping("addExpense")
    public ResponseEntity<ExpenseDTO> addExpense(@RequestBody ExpenseDTO expenseDTO){
        return new ResponseEntity<>(expenseService.addExpense(expenseDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("delete/{expenseId}")
    public ResponseEntity<ExpenseDTO> deleteExpenseOfCurrentUser(@PathVariable Long expenseId){
        return new ResponseEntity<>(expenseService.deleteExpense(expenseId),HttpStatus.OK);
    }
}
