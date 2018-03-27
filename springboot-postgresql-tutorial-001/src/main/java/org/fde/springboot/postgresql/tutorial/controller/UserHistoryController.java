/**
 * 
 */
package org.fde.springboot.postgresql.tutorial.controller;

import java.util.List;

import org.fde.springboot.postgresql.tutorial.model.User;
import org.fde.springboot.postgresql.tutorial.model.UserService;
import org.fde.springboot.postgresql.tutorial.model.audit.AuditHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fdelom
 *
 */
@RestController
@RequestMapping("/v1/history/user")
public class UserHistoryController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.FOUND)
	public ResponseEntity<List<AuditHistory<User>>> findAuditRevisionsById(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.FOUND).body(userService.findAuditRevisionsById(id));
	}
}
