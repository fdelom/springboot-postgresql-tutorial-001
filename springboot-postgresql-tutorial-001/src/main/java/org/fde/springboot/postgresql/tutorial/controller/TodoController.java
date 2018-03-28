/**
 * 
 */
package org.fde.springboot.postgresql.tutorial.controller;

import java.util.List;

import javax.validation.Valid;

import org.fde.springboot.postgresql.tutorial.exception.TodoNotFoundException;
import org.fde.springboot.postgresql.tutorial.model.Todo;
import org.fde.springboot.postgresql.tutorial.model.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author fdelom
 *
 */
@RestController
@RequestMapping("/v1/todo")
public class TodoController {
	@Autowired
	private TodoService todoService;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.FOUND)
	public ResponseEntity<List<Todo>> findAll() {
		return ResponseEntity.status(HttpStatus.FOUND).body(todoService.findAll());
	}

	@RequestMapping(value = "/findByContentDescription", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.FOUND)
	public ResponseEntity<List<Todo>> findByContentDescription(@RequestParam String sDescription) {
		return ResponseEntity.status(HttpStatus.FOUND).body(todoService.findByContentDescription(sDescription));
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Todo> create(@Valid @RequestBody Todo todo) {
		return ResponseEntity.status(HttpStatus.CREATED).body(todoService.create(todo));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Todo modified"),
			@ApiResponse(code = 304, message = "Todo not modified due to an error") })
	public ResponseEntity<Todo> update(@PathVariable Long id, @Valid @RequestBody Todo todo) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(todoService.update(id, todo));
		} catch (TodoNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Todo deleted"),
			@ApiResponse(code = 400, message = "Request error") })
	public ResponseEntity<Long> delete(@PathVariable Long id) {
		try {
			todoService.delete(id);
			return ResponseEntity.status(HttpStatus.OK).body(id);
		} catch (TodoNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
}
