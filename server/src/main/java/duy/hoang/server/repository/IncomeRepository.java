package duy.hoang.server.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import duy.hoang.server.entity.IncomeEntity;

public interface IncomeRepository extends JpaRepository<IncomeEntity, Long> {
     List<IncomeEntity> findByProfileIdOrderByDateDesc(Long profileId);
     List<IncomeEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);

     @Query("SELECT COALESCE(SUM(e.amount), 0) FROM IncomeEntity e WHERE e.profile.id = :profileId")
     BigDecimal findTotalIncomeByProfileId(@Param("profileId") Long profileId);
     
     List<IncomeEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(Long profileId, LocalDate startDate, LocalDate endDate, String keyWord, Sort sort);

     List<IncomeEntity> findByProfileIdAndDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);

}
