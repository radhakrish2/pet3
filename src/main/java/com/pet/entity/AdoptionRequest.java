package com.pet.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDate requestDate;
	@Enumerated(EnumType.STRING)
	private Status status; // e.g., PENDING, APPROVED, REJECTED
	private String remarks;
	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet pet;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	// Getters and Setters
	
	
	 @PrePersist
	    protected void onCreate() {
	        this.requestDate = LocalDate.now();
	    }
}
