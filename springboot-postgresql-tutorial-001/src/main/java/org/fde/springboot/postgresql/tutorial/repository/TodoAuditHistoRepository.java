/**
 * 
 */
package org.fde.springboot.postgresql.tutorial.repository;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.fde.springboot.postgresql.tutorial.model.Todo;
import org.fde.springboot.postgresql.tutorial.model.audit.AuditHistory;
import org.fde.springboot.postgresql.tutorial.repository.audit.AuditHistoryRepository;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fdelom
 *
 */
@Component
public class TodoAuditHistoRepository extends AuditHistoryRepository<Todo> {

	@Override
	@Transactional(readOnly = true)
	public List<AuditHistory<Todo>> findAuditRevisionsById(Long Id) {
		AuditReader auditReader = AuditReaderFactory.get(entityManager);

		AuditQuery auditQuery = auditReader.createQuery().forRevisionsOfEntity(Todo.class, false, true)
				.add(AuditEntity.id().eq(Id));

		return getConvertedAuditResultsList(auditQuery).stream().map(x -> getHistory(x)).collect(Collectors.toList());
	}

	@Override
	protected Class<Todo> getEntityType() {
		return Todo.class;
	}

	@Override
	public List<AuditHistory<Todo>> findAuditRevisionsByIdByLocalDateTime(Long Id, LocalDateTime timestamp) {
		AuditReader auditReader = AuditReaderFactory.get(entityManager);

		Date sqlTimestamp = new Date(timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		AuditQuery auditQuery = auditReader.createQuery().forRevisionsOfEntity(Todo.class, false, true)
				.add(AuditEntity.id().eq(Id))
				.add(AuditEntity.revisionNumber().eq(auditReader.getRevisionNumberForDate(sqlTimestamp)));

		return getConvertedAuditResultsList(auditQuery).stream().map(x -> getHistory(x)).collect(Collectors.toList());
	}
}
