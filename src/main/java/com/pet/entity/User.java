package com.pet.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@Column(unique = true, nullable = false)
	private String email;
	private String password;
	@Enumerated(EnumType.STRING)
	private Role role; // e.g., OWNER, VOLUNTEER, ADMIN
	private String phone;
	private String address;
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Pet> pets;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AdoptionRequest> adoptionRequests;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments;

	// Getters and Setters
	public enum Role {
		OWNER, VOLUNTEER, ADMIN
	}
}