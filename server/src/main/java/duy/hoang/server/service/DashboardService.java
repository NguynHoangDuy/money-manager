package duy.hoang.server.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import duy.hoang.server.dto.ExpenseDTO;
import duy.hoang.server.dto.IncomeDTO;
import duy.hoang.server.dto.RecentTransactionDTO;
import duy.hoang.server.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {
     private final ExpenseService expenseService;
     private final IncomeService incomeService;
     private final ProfileService profileService;

     public Map<String, Object> getDashboardData() {
          ProfileEntity profile = profileService.getCurrentProfile();

          Map<String, Object> returnValue = new LinkedHashMap<>();

          List<IncomeDTO> latestIncomes = incomeService.getLastest5Incomes();
          List<ExpenseDTO> latestExpenses = expenseService.getLastest5Expenses();

          List<RecentTransactionDTO> recentTransactions = Stream.concat(
                    latestIncomes.stream()
                              .map(income -> RecentTransactionDTO.builder()
                                        .id(income.getId())
                                        .profileId(profile.getId())
                                        .icon(income.getIcon())
                                        .name(income.getName())
                                        .amount(income.getAmount())
                                        .date(income.getDate())
                                        .createdAt(income.getCreatedAt())
                                        .updatedAt(income.getUpdatedAt())
                                        .type("income")
                                        .build()),

                    latestExpenses.stream()
                              .map(expense -> RecentTransactionDTO.builder()
                                        .id(expense.getId())
                                        .profileId(profile.getId())
                                        .icon(expense.getIcon())
                                        .name(expense.getName())
                                        .amount(expense.getAmount())
                                        .date(expense.getDate())
                                        .createdAt(expense.getCreatedAt())
                                        .updatedAt(expense.getUpdatedAt())
                                        .type("expense")
                                        .build()))
                    .sorted((t1, t2) -> {
                         int cmp = t2.getDate().compareTo(t1.getDate());

                         if (cmp == 0
                                   && t1.getCreatedAt() != null
                                   && t2.getCreatedAt() != null) {

                              return t2.getCreatedAt()
                                        .compareTo(t1.getCreatedAt());
                         }

                         return cmp;
                    })
                    .collect(Collectors.toList());
          returnValue.put("totalBalance", incomeService.getTotalIncomeForCurrentUser()
                    .subtract(expenseService.getTotalExpenseForCurrentUser()));
          returnValue.put("totalIncome", incomeService.getTotalIncomeForCurrentUser());
          returnValue.put("totalExpense", expenseService.getTotalExpenseForCurrentUser());
          returnValue.put("recent5Expenses", latestExpenses);
          returnValue.put("recent5Incomes", latestIncomes);
          returnValue.put("recentTransactions", recentTransactions);
          return returnValue;
     }
}
