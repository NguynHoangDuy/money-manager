
package duy.hoang.server.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import duy.hoang.server.dto.ExpenseDTO;
import duy.hoang.server.dto.IncomeDTO;
import duy.hoang.server.entity.CategoryEntity;
import duy.hoang.server.entity.ExpenseEntity;
import duy.hoang.server.entity.IncomeEntity;
import duy.hoang.server.entity.ProfileEntity;
import duy.hoang.server.repository.CategoryRepository;
import duy.hoang.server.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncomeService {
     private final CategoryRepository categoryRepository;
     private final IncomeRepository incomeRepository;
     private final ProfileService profileService;

     public IncomeDTO addIncome(IncomeDTO incomeDTO) {
          ProfileEntity profile = profileService.getCurrentProfile();
          CategoryEntity category = categoryRepository.findById(incomeDTO.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));

          return toDTO(incomeRepository.save(toEntity(incomeDTO, profile, category)));
     }

     public List<IncomeDTO> getCurrentMonthIncomes() {
          ProfileEntity profile = profileService.getCurrentProfile();
          LocalDate now = LocalDate.now();
          LocalDate startOfMonth = now.withDayOfMonth(1);
          LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());

          return incomeRepository.findByProfileIdAndDateBetween(profile.getId(), startOfMonth, endOfMonth)
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
     }

     public void deleteIncome(Long incomeId) {
          ProfileEntity profile = profileService.getCurrentProfile();
          IncomeEntity income = incomeRepository.findById(incomeId).orElseThrow(() -> new RuntimeException("Income not found or not accessible"));
          if (income.getProfile().getId() != profile.getId()) {
               throw new RuntimeException("Income not found or not accessible");
          }
          incomeRepository.delete(income);
     }

     public List<IncomeDTO> getLastest5Incomes () {
          ProfileEntity profile = profileService.getCurrentProfile();
          return incomeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId())
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());     
     }

     public List<IncomeDTO> filterIncomes(LocalDate startDate, LocalDate endDate, String keyWord, Sort sort) {
          ProfileEntity profile = profileService.getCurrentProfile();
          return incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate, keyWord, sort)
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
     }

     public BigDecimal getTotalIncomeForCurrentUser() {
          ProfileEntity profile = profileService.getCurrentProfile();
          return incomeRepository.findTotalIncomeByProfileId(profile.getId());
     }

     private IncomeDTO toDTO(IncomeEntity income) {
          return IncomeDTO.builder()
                    .id(income.getId())
                    .name(income.getName())
                    .icon(income.getIcon())
                    .categoryName(income.getCategory() != null ? income.getCategory().getName() : null)
                    .categoryId(income.getCategory() != null ? income.getCategory().getId() : null)
                    .amount(income.getAmount())
                    .date(income.getDate())
                    .createdAt(income.getCreatedAt())
                    .updatedAt(income.getUpdatedAt())
                    .build();
     }

     private IncomeEntity toEntity(IncomeDTO income, ProfileEntity profile, CategoryEntity category) {
          return IncomeEntity.builder()
                    .id(income.getId())
                    .name(income.getName())
                    .icon(income.getIcon())
                    .category(category)
                    .profile(profile)
                    .amount(income.getAmount())
                    .date(income.getDate())
                    .build();
     }
}
