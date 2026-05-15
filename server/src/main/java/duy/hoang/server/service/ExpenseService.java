package duy.hoang.server.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import duy.hoang.server.dto.ExpenseDTO;
import duy.hoang.server.entity.CategoryEntity;
import duy.hoang.server.entity.ExpenseEntity;
import duy.hoang.server.entity.ProfileEntity;
import duy.hoang.server.repository.CategoryRepository;
import duy.hoang.server.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {
     private final ExpenseRepository expenseRepository;
     private final CategoryRepository categoryRepository;
     private final ProfileService profileService;

     public ExpenseDTO addExpense(ExpenseDTO expenseDTO) {
          ProfileEntity profile = profileService.getCurrentProfile();
          CategoryEntity category = categoryRepository.findById(expenseDTO.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));

          return toDTO(expenseRepository.save(toEntity(expenseDTO, profile, category)));
     }

     public List<ExpenseDTO> getCurrentMonthExpenses() {
          ProfileEntity profile = profileService.getCurrentProfile();
          LocalDate now = LocalDate.now();
          LocalDate startOfMonth = now.withDayOfMonth(1);
          LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());

          return expenseRepository.findByProfileIdAndDateBetween(profile.getId(), startOfMonth, endOfMonth)
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
     }

     public void deleteExpense(Long expenseId) {
          ProfileEntity profile = profileService.getCurrentProfile();
          ExpenseEntity expense = expenseRepository.findById(expenseId).orElseThrow(() -> new RuntimeException("Expense not found or not accessible"));
          if (expense.getProfile().getId() != profile.getId()) {
               throw new RuntimeException("Expense not found or not accessible");
          }
          expenseRepository.delete(expense);
     }

     public List<ExpenseDTO> getLastest5Expenses () {
          ProfileEntity profile = profileService.getCurrentProfile();
          return expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId())
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());     
     }

     public BigDecimal getTotalExpenseForCurrentUser() {
          ProfileEntity profile = profileService.getCurrentProfile();
          return expenseRepository.findTotalExpenseByProfileId(profile.getId());
     }

     public List<ExpenseDTO> filterExpenses(LocalDate startDate, LocalDate endDate, String keyWord, Sort sort) {
          ProfileEntity profile = profileService.getCurrentProfile();
          return expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate, keyWord, sort)
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
     }

     public List<ExpenseDTO> getExpensesForUserOnDate(LocalDate date) {
          ProfileEntity profile = profileService.getCurrentProfile();
          return expenseRepository.findByProfileIdAndDate(profile.getId(), date)
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
     }

     private ExpenseDTO toDTO(ExpenseEntity expense) {
          return ExpenseDTO.builder()
                    .id(expense.getId())
                    .name(expense.getName())
                    .icon(expense.getIcon())
                    .categoryName(expense.getCategory() != null ? expense.getCategory().getName() : null)
                    .categoryId(expense.getCategory() != null ? expense.getCategory().getId() : null)
                    .amount(expense.getAmount())
                    .date(expense.getDate())
                    .createdAt(expense.getCreatedAt())
                    .updatedAt(expense.getUpdatedAt())
                    .build();
     }

     private ExpenseEntity toEntity(ExpenseDTO expense, ProfileEntity profile, CategoryEntity category) {
          return ExpenseEntity.builder()
                    .id(expense.getId())
                    .name(expense.getName())
                    .icon(expense.getIcon())
                    .category(category)
                    .profile(profile)
                    .amount(expense.getAmount())
                    .date(expense.getDate())
                    .build();
     }
}
