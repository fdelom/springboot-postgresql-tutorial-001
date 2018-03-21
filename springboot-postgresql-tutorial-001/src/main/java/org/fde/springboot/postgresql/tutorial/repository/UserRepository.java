/**
 * 
 */
package org.fde.springboot.postgresql.tutorial.repository;

import org.fde.springboot.postgresql.tutorial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author fdelom
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {

}
