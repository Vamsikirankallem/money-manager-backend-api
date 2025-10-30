package com.vamsi.MoneyManagerApp.controller;

import com.vamsi.MoneyManagerApp.dto.FilterDTO;
import com.vamsi.MoneyManagerApp.service.ExpenseService;
import com.vamsi.MoneyManagerApp.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
@CrossOrigin("*")
public class FilterController {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<?> filterTransaction(@RequestBody FilterDTO filterDTO) {
        LocalDate startDate = filterDTO.getStartDate() != null ? filterDTO.getStartDate() : LocalDate.MIN;
        LocalDate endDate = filterDTO.getEndDate() != null ? filterDTO.getEndDate() : LocalDate.now();
        String keyword = filterDTO.getKeyword() != null ? filterDTO.getKeyword() : "";
        String sortField = filterDTO.getSortField() != null ? filterDTO.getSortField() : "date";
        Sort.Direction direction = "desc".equalsIgnoreCase(filterDTO.getSortOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);
        if ("income".equalsIgnoreCase(filterDTO.getType())) {
            return new ResponseEntity<>(incomeService.filterIncomes(startDate, endDate, keyword, sort), HttpStatus.OK);
        } else if ("expense".equalsIgnoreCase(filterDTO.getType())) {
            return new ResponseEntity<>(expenseService.filterExpenses(startDate, endDate, keyword, sort), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid Type! Must be either income or expense",HttpStatus.BAD_REQUEST);
        }
    }
}
