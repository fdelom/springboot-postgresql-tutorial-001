/**
 * 
 */
package org.fde.springboot.postgresql.tutorial.model.audit;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author fdelom
 *
 */
@Getter
@Setter
@EqualsAndHashCode
@MappedSuperclass
public class DefaultRevisionEntity implements Serializable {

	private static final long serialVersionUID = 5883271143267628117L;

	@Id
	@RevisionNumber
	@GeneratedValue
	@Column(name = "revision")
	private int revisionNumber;

	@RevisionTimestamp
	@Column(name = "revision_timestamp")
	private long revisionTimestamp;

	@Transient
	public LocalDateTime getRevisionDateTime() {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(revisionTimestamp), ZoneId.systemDefault());
	}
}
