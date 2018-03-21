/**
 * 
 */
package org.fde.springboot.postgresql.tutorial.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.fde.springboot.postgresql.tutorial.MainApp;
import org.fde.springboot.postgresql.tutorial.model.User;
import org.fde.springboot.postgresql.tutorial.model.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author fdelom
 *
 */
@SpringBootTest(classes = { MainApp.class })
@WebMvcTest
public class UserControllerTest {
	@Mock
	private UserService userService;

	@InjectMocks
	private UserController userController;

	private MockMvc mockMvc;

	@Before
	public void setup() {

		// Process mock annotations
		MockitoAnnotations.initMocks(this);

		// Setup Spring test in standalone mode
		this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
	}

	@Test
	public void findall_should_return_found_status_and_2_results() throws Exception {
		ArrayList<User> findAllList = new ArrayList<>();
		User user = new User();
		user.setId(0L);
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setPassword("secret");
		findAllList.add(user);

		user = new User();
		user.setId(1L);
		user.setFirstName("Jane");
		user.setLastName("Doe");
		user.setPassword("password");
		findAllList.add(user);

		Mockito.when(userService.findAll()).thenReturn(findAllList);

		mockMvc.perform(get("/v1/user").accept(MediaType.APPLICATION_JSON)).andExpect(status().isFound())
				.andExpect(jsonPath("$.*", hasSize(2))).andExpect(jsonPath("$.[0].id", equalTo(0)))
				.andExpect(jsonPath("$.[0].firstName", equalTo("John")))
				.andExpect(jsonPath("$.[0].lastName", equalTo("Doe")))
				.andExpect(jsonPath("$.[0].password", equalTo("secret"))).andExpect(jsonPath("$.[1].id", equalTo(1)))
				.andExpect(jsonPath("$.[1].firstName", equalTo("Jane")))
				.andExpect(jsonPath("$.[1].lastName", equalTo("Doe")))
				.andExpect(jsonPath("$.[1].password", equalTo("password")));
	}

	@Test
	public void should_return_created_user() throws Exception {
		Gson gson = new GsonBuilder().create();

		User user = new User();
		user.setId(0L);
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setPassword("secret");

		Mockito.when(userService.create(Matchers.<User> any())).thenReturn(user);

		mockMvc.perform(post("/v1/user").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(user))).andExpect(status().isCreated()).andExpect(jsonPath("id", equalTo(0)))
				.andExpect(jsonPath("firstName", equalTo("John"))).andExpect(jsonPath("lastName", equalTo("Doe")))
				.andExpect(jsonPath("password", equalTo("secret")));
	}
}
