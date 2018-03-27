/**
 * 
 */
package org.fde.springboot.postgresql.tutorial.model;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.fde.springboot.postgresql.tutorial.exception.UserNotFoundException;
import org.fde.springboot.postgresql.tutorial.model.audit.AuditHistory;
import org.fde.springboot.postgresql.tutorial.repository.UserAuditHistoRepository;
import org.fde.springboot.postgresql.tutorial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

/**
 * @author fdelom
 *
 */
@Service
public class UserService {
	@Autowired
	private UserRepository repository;

	@Autowired
	private UserAuditHistoRepository repositoryHisto;

	public List<User> findAll() {
		return repository.findAll().stream().map(
				entity -> new User(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getPassword()))
				.collect(Collectors.toList());
	}

	@Transactional
	public User create(User newUser) {
		return repository.saveAndFlush(newUser);
	}

	@Transactional
	public User update(Long id, User user) throws UserNotFoundException {
		User entity = findOneSafe(id);
		if (Strings.isNullOrEmpty(user.getFirstName()) == false) {
			entity.setFirstName(user.getFirstName());
		}
		if (Strings.isNullOrEmpty(user.getLastName()) == false) {
			entity.setLastName(user.getLastName());
		}
		if (Strings.isNullOrEmpty(user.getPassword()) == false) {
			entity.setPassword(user.getPassword());
		}
		return entity;
	}

	@Transactional
	public void delete(Long id) throws UserNotFoundException {
		User user = findOneSafe(id);
		repository.delete(user);
	}

	private User findOneSafe(Long id) throws UserNotFoundException {
		User user = repository.findOne(id);
		if (user == null) {
			throw new UserNotFoundException();
		} else {
			return user;
		}
	}

	public List<AuditHistory<User>> findAuditRevisionsById(Long userId) {
		return repositoryHisto.findAuditRevisionsById(userId);
	}
}
