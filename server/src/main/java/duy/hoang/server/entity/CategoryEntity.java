package duy.hoang.server.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryEntity {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;
     private String name;
     @Column(updatable = false)
     @CreationTimestamp
     private LocalDateTime createdAt;
     @UpdateTimestamp
     private LocalDateTime updatedAt;

     private String icon;
     private String type;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "profileId", nullable = false)
     private ProfileEntity profile;
}
