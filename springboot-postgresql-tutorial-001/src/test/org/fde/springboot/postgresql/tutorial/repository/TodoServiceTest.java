package org.fde.springboot.postgresql.tutorial.repository;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fde.springboot.postgresql.tutorial.MainApp;
import org.fde.springboot.postgresql.tutorial.Utils.TestHelper;
import org.fde.springboot.postgresql.tutorial.exception.TodoNotFoundException;
import org.fde.springboot.postgresql.tutorial.model.Todo;
import org.fde.springboot.postgresql.tutorial.model.TodoDetails;
import org.fde.springboot.postgresql.tutorial.model.TodoService;
import org.fde.springboot.postgresql.tutorial.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author fdelom
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { MainApp.class }, properties = {
		"spring.datasource.url=jdbc:postgresql://localhost:5432/tutorialtestdb" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TodoServiceTest {
	@Autowired
	private TodoRepository repository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private TodoService todoService;

	@Before
	public void setUp() throws SQLException {
		repository.deleteAll();
		userRepository.deleteAll();

		TestHelper.resetAutoIncrementColumns(applicationContext, "users", "todo");

		User userJohn = new User(1L, "John", "Doe", "test");
		User userJane = new User(2L, "Jane", "Doe", "test");
		userRepository.saveAndFlush(userJohn);
		userRepository.saveAndFlush(userJane);

		Todo todoJohn = new Todo(1L, userJohn,
				new TodoDetails("John's Todo", new ArrayList<String>(Arrays.asList("tag1", "tag2"))));
		Todo todoJane = new Todo(2L, userJane,
				new TodoDetails("Jane's Todo", new ArrayList<String>(Arrays.asList("tag1", "tag3"))));
		repository.saveAndFlush(todoJohn);
		repository.saveAndFlush(todoJane);
	}

	@Test
	public void should_return_two_elm_when_findAll() {
		User userJohn = new User(1L, "John", "Doe", "test");
		User userJane = new User(2L, "Jane", "Doe", "test");

		List<Todo> result = todoService.findAll();
		assertThat(result.size(), equalTo(2));
		assertThat(result.get(0).getId(), equalTo(1L));
		assertThat(result.get(0).getUser(), equalTo(userJohn));
		assertThat(result.get(0).getContent(),
				equalTo(new TodoDetails("John's Todo", new ArrayList<String>(Arrays.asList("tag1", "tag2")))));

		assertThat(result.get(1).getId(), equalTo(2L));
		assertThat(result.get(1).getUser(), equalTo(userJane));
		assertThat(result.get(1).getContent(),
				equalTo(new TodoDetails("Jane's Todo", new ArrayList<String>(Arrays.asList("tag1", "tag3")))));
	}

	@Test
	public void should_create_and_return_new_elm() {
		Todo todo = new Todo();
		User userJohn = new User(1L, "John", "Doe", "test");
		TodoDetails details = new TodoDetails("Jane's Todo", new ArrayList<String>(Arrays.asList("tag1", "tag3")));
		todo.setUser(userJohn);
		todo.setContent(details);

		Todo result = todoService.create(todo);

		assertThat(todoService.findAll().size(), equalTo(3));
		assertThat(result.getId(), equalTo(3L));
		assertThat(result.getUser(), equalTo(userJohn));
		assertThat(result.getContent(), equalTo(details));
	}

	@Test
	public void should_update_user_and_return_elm() throws TodoNotFoundException {
		User userUpdate = new User(1L, "John", "Doe", "test");

		Todo todoUpdate = new Todo();
		todoUpdate.setUser(userUpdate);

		Todo result = todoService.update(2L, todoUpdate);
		assertThat(result.getId(), equalTo(2L));
		assertThat(result.getUser(), equalTo(userUpdate));
		assertThat(result.getContent(),
				equalTo(new TodoDetails("Jane's Todo", new ArrayList<String>(Arrays.asList("tag1", "tag3")))));
	}

	@Test(expected = TodoNotFoundException.class)
	public void should_update_throw_TodoNotFoundException_when_id_is_not_known() throws TodoNotFoundException {
		Todo todo = new Todo();
		todoService.update(42L, todo);
	}

	@Test
	public void should_delete_elm_with_id_given() throws TodoNotFoundException {
		todoService.delete(1L);
		assertThat(todoService.findAll().size(), equalTo(1));
	}

	@Test(expected = TodoNotFoundException.class)
	public void should_delete_throw_TodoNotFoundException_when_id_is_not_known() throws TodoNotFoundException {
		todoService.delete(42L);
	}

	@Test
	public void should_find_one_elm_by_content_description() {
		List<Todo> result = repository.findByContentDescription("John's Todo");
		User userJohn = new User(1L, "John", "Doe", "test");
		assertThat(result.size(), equalTo(1));
		assertThat(result.get(0).getId(), equalTo(1L));
		assertThat(result.get(0).getUser(), equalTo(userJohn));
		assertThat(result.get(0).getContent(),
				equalTo(new TodoDetails("John's Todo", new ArrayList<String>(Arrays.asList("tag1", "tag2")))));
	}

	@Test
	public void should_find_one_elm_by_content_tags() throws TodoNotFoundException {
		List<Todo> result = todoService.findByContentTagsIn(new ArrayList<String>(Arrays.asList("tag1", "tag2")));
		// List<Todo> result = repository.findByContentTagsIn(new
		// ArrayList<String>(Arrays.asList("tag1", "tag2")));
		User userJohn = new User(1L, "John", "Doe", "test");
		User userJane = new User(2L, "Jane", "Doe", "test");

		assertThat(result.size(), equalTo(2));
		assertThat(result.get(0).getId(), equalTo(1L));
		assertThat(result.get(0).getUser(), equalTo(userJohn));
		assertThat(result.get(0).getContent(),
				equalTo(new TodoDetails("John's Todo", new ArrayList<String>(Arrays.asList("tag1", "tag2")))));

		assertThat(result.get(1).getId(), equalTo(2L));
		assertThat(result.get(1).getUser(), equalTo(userJane));
		assertThat(result.get(1).getContent(),
				equalTo(new TodoDetails("Jane's Todo", new ArrayList<String>(Arrays.asList("tag1", "tag3")))));
	}

	@Test(expected = TodoNotFoundException.class)
	public void should_find_zero_elm_by_content_tags() throws TodoNotFoundException {
		todoService.findByContentTagsIn(new ArrayList<String>(Arrays.asList("tag42")));
	}
}
