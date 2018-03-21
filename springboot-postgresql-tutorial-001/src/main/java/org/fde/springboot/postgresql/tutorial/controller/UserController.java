/**
 * 
 */
package org.fde.springboot.postgresql.tutorial.controller;

import java.util.List;

import javax.validation.Valid;

import org.fde.springboot.postgresql.tutorial.exception.UserNotFoundException;
import org.fde.springboot.postgresql.tutorial.model.User;
import org.fde.springboot.postgresql.tutorial.model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author fdelom
 *
 */
@RestController
@RequestMapping("/v1/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.FOUND)
	public ResponseEntity<List<User>> findAll() {
		return ResponseEntity.status(HttpStatus.FOUND).body(userService.findAll());
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<User> create(@Valid @RequestBody User user) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Task modified"),
			@ApiResponse(code = 304, message = "User not modified due to an error") })
	public ResponseEntity<User> update(@PathVariable Long id, @Valid @RequestBody User user) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(userService.update(id, user));
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "User deleted"),
			@ApiResponse(code = 400, message = "Request error") })
	public ResponseEntity<Long> delete(@PathVariable Long id) {
		try {
			userService.delete(id);
			return ResponseEntity.status(HttpStatus.OK).body(id);
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
}
