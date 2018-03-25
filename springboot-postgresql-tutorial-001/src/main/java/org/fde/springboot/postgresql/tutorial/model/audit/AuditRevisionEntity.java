/**
 * 
 */
package org.fde.springboot.postgresql.tutorial.model.audit;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.RevisionEntity;

/**
 * @author fdelom
 *
 */
@Entity
@RevisionEntity
@Table(name = "REVISION")
public class AuditRevisionEntity extends DefaultRevisionEntity {

	private static final long serialVersionUID = -2679978097422629088L;

	public String toString() {
		return "AuditRevisionEntity --> revisionNumber = " + getRevisionNumber() + ", revisionDate = "
				+ this.getRevisionDateTime();
	}

}
