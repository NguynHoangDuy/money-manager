package duy.hoang.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import duy.hoang.server.dto.ExpenseDTO;
import duy.hoang.server.dto.IncomeDTO;
import duy.hoang.server.service.IncomeService;
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
@RequestMapping("/incomes")
public class IncomeController {

     private final IncomeService incomeService;

     @PostMapping
     public ResponseEntity<IncomeDTO> addIncome(@RequestBody IncomeDTO incomeDTO) {
          IncomeDTO entity = incomeService.addIncome(incomeDTO);
         
          return ResponseEntity.ok(entity);
     }

     @GetMapping
     public ResponseEntity<List<IncomeDTO>> getCurrentMonthIncomes() {
          return ResponseEntity.ok(incomeService.getCurrentMonthIncomes());
     }

     @DeleteMapping("/{id}")
     public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {
          incomeService.deleteIncome(id);
          return ResponseEntity.noContent().build();
     }
     
}
