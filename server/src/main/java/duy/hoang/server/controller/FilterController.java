package duy.hoang.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import duy.hoang.server.dto.FilterDTO;
import duy.hoang.server.service.ExpenseService;
import duy.hoang.server.service.IncomeService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class FilterController {
     private final ExpenseService expenseService;
     private final IncomeService incomeService;

     @PostMapping
     public ResponseEntity<?> filterTransactions(@RequestBody FilterDTO filter) {
          LocalDate startDate = filter.getStartDate() != null ? LocalDate.parse(filter.getStartDate()) : LocalDate.MIN;
          LocalDate endDate = filter.getEndDate() != null ? LocalDate.parse(filter.getEndDate()) : LocalDate.now();

          String keyWord = filter.getKeyWord() != null ? filter.getKeyWord() : "";
          String sortField = filter.getSortField() != null ? filter.getSortField() : "date";
          Sort.Direction direction = "desc".equalsIgnoreCase(filter.getSortDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC;

          Sort sort = Sort.by(direction, sortField);

          if ("income".equalsIgnoreCase(filter.getType())) {
               return ResponseEntity.ok(incomeService.filterIncomes(startDate, endDate, keyWord, sort));
          } else if ("expense".equalsIgnoreCase(filter.getType())) {
               return ResponseEntity.ok(expenseService.filterExpenses(startDate, endDate, keyWord, sort));
          } else {
               return ResponseEntity.badRequest().body("Invalid type. Must be 'income' or 'expense'.");
          }
         
     }
     
}

