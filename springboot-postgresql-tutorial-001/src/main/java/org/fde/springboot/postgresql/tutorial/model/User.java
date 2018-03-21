package org.fde.springboot.postgresql.tutorial.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author fdelom
 *
 */
@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = { "id" })
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "SERIAL PRIMARY KEY", unique = true, nullable = false)
	private Long id;

	@Column(name = "firstname", columnDefinition = "VARCHAR(64)", nullable = false)
	private String firstName;

	@Column(name = "lastname", columnDefinition = "VARCHAR(64)", nullable = false)
	private String lastName;

	@Column(name = "password", columnDefinition = "VARCHAR(64)", nullable = false)
	private String password;
}
