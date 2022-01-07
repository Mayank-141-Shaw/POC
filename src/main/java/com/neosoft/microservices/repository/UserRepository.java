package com.neosoft.microservices.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.neosoft.microservices.bean.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	@Query("SELECT u FROM User u where u.firstname =:fname and u.deleted = false")
	List<User> findbyFirstname(String fname);
	
	@Query("SELECT u FROM User u where u.surname =:surname and u.deleted = false")
	List<User> findBySurname(String surname);

	@Query("SELECT u FROM User u where u.pincode =:pincode and u.deleted = false")
	List<User> findbyPincode(int pincode);
	
	@Query("SELECT u FROM User u Where u.deleted = false Order By u.dob, u.doj ASC")
	List<User> findAllSortByDobAndDoj();
	
}
