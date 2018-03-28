package org.fde.springboot.postgresql.tutorial.model;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.fde.springboot.postgresql.tutorial.exception.TodoNotFoundException;
import org.fde.springboot.postgresql.tutorial.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fdelom
 *
 */
@Service
public class TodoService {
	@Autowired
	private TodoRepository repository;

	@Autowired
	private EntityManager entityManager;

	public List<Todo> findAll() {
		return repository.findAll().stream()
				.map(entity -> new Todo(entity.getId(), entity.getUser(), entity.getContent()))
				.collect(Collectors.toList());
	}

	public List<Todo> findByContentDescription(String sDescription) {
		return repository.findByContentDescription(sDescription).stream()
				.map(entity -> new Todo(entity.getId(), entity.getUser(), entity.getContent()))
				.collect(Collectors.toList());
	}

	@Transactional
	public Todo create(Todo newTodo) {
		return repository.saveAndFlush(newTodo);
	}

	@Transactional
	public Todo update(Long id, Todo todo) throws TodoNotFoundException {
		Todo entity = findOneSafe(id);
		if (todo.getUser() != null) {
			entity.setUser(todo.getUser());
		}
		if (todo.getContent() != null) {
			entity.setContent(todo.getContent());
		}
		return entity;
	}

	@Transactional
	public void delete(Long id) throws TodoNotFoundException {
		Todo todo = findOneSafe(id);
		repository.delete(todo);
	}

	private Todo findOneSafe(Long id) throws TodoNotFoundException {
		Todo todo = repository.findOne(id);
		if (todo == null) {
			throw new TodoNotFoundException();
		} else {
			return todo;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Todo> findByContentTagsIn(List<String> tagsList) throws TodoNotFoundException {
		List<Todo> result = null;
		if (tagsList != null && tagsList.size() > 0) {
			String sQuery = "SELECT T.* FROM TODO T WHERE (T.content->'tags') \\?\\?| array[";
			for (int i = 0; i < tagsList.size(); ++i) {
				sQuery += "'" + tagsList.get(i) + "'";
				if (i < tagsList.size() - 1) {
					sQuery += ", ";
				}
			}
			sQuery += "]";
			result = (List<Todo>) entityManager.createNativeQuery(sQuery, Todo.class).getResultList();
		}
		if (result == null || result.size() == 0) {
			throw new TodoNotFoundException();
		} else {
			return result;
		}
	}
}
