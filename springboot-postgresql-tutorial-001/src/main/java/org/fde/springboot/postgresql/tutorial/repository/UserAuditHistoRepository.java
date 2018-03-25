package org.fde.springboot.postgresql.tutorial.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.fde.springboot.postgresql.tutorial.model.User;
import org.fde.springboot.postgresql.tutorial.model.audit.AuditHistory;
import org.fde.springboot.postgresql.tutorial.repository.audit.AuditHistoryRepository;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserAuditHistoRepository extends AuditHistoryRepository<User> {

	@Override
	@Transactional(readOnly = true)
	public List<AuditHistory<User>> listAuditRevisions(Long Id) {
		AuditReader auditReader = AuditReaderFactory.get(entityManager);

		AuditQuery auditQuery = auditReader.createQuery().forRevisionsOfEntity(User.class, false, true)
				.add(AuditEntity.id().eq(Id));

		return getConvertedAuditResultsList(auditQuery).stream().map(x -> getHistory(x)).collect(Collectors.toList());
	}

	@Override
	protected Class<User> getEntityType() {
		return User.class;
	}
}
