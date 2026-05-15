package duy.hoang.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import duy.hoang.server.dto.ExpenseDTO;
import duy.hoang.server.service.ExpenseService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {
     private final ExpenseService expenseService;

     @PostMapping
     public ResponseEntity<ExpenseDTO> addExpense(@RequestBody ExpenseDTO data) {
          ExpenseDTO entity = expenseService.addExpense(data);

          return ResponseEntity.ok(entity);
     }

     @GetMapping
     public ResponseEntity<List<ExpenseDTO>> getCurrentMonthExpenses() {
          return ResponseEntity.ok(expenseService.getCurrentMonthExpenses());
     }

     @DeleteMapping("/{id}")
     public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
          expenseService.deleteExpense(id);
          return ResponseEntity.noContent().build();
     }

}
