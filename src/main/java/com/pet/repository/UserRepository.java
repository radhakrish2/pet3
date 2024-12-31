package com.pet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pet.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	// Optional: Add custom query methods if needed, e.g., find by email
	User findByEmail(String email);
	List<User> findByRole(User.Role role); // Query to find users by their role
	boolean existsByEmail(String email);
}
