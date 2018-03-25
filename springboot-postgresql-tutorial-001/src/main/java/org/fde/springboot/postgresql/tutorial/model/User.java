package org.fde.springboot.postgresql.tutorial.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author fdelom
 *
 */
@Entity
@Audited
@EntityListeners({ AuditingEntityListener.class })
@Table(name = "USERS")
@Getter
@Setter
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

	@CreatedBy
	@Column(name = "created_by")
	private String createdBy;

	@CreatedDate
	@Column(name = "created_date")
	private LocalDateTime createdAt;

	@LastModifiedBy
	@Column(name = "modified_by")
	private String lastModifiedBy;

	@LastModifiedDate
	@Column(name = "modified_date")
	private LocalDateTime lastModifiedAt;

	public User() {
	}

	public User(Long id, String first, String last, String pass) {
		this.id = id;
		this.firstName = first;
		this.lastName = last;
		this.password = pass;
	}

}
