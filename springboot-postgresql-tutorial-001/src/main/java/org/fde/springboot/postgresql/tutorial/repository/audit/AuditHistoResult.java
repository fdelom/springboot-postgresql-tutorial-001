/**
 * 
 */
package org.fde.springboot.postgresql.tutorial.repository.audit;

import org.fde.springboot.postgresql.tutorial.model.audit.DefaultRevisionEntity;
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
public class AuditHistoResult<Entity> {
	private final Entity entity;
	private final DefaultRevisionEntity revision;
	private final RevisionType type;
}
