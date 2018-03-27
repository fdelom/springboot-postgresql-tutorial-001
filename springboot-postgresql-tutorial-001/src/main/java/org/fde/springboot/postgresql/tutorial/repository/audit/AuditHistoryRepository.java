package org.fde.springboot.postgresql.tutorial.repository.audit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.fde.springboot.postgresql.tutorial.model.audit.AuditHistory;
import org.fde.springboot.postgresql.tutorial.model.audit.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditQuery;

public abstract class AuditHistoryRepository<T> {

	@PersistenceContext
	protected EntityManager entityManager;

	protected abstract Class<T> getEntityType();

	public abstract List<AuditHistory<T>> findAuditRevisionsById(Long Id);

	public abstract List<AuditHistory<T>> findAuditRevisionsByIdByLocalDateTime(Long Id, LocalDateTime timestamp);

	protected List<AuditHistoResult<T>> getConvertedAuditResultsList(AuditQuery auditQuery) {
		List<?> results = auditQuery.getResultList();

		if (results == null) {
			return new ArrayList<AuditHistoResult<T>>();
		}

		return results.stream().filter(x -> x instanceof Object[]).map(x -> (Object[]) x)
				.map(x -> getAuditHistoResult(x)).collect(Collectors.toList());
	}

	public AuditHistoResult<T> getAuditHistoResult(Object[] item) {
		if (item == null) {
			return null;
		}

		if (item.length < 3) {
			return null;
		}

		T entity = null;
		if (getEntityType().isInstance(item[0])) {
			entity = getEntityType().cast(item[0]);
		}

		DefaultRevisionEntity revision = null;
		if (item[1] instanceof DefaultRevisionEntity) {
			revision = (DefaultRevisionEntity) item[1];
		}

		RevisionType revisionType = null;
		if (item[2] instanceof RevisionType) {
			revisionType = (RevisionType) item[2];
		}

		return new AuditHistoResult<T>(entity, revision, revisionType);
	}

	protected AuditHistory<T> getHistory(AuditHistoResult<T> auditQueryResult) {
		return new AuditHistory<T>(auditQueryResult.getEntity(), auditQueryResult.getRevision().getRevisionNumber(),
				auditQueryResult.getType());
	}
}
