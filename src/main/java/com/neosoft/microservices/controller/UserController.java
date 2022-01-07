package com.neosoft.microservices.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neosoft.microservices.bean.User;
import com.neosoft.microservices.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {
	
	
	@Autowired
	private UserRepository userRepo;
	
	
	@GetMapping()
	public List<User> getUsers(){
		return userRepo.findAll();
	}

	@GetMapping("/{id}")
	public Optional<User> getUserById(@PathVariable Long id) {
		if(!userRepo.findById(id).isPresent()) {
			throw new InvalidRequestException("Invalid user Id. User does not exist");
		}
		return userRepo.findById(id);
	}
	
	@GetMapping("/fname/{fname}")
	public List<User> getUsersByFirstname(@PathVariable("fname") String fname){
		return userRepo.findbyFirstname(fname);
	}
	
	
	@GetMapping("/lname/{lname}")
	public List<User> getUsersBySurname(@PathVariable("lname") String lname){
		return userRepo.findBySurname(lname);
	}
	
	
	@GetMapping("/pincode/{pin}")
	public List<User> getUsersByPincode(@PathVariable int pin){
		return userRepo.findbyPincode(pin);
	}
	
	@GetMapping("/sortByDobAndDoj")
	public List<User> getUsersSortedByDobAndDoj(){
		return userRepo.findAllSortByDobAndDoj();
	}
	
	
	
	
	
	
	@PostMapping()
	public User addNewUser(@RequestBody User newUser) {
		if(newUser == null) throw new InvalidRequestException("User record must not be null");
		return userRepo.save(newUser);
	}
	
	
	
	
	
	
	@PutMapping()
	public User updateUser(@RequestBody User newUser) {
		if (newUser == null || newUser.getId() == null) {
            throw new InvalidRequestException("User record or id must not be null");
        }
        Optional<User> optionalUser = userRepo
        		.findById(newUser.getId());
        if (!optionalUser.isPresent()) {
            throw new InvalidRequestException("User with ID "+newUser.getId()+" not found.");
        }
        User existingUser = optionalUser.get();

        existingUser.setFirstname(newUser.getFirstname());
        existingUser.setCity(newUser.getCity());
        existingUser.setSurname(newUser.getSurname());
        existingUser.setPincode(newUser.getPincode());
        existingUser.setSalary(newUser.getSalary());
        existingUser.setDeleted(newUser.isDeleted());
        existingUser.setDob(newUser.getDob());
        existingUser.setDoj(newUser.getDoj());
        
        return userRepo.save(existingUser);
	}
	
	
	
	
	
	
	@PutMapping("/soft/{id}")
	public User softDelete(@PathVariable("id") Long id) {
		if (id == null) {
            throw new InvalidRequestException("Id must not be null");
        }
        Optional<User> optionalUser = userRepo.findById(id);
        
        if (!optionalUser.isPresent()) {
            throw new InvalidRequestException("User with ID "+id+" not found.");
        }
        User existingUser = optionalUser.get();
        
        existingUser.setDeleted(true);
        
        return userRepo.save(existingUser);
	}
	
	@PutMapping("/unsoft/{id}")
	public User unSoftDelete(@PathVariable("id") Long id) {
		if (id == null) {
            throw new InvalidRequestException("Id must not be null");
        }
        Optional<User> optionalUser = userRepo.findById(id);
        
        if (!optionalUser.isPresent()) {
            throw new InvalidRequestException("User with ID "+id+" not found.");
        }
        User existingUser = optionalUser.get();
        
        existingUser.setDeleted(false);
        
        return userRepo.save(existingUser);
//        
//		return userRepo.findById(id)
//				.map(user -> {
//					user.setDeleted(false);
//					return userRepo.save(user);
//				}).orElseThrow(() -> new InvalidRequestException("Invalid user Id. User does not exist"));

	}
	
	@DeleteMapping("/{id}")
	public void hardDelete(@PathVariable("id") Long id) {
		if(!userRepo.findById(id).isPresent()) {
			throw new InvalidRequestException("Invalid user Id. It does not exist.");
		}
		userRepo.deleteById(id);
	}
	

}
