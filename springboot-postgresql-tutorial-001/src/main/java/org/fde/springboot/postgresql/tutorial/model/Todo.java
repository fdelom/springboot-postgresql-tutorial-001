package org.fde.springboot.postgresql.tutorial.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.fde.springboot.postgresql.tutorial.model.extension.JsonbParameterizedType;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
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
@Table(name = "TODO")
@Getter
@Setter
@EqualsAndHashCode(exclude = { "id", "createdBy", "createdAt", "lastModifiedBy", "lastModifiedAt" })
@TypeDef(name = "TodoDetailsJsonb", typeClass = JsonbParameterizedType.class, parameters = {
		@Parameter(name = JsonbParameterizedType.CLASS, value = "org.fde.springboot.postgresql.tutorial.model.TodoDetails") })
public class Todo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "SERIAL PRIMARY KEY", unique = true, nullable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Type(type = "TodoDetailsJsonb")
	@Column
	private TodoDetails content;

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

	public Todo() {
	}

	public Todo(Long id, User user, TodoDetails content) {
		this.id = id;
		this.user = user;
		this.content = content;
	}
}
