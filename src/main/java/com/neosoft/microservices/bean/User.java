package com.neosoft.microservices.bean;

import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import java.util.Date;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "User")
@Builder
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id; 
	
	@NonNull
	@Column(name = "firstName")
	private String firstname;
	
	@NonNull
	@Column(name = "surname")
	private String surname;
	
	private String city;
	
	@Min(value = 0)@Max(value = 999999)
	@Column(name = "pincode")
	private int pincode;
	
	private float salary;
	
	@Column(name = "date_of_birth")
	@Temporal(TemporalType.DATE)
	private Date dob;
	
	@Column(name = "joining_date")
	@Temporal(TemporalType.DATE)
	private Date doj;
	
	private boolean deleted;
}
