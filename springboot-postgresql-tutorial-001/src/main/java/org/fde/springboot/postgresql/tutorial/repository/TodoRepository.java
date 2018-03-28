package org.fde.springboot.postgresql.tutorial.repository;

import java.util.List;

import org.fde.springboot.postgresql.tutorial.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author fdelom
 *
 */
public interface TodoRepository extends JpaRepository<Todo, Long> {

	@Query(value = "SELECT T.* FROM TODO T WHERE T.content ->>'description' = :description", nativeQuery = true)
	List<Todo> findByContentDescription(@Param(value = "description") String description);

	/*
	 * @Query(value =
	 * "SELECT T.* FROM TODO T WHERE T.content->>'tags'\\:\\:text IN :tagsList",
	 * nativeQuery = true) List<Todo> findByContentTagsIn(@Param(value =
	 * "tagsList") List<String> tagsList);
	 */

}
