/**
 * 
 */
package org.fde.springboot.postgresql.tutorial.model.audit;

import org.hibernate.envers.RevisionType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author fdelom
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class AuditHistory<T> {
	private final T entity;
	private final Number revision;
	private final RevisionType revisionType;
}
