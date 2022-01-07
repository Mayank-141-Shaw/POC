package com.neosoft.microservices;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neosoft.microservices.bean.User;
import com.neosoft.microservices.controller.InvalidRequestException;
import com.neosoft.microservices.controller.UserController;
import com.neosoft.microservices.repository.UserRepository;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
@WebMvcTest(value = UserController.class)
class Poc1ApplicationTests{
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@MockBean
	UserRepository repo;
	
	
	User USER_1 = new User(1l, "Mayank", "Shaw", "Kolkata", 700115, 50000f, new Date(1999-3-9), new Date(2021-8-11), false);
	User USER_2 = new User(2l, "John", "Lambert", "Mumbai", 890567, 50000f, new Date(1995-6-19), new Date(2017-10-11), false);
	User USER_3 = new User(3l, "Priya", "Yadav", "Indore", 112008, 50000f, new Date(1993-11-10), new Date(2020-6-15), false);
	User USER_4 = new User(4l, "Anjali", "Mahanti", "Ooty", 162008, 50000f, new Date(1993-11-10), new Date(2020-6-15), false);

	@Test
	void contextLoads() {
	}
	
	@Test
	public void getAllUsers_success() throws Exception {
		 List<User> records = new ArrayList<>(Arrays.asList(USER_1, USER_2, USER_3, USER_4));
		 
		 Mockito.when(repo.findAll()).thenReturn(records);
		 
		 mockMvc.perform(MockMvcRequestBuilders
	                .get("/users")
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().isOk()) //200
	                .andExpect(jsonPath("$", hasSize(4)))
	                .andExpect(jsonPath("$[1].firstname", is("John")));
		
	}
	
	@Test
	public void getUserById_success() throws Exception{
		Mockito.when(repo.findById(USER_1.getId()))
    	.thenReturn(Optional.of(USER_1));
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/users/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.firstname", is("Mayank")));
	}
	
	@Test
	public void getUserById_recordNotFound() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/users/20")
				.contentType(MediaType.ALL))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void getUserByFirstname_success() throws Exception{
		List<User> records = new ArrayList<>(Arrays.asList(USER_1));
		
		Mockito.when(repo.findbyFirstname(USER_1.getFirstname()))
			.thenReturn(records);
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/users/fname/Mayank")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$[0].firstname", is("Mayank")));
	}
	
	@Test
	public void getUserByLastname_success() throws Exception{
		List<User> records = new ArrayList<>(Arrays.asList(USER_1));
		
		Mockito.when(repo.findBySurname(USER_1.getSurname()))
			.thenReturn(records);
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/users/lname/Shaw")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$[0].surname", is("Shaw")));
	}
	
	@Test
	public void getUserByPincode_success() throws Exception{
		List<User> records = new ArrayList<>(Arrays.asList(USER_1));
		
		Mockito.when(repo.findbyPincode(USER_1.getPincode()))
			.thenReturn(records);
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/users/pincode/700115")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$[0].pincode", is(700115)));
	}
	
	
	@Test
	public void getUserSortedByDOB_success() throws Exception{
		List<User> records = new ArrayList<>(Arrays.asList(USER_3, USER_4, USER_2, USER_1));
		
		Mockito.when(repo.findAllSortByDobAndDoj()).thenReturn(records);
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/users/sortByDobAndDoj")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("[0].firstname", is("Priya")));
	}
	
	@Test
	public void createUser_success() throws Exception{
		User newUser = User.builder()
				.firstname("Jeffry")
				.surname("Alan")
				.city("Kenya")
				.pincode(701115)
				.salary(9000)
				.dob(new Date(1989-3-9))
				.doj(new Date(2020-10-11))
				.build();
		
		Mockito.when(repo.save(newUser)).thenReturn(newUser);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(newUser));
		
		mockMvc.perform(mockRequest)
		.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", notNullValue()))
			.andExpect(jsonPath("$.firstname", is("Jeffry")));
	}
	
	
	@Test
	public void createUser_emptyRequestBody() throws Exception{
		User newUser = null;
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(newUser));
		
		mockMvc.perform(mockRequest)
			.andDo(print())
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void updateUserRecord_success() throws Exception{
		User updatedUser = User.builder()
				.Id(1l)
				.firstname("Mayank")
				.surname("Shaw")
				.city("Sodepur")
				.pincode(700115)
				.salary(900000)
				.dob(new Date(1999-3-9))
				.doj(new Date(2021-10-11))
				.build();
		
		Mockito.when(repo.findById(USER_1.getId()))
        .thenReturn(Optional.of(USER_1));
        
        Mockito.when(repo.save(updatedUser)).thenReturn(updatedUser);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(updatedUser));
		
		mockMvc.perform(mockRequest)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", notNullValue()))
			.andExpect(jsonPath("$.city", is("Sodepur")))
			.andExpect(jsonPath("$.salary", is(900000.0)));
	}
	
	@Test
	public void updateUserRecord_nullId() throws Exception{
		User updatedUser = User.builder()
				.firstname("Kenan")
				.surname("Thompson")
				.city("Perth")
				.pincode(876342)
				.salary(90000)
				.dob(new Date(1990-3-9))
				.doj(new Date(2018-10-11))
				.build();

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedUser));

        mockMvc.perform(mockRequest)
        		.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                    assertTrue(result.getResolvedException() 
                    		instanceof InvalidRequestException))
                .andExpect(result ->
                	assertEquals("User record or id must not be null", 
            		result.getResolvedException().getMessage()));
	}
	
	
	@Test
    public void updateUserRecord_recordNotFound() throws Exception {
		User updatedUser = User.builder()
				.Id(7l)
				.firstname("Kenan")
				.surname("Thompson")
				.city("Perth")
				.pincode(876342)
				.salary(90000)
				.dob(new Date(1990-3-9))
				.doj(new Date(2018-10-11))
				.build(); 

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedUser));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                    assertTrue(result.getResolvedException() 
                    		instanceof InvalidRequestException))
                .andExpect(result ->
                	assertEquals("User with ID 7 not found.", 
                			result.getResolvedException().getMessage()));
    }
	
	
	
	@Test
    public void hardDeleteUserById_success() throws Exception {
		
		Mockito.when(repo.findById(USER_2.getId()))
        .thenReturn(Optional.of(USER_2));
		
        
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/users/2")
                .contentType(MediaType.APPLICATION_JSON))
        	.andDo(print())
            .andExpect(status().isOk());              
    }
	
	@Test
    public void hardDeleteUserById_notFound() throws Exception {       

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/users/20")
                .contentType(MediaType.APPLICATION_JSON))
        		.andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() 
                        		instanceof InvalidRequestException))
                .andExpect(result ->
                assertEquals("Invalid user Id. It does not exist.", 
                		result.getResolvedException().getMessage()));
    }
	
	
	@Test
	public void softDeleteUserById_success() throws Exception{
		
		// finding user
		Mockito.when(repo.findById(USER_3.getId()))
        .thenReturn(Optional.of(USER_3));
		
		// updating user status
		USER_3.setDeleted(true);
		
		// update
        Mockito.when(repo.save(USER_3)).thenReturn(USER_3);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/users/soft/3")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(USER_3));
		
		mockMvc.perform(mockRequest)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", notNullValue()))
			.andExpect(jsonPath("$.deleted", is(true)));
		
	}
	
	
	@Test
    public void softDeleteUserById_notFound() throws Exception {       

        mockMvc.perform(MockMvcRequestBuilders
                .put("/users/soft/25")
                .contentType(MediaType.APPLICATION_JSON))
        		.andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() 
                        		instanceof InvalidRequestException))
                .andExpect(result ->
                assertEquals("User with ID 25 not found.", 
                		result.getResolvedException().getMessage()));
    }
	
	@Test
	public void softDeleteUserById_nullId() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.put("/users/soft/"))
			.andDo(print())
			.andExpect(status().isMethodNotAllowed());
	}
	
	
	@Test
	public void unsoftDeleteUserById_success() throws Exception{
		
		// finding user
		Mockito.when(repo.findById(USER_3.getId()))
        .thenReturn(Optional.of(USER_3));
		
		// updating user status
		USER_3.setDeleted(false);
		
		// update
        Mockito.when(repo.save(USER_3)).thenReturn(USER_3);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/users/unsoft/3")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(USER_3));
		
		mockMvc.perform(mockRequest)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", notNullValue()))
			.andExpect(jsonPath("$.deleted", is(false)));
		
	}
	
	@Test
    public void unSoftDeleteUserById_notFound() throws Exception {       

        mockMvc.perform(MockMvcRequestBuilders
                .put("/users/unsoft/25")
                .contentType(MediaType.APPLICATION_JSON))
        		.andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() 
                        		instanceof InvalidRequestException))
                .andExpect(result ->
                assertEquals("User with ID 25 not found.", 
                		result.getResolvedException().getMessage()));
    }
}
