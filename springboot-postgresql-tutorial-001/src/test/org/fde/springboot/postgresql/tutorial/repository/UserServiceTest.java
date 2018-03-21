/**
 * 
 */
package org.fde.springboot.postgresql.tutorial.repository;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.List;

import org.fde.springboot.postgresql.tutorial.MainApp;
import org.fde.springboot.postgresql.tutorial.Utils.TestHelper;
import org.fde.springboot.postgresql.tutorial.exception.UserNotFoundException;
import org.fde.springboot.postgresql.tutorial.model.User;
import org.fde.springboot.postgresql.tutorial.model.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { MainApp.class }, properties = {
		"spring.datasource.url=jdbc:postgresql://localhost:5432/tutorialtestdb" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceTest {
	@Autowired
	private UserRepository repository;

	@Autowired
	private TodoRepository todoRepository;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private UserService userService;

	@Before
	public void setUp() throws SQLException {
		todoRepository.deleteAll();
		repository.deleteAll();

		TestHelper.resetAutoIncrementColumns(applicationContext, "users");

		User userJohn = new User(1L, "John", "Doe", "test");
		User userJane = new User(2L, "Jane", "Doe", "test");
		repository.saveAndFlush(userJohn);
		repository.saveAndFlush(userJane);
	}

	@Test
	public void should_return_two_elm_when_findAll() {
		List<User> result = userService.findAll();
		assertThat(result.size(), equalTo(2));
		assertThat(result.get(0).getId(), equalTo(1L));
		assertThat(result.get(0).getFirstName(), equalTo("John"));
		assertThat(result.get(0).getLastName(), equalTo("Doe"));
		assertThat(result.get(0).getPassword(), equalTo("test"));

		assertThat(result.get(1).getId(), equalTo(2L));
		assertThat(result.get(1).getFirstName(), equalTo("Jane"));
		assertThat(result.get(1).getLastName(), equalTo("Doe"));
		assertThat(result.get(1).getPassword(), equalTo("test"));
	}

	@Test
	public void should_create_and_return_new_elm() {
		User user = new User();
		user.setFirstName("firstname");
		user.setLastName("lastname");
		user.setPassword("password");

		User result = userService.create(user);

		assertThat(userService.findAll().size(), equalTo(3));
		assertThat(result.getId(), equalTo(3L));
		assertThat(result.getFirstName(), equalTo("firstname"));
		assertThat(result.getLastName(), equalTo("lastname"));
		assertThat(result.getPassword(), equalTo("password"));
	}

	@Test
	public void should_update_firstname_and_return_elm() throws UserNotFoundException {
		User user = new User();
		user.setFirstName("FirstName_updated");
		User result = userService.update(2L, user);
		assertThat(result.getId(), equalTo(2L));
		assertThat(result.getFirstName(), equalTo("FirstName_updated"));
		assertThat(result.getLastName(), equalTo("Doe"));
		assertThat(result.getPassword(), equalTo("test"));
	}

	@Test(expected = UserNotFoundException.class)
	public void should_update_throw_UserNotFoundException_when_id_is_not_known() throws UserNotFoundException {
		User user = new User();
		userService.update(42L, user);
	}

	@Test
	public void should_delete_elm_with_id_given() throws UserNotFoundException {
		userService.delete(1L);
		assertThat(userService.findAll().size(), equalTo(1));
	}

	@Test(expected = UserNotFoundException.class)
	public void should_delete_throw_UserNotFoundException_when_id_is_not_known() throws UserNotFoundException {
		userService.delete(42L);
	}
}